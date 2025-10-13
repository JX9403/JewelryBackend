package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.GameResult;
import com.ndn.JewelryBackend.enums.ActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    @Query("SELECT gr FROM GameResult gr WHERE gr.game.status = :status ORDER BY gr.score DESC")
    List<GameResult> findTop10ByGameStatusOrderByScoreDesc(@Param("status") ActiveStatus status);
}
