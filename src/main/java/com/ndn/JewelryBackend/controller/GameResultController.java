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


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        gameResultService.delete(id);
    }

    @GetMapping
    public List<GameResultResponse> getAll() {
        return gameResultService.getAll();
    }

}
