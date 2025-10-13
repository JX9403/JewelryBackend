package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.entity.Image;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CollectionRepository collectionRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        Collection collection = collectionRepository.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .gender(request.getGender())
                .views(0)
                .category(request.getCategory())
                .collection(collection)
                .build();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<Image> images = request.getImages().stream()
                    .map(img -> Image.builder()
                            .url(img.getUrl())
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

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGender(request.getGender());
        product.setCategory(request.getCategory());
        product.setCollection(collection);

        if (request.getImages() != null) {
            product.getImages().clear();
            List<Image> newImages = request.getImages().stream()
                    .map(img -> Image.builder()
                            .url(img.getUrl())
                            .build())
                    .toList();
            product.getImages().addAll(newImages);
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
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
                .category(product.getCategory())
                .collectionName(product.getCollection() != null ? product.getCollection().getName() : null)
                .images(product.getImages() != null
                        ? product.getImages().stream()
                        .map(image -> ImageResponse.builder()
                                .id(image.getId())
                                .url(image.getUrl())
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
}
