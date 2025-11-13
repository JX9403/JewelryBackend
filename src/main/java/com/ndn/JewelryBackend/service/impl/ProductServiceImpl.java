package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.dto.response.ProductSimilarityProjection;
import com.ndn.JewelryBackend.entity.*;
import com.ndn.JewelryBackend.exception.InsufficientStockException;
import com.ndn.JewelryBackend.repository.CategoryRepository;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.service.AIService;
import com.ndn.JewelryBackend.service.ProductService;
import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CollectionRepository collectionRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    @Override
    public ProductResponse create(ProductRequest request) {
        Collection collection = collectionRepository.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .gender(request.getGender())
                .views(0)
                .category(category)
                .collection(collection)
                .build();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<Image> images = request.getImages().stream()
                    .map(img -> Image.builder()
                            .url(img.getUrl())
                            .isUsedForAI(img.getIsUsedForAI())
                            .build())
                    .toList();
            product.setImages(images);
        }

        Product saved = productRepository.save(product);

        return toResponse(saved);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Collection collection = collectionRepository.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGender(request.getGender());
        product.setCategory(category);
        product.setCollection(collection);

        // Cập nhật danh sách ảnh
        if (request.getImages() != null) {
            List<Image> updatedImages = request.getImages().stream()
                    .map(imgReq -> {
                        if (imgReq.getId() != null) {
                            // ảnh cũ -> cập nhật lại
                            return product.getImages().stream()
                                    .filter(img -> img.getId().equals(imgReq.getId()))
                                    .findFirst()
                                    .map(existing -> {
                                        existing.setUrl(imgReq.getUrl());
                                        existing.setIsUsedForAI(imgReq.getIsUsedForAI());
                                        return existing;
                                    })
                                    .orElseGet(() -> Image.builder()
                                            .url(imgReq.getUrl())
                                            .isUsedForAI(imgReq.getIsUsedForAI())
                                            .build());
                        } else {
                            // ảnh mới
                            return Image.builder()
                                    .url(imgReq.getUrl())
                                    .isUsedForAI(imgReq.getIsUsedForAI())
                                    .build();
                        }
                    })
                    .toList();

            product.getImages().clear();
            product.getImages().addAll(updatedImages);
        }

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponse> getAll(String name, Long categoryId, Long collectionId, String gender, Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setViews(product.getViews() + 1);
        productRepository.save(product);

        return toResponse(product);
    }

    ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .gender(product.getGender())
                .views(product.getViews())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .collectionName(product.getCollection() != null ? product.getCollection().getName() : null)
                .images(product.getImages() != null
                        ? product.getImages().stream()
                        .map(image -> ImageResponse.builder()
                                .id(image.getId())
                                .url(image.getUrl())
                                .isUsedForAI(image.getIsUsedForAI())
                                .build())
                        .toList()
                        : List.of())
                .build();
    }

    @Override
    public Page<ProductResponse> getProductsByCollection( Long collectionId, Pageable pageable) {
        Page<Product> page = productRepository.findByCollectionId(collectionId, pageable);
        return page.map(this::toResponse);

    }
    @Override
    public Page<ProductResponse> getProductsByCategory( Long categoryId, Pageable pageable) {
        Page<Product> page = productRepository.findByCategoryId(categoryId, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public Page<ProductResponse> visualSearch(Long categoryID, MultipartFile file, Pageable pageable) throws InsufficientStockException {
        PGvector pGvector = aiService.productAnalyze(file);
        if(pGvector.isNull()) throw new InsufficientStockException("Vector null");

        Page<ProductSimilarityProjection> idPage = productRepository.findSimilarProductIds(categoryID, pGvector.toString(), pageable);

        List<Long> productIds = idPage.getContent().stream().map(ProductSimilarityProjection::getId).toList();

        if(productIds.isEmpty()) return Page.empty(pageable);

        List<Product> productsWithImages = productRepository.findProductsWithImagesByIds(productIds);

        // Tạo Map để tra cứu Product (vì list này chưa được sắp xếp)
        Map<Long, Product> productMap = productsWithImages.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<ProductResponse> responses = productIds.stream().map(id -> productMap.get(id))
                .map(this::toResponse).toList();

        return new PageImpl<>(
                responses,
                pageable,
                idPage.getTotalElements()
        );
    }
}
