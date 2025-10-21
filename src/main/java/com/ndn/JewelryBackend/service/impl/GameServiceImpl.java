package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.dto.response.GameResponse;
import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.entity.Game;
import com.ndn.JewelryBackend.enums.ActiveStatus;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.repository.GameRepository;
import com.ndn.JewelryBackend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Game create(Game game) {
        game.setId(null);
        game.setStatus(ActiveStatus.OFF);
        return gameRepository.save(game);
    }

    @Override
    public Game update(Long id, Game game) {
        Game existing = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        existing.setName(game.getName());
        existing.setStatus(game.getStatus());
        existing.setUrl(game.getUrl());
        return gameRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game not found");
        }
        gameRepository.deleteById(id);
    }

    @Override
    public Game getById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
    }

    @Override
    public Page<GameResponse> getAll(String name, Pageable pageable) {
        Page<Game> page = gameRepository.findAll(name, pageable);
        return page.map(this::toResponse);
    }

    private GameResponse toResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .build();
    }
}
