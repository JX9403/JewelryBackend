package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.GameResultRequest;
import com.ndn.JewelryBackend.dto.response.GameResultResponse;

import java.util.List;

public interface GameResultService {
    GameResultResponse create(GameResultRequest request);
    GameResultResponse update(Long id, GameResultRequest request);
    void delete(Long id);
    GameResultResponse getById(Long id);
    List<GameResultResponse> getAll();
    List<GameResultResponse> getByGame(Long gameId);
    List<GameResultResponse> getByUser(Long userId);
}
