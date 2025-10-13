package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.GameResultRequest;
import com.ndn.JewelryBackend.dto.response.GameResultResponse;

import java.util.List;

public interface GameResultService {
    GameResultResponse create(GameResultRequest request);
    void delete(Long id);
    List<GameResultResponse> getAll();
}
