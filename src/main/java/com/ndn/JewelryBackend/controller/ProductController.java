package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.ProductRequest;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody ProductRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(productService.create(request))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                                  @RequestBody ProductRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(productService.update(id, request))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        productService.delete(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(204)
                .status(true)
                .message("Successfully!")
                .data(null)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) String gender,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] parts = sort.split(",");
        String sortField = parts[0];
        String sortDir = (parts.length > 1) ? parts[1] : "desc";

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<ProductResponse> responses =
                productService.getAll(name, categoryId, collectionId, gender, pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(responses)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(productService.getById(id))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/visual_search", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse>  visualSearch(
            @RequestParam MultipartFile file,
            @RequestParam Long categoriesId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        Page<ProductResponse> responses = productService.visualSearch(categoriesId, file, PageRequest.of(page, size));
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(responses)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
