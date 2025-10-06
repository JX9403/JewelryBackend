package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public CollectionResponse create(@RequestBody CollectionRequest request) {
        return collectionService.create(request);
    }

    @PutMapping("/{id}")
    public CollectionResponse update(@PathVariable Long id, @RequestBody CollectionRequest request) {
        return collectionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @GetMapping
    public List<CollectionResponse> getAll() {
        return collectionService.getAll();
    }

    @GetMapping("/{id}")
    public CollectionResponse getById(@PathVariable Long id) {
        return collectionService.getById(id);
    }
}
