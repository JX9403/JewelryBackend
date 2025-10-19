package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.CategoryRequest;
import com.ndn.JewelryBackend.dto.response.CategoryResponse;
import com.ndn.JewelryBackend.dto.response.CategoryResponse;
import com.ndn.JewelryBackend.dto.response.CategoryResponse;
import com.ndn.JewelryBackend.entity.Category;
import com.ndn.JewelryBackend.entity.Category;
import com.ndn.JewelryBackend.entity.Category;
import com.ndn.JewelryBackend.repository.CategoryRepository;
import com.ndn.JewelryBackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists!");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
        return mapToResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found!");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryResponse> getAll(String name, Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(name, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        return mapToResponse(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .description(category.getDescription())
                .name(category.getName())
                .build();
    }
}
