package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.*;
import com.ndn.JewelryBackend.repository.CategoryRepository;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CollectionRepository collectionRepository;
    private final CategoryRepository categoryRepository;

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
    public Page<ProductResponse> getAll(String name, Long categoryId, Long collectionId, BigDecimal priceTo, BigDecimal priceFrom, String gender, Pageable pageable) {
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

    private ProductResponse toResponse(Product product) {
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
    public List<ProductResponse> getProductsByCollection(Long collectionId) {
        return productRepository.findByCollectionId(collectionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
