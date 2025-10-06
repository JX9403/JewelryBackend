package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.entity.Image;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.repository.ImageRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CollectionRepository collectionRepository;
    private final ImageRepository imageRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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

        if (request.getImageUrls() != null) {
            List<Image> images = request.getImageUrls().stream()
                    .map(url -> Image.builder().url(url).product(product).build())
                    .toList();
            product.setImages(images);
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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

        if (request.getImageUrls() != null) {
            product.getImages().clear();
            List<Image> newImages = request.getImageUrls().stream()
                    .map(url -> Image.builder().url(url).product(product).build())
                    .toList();
            product.getImages().addAll(newImages);
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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
                        ? product.getImages().stream().map(Image::getUrl).toList()
                        : List.of())
                .build();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponse> getProductsByCollection(Long collectionId) {
        return productRepository.findByCollectionId(collectionId)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .gender(product.getGender())
                        .views(product.getViews())
                        .category(product.getCategory())
                        .collectionName(product.getCollection() != null ? product.getCollection().getName() : null)
                        .images(product.getImages() != null
                                ? product.getImages().stream().map(img -> img.getUrl()).toList()
                                : List.of())
                        .build()
                )
                .toList();
    }

}
