package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.CollectionResponse;
import com.ndn.JewelryBackend.dto.response.GameResponse;
import com.ndn.JewelryBackend.entity.Game;
import com.ndn.JewelryBackend.enums.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameService {
    Game create(Game game);
    Game update(Long id, Game game);
    void delete(Long id);
    Game getById(Long id);
    Page<GameResponse> getAll(String name, Pageable pageable);

}

