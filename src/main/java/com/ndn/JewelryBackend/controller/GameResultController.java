package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.GameResultRequest;
import com.ndn.JewelryBackend.dto.response.GameResultResponse;
import com.ndn.JewelryBackend.service.GameResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-results")
@RequiredArgsConstructor
public class GameResultController {

    private final GameResultService gameResultService;

    @PostMapping
    public GameResultResponse create(@RequestBody GameResultRequest request) {
        return gameResultService.create(request);
    }

    @PutMapping("/{id}")
    public GameResultResponse update(@PathVariable Long id, @RequestBody GameResultRequest request) {
        return gameResultService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        gameResultService.delete(id);
    }

    @GetMapping("/{id}")
    public GameResultResponse getById(@PathVariable Long id) {
        return gameResultService.getById(id);
    }

    @GetMapping
    public List<GameResultResponse> getAll() {
        return gameResultService.getAll();
    }

    @GetMapping("/game/{gameId}")
    public List<GameResultResponse> getByGame(@PathVariable Long gameId) {
        return gameResultService.getByGame(gameId);
    }

    @GetMapping("/user/{userId}")
    public List<GameResultResponse> getByUser(@PathVariable Long userId) {
        return gameResultService.getByUser(userId);
    }
}
