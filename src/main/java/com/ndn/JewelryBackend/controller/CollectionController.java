package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CollectionRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data( collectionService.create(request))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                                     @RequestBody CollectionRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(collectionService.update(id, request))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        collectionService.delete(id);
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] parts = sort.split(",");
        String sortField = parts[0];
        String sortDir = (parts.length > 1) ? parts[1] : "desc";

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<CollectionResponse> result =
                collectionService.getAll(name, pageable);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(result)
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
                .data(collectionService.getById(id))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{collectionId}/products")
    public ResponseEntity<ApiResponse> getProducts(@PathVariable Long collectionId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt,desc") String sort) {
        String[] parts = sort.split(",");
        String sortField = parts[0];
        String sortDir = (parts.length > 1) ? parts[1] : "desc";

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<ProductResponse> result =
                collectionService.getProducts(collectionId, pageable);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(result)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
