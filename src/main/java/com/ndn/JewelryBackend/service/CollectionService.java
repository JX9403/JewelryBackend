package com.ndn.JewelryBackend.service;


import com.ndn.JewelryBackend.dto.request.CollectionRequest;
import com.ndn.JewelryBackend.dto.response.CollectionResponse;

import java.util.List;

public interface CollectionService {
    CollectionResponse create(CollectionRequest request);
    CollectionResponse update(Long id, CollectionRequest request);
    void delete(Long id);
    List<CollectionResponse> getAll();
    CollectionResponse getById(Long id);
}
