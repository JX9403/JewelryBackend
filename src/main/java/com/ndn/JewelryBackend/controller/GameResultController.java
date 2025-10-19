package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.GameResultRequest;
import com.ndn.JewelryBackend.dto.response.GameResultResponse;
import com.ndn.JewelryBackend.service.GameResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/game-results")
@RequiredArgsConstructor
public class GameResultController {

    private final GameResultService gameResultService;

    @PostMapping
    public ResponseEntity<GameResultResponse> create(@RequestBody GameResultRequest request) {
        GameResultResponse response = gameResultService.create(request);
        return ResponseEntity
                .created(URI.create("/api/game-results/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameResultService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping
    public ResponseEntity<List<GameResultResponse>> getAll() {
        List<GameResultResponse> responses = gameResultService.getAll();
        return ResponseEntity.ok(responses); // 200 OK
    }
}
