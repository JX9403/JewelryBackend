package com.ndn.JewelryBackend.service;


import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CollectionService {
    CollectionResponse create(CollectionRequest request);
    CollectionResponse update(Long id, CollectionRequest request);
    void delete(Long id);
    Page<CollectionResponse> getAll(String name, Pageable pageable);
    CollectionResponse getById(Long id);
    Page<ProductResponse> getProducts(Long categoryId, Pageable pageable);
}
