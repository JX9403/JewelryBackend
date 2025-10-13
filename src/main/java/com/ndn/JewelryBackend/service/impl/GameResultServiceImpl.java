package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.GameResultRequest;
import com.ndn.JewelryBackend.dto.response.GameResultResponse;
import com.ndn.JewelryBackend.entity.Game;
import com.ndn.JewelryBackend.entity.GameResult;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.ActiveStatus;
import com.ndn.JewelryBackend.repository.GameRepository;
import com.ndn.JewelryBackend.repository.GameResultRepository;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.GameResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameResultServiceImpl implements GameResultService {

    private final GameResultRepository gameResultRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private GameResultResponse mapToDto(GameResult entity) {
        return GameResultResponse.builder()
                .id(entity.getId())
                .gameId(entity.getGame().getId())
                .gameName(entity.getGame().getName())
                .userId(entity.getUser().getId())
                .userName(entity.getUser().getUsername())
                .score(entity.getScore())
                .build();
    }

    @Override
    public GameResultResponse create(GameResultRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        GameResult result = GameResult.builder()
                .game(game)
                .user(user)
                .score(request.getScore())
                .build();

        return mapToDto(gameResultRepository.save(result));
    }


    @Override
    public void delete(Long id) {
        gameResultRepository.deleteById(id);
    }

    @Override
    public List<GameResultResponse> getAll() {
        return gameResultRepository.findTop10ByGameStatusOrderByScoreDesc(ActiveStatus.ON)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

}
