package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.entity.Game;
import com.ndn.JewelryBackend.enums.ActiveStatus;

import java.util.List;

public interface GameService {
    Game create(Game game);
    Game update(Long id, Game game);
    void delete(Long id);
    Game getById(Long id);
    List<Game> getAll();
}

