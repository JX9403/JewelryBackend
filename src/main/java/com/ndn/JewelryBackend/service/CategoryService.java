package com.ndn.JewelryBackend.service;


import com.ndn.JewelryBackend.dto.request.CategoryRequest;
import com.ndn.JewelryBackend.dto.response.CategoryResponse;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
    Page<CategoryResponse> getAll(String name, Pageable pageable);
    CategoryResponse getById(Long id);
}
