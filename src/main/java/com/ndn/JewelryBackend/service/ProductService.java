package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.exception.InsufficientStockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);

    Page<ProductResponse> getAll(String name, Long categoryId, Long collectionId, String gender, Pageable pageable);

    ProductResponse getById(Long id);

    Page<ProductResponse> getProductsByCollection( Long collectionId, Pageable pageable);
    Page<ProductResponse> getProductsByCategory( Long categoryId, Pageable pageable);

    ProductResponse toResponse(Product product);
    Page<ProductResponse> visualSearch(Long categoryID , MultipartFile file, Pageable pageable) throws InsufficientStockException;
}
