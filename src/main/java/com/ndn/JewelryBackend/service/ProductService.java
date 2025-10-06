package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);

    List<ProductResponse> getAll();

    ProductResponse getById(Long id);

    List<ProductResponse> getProductsByCollection(Long collectionId);
}
