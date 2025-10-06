package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Override
    public CollectionResponse create(CollectionRequest request) {
        if (collectionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Collection name already exists!");
        }
        Collection collection = Collection.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        collectionRepository.save(collection);
        return mapToResponse(collection);
    }

    @Override
    public CollectionResponse update(Long id, CollectionRequest request) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found!"));
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        return mapToResponse(collectionRepository.save(collection));
    }

    @Override
    public void delete(Long id) {
        if (!collectionRepository.existsById(id)) {
            throw new RuntimeException("Collection not found!");
        }
        collectionRepository.deleteById(id);
    }

    @Override
    public List<CollectionResponse> getAll() {
        return collectionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CollectionResponse getById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found!"));
        return mapToResponse(collection);
    }

    private CollectionResponse mapToResponse(Collection collection) {
        return CollectionResponse.builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .build();
    }
}
