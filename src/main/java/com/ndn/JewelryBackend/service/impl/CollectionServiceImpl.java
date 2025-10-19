package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.dto.response.UserVoucherResponse;
import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.entity.UserVoucher;
import com.ndn.JewelryBackend.repository.CollectionRepository;
import com.ndn.JewelryBackend.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return toResponse(collection);
    }

    @Override
    public CollectionResponse update(Long id, CollectionRequest request) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found!"));
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        return toResponse(collectionRepository.save(collection));
    }

    @Override
    public void delete(Long id) {
        if (!collectionRepository.existsById(id)) {
            throw new RuntimeException("Collection not found!");
        }
        collectionRepository.deleteById(id);
    }

    @Override
    public Page<CollectionResponse> getAll(String name, Pageable pageable) {
        Page<Collection> page = collectionRepository.findAll(name, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public CollectionResponse getById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found!"));
        return toResponse(collection);
    }


    private CollectionResponse toResponse(Collection collection) {
        return CollectionResponse.builder()
                .id(collection.getId())
                .description(collection.getDescription())
                .name(collection.getName())
                .build();
    }
}
