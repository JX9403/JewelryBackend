package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByGameId(Long gameId);
    List<GameResult> findByUserId(Long userId);
}
