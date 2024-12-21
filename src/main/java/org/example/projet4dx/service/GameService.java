package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.dao.GameDAO;
import org.example.projet4dx.model.Game;

import java.util.List;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(EntityManager em) {
        this.gameDAO = new GameDAO(em);
    }

    public List<Game> getAllGames() {
        return gameDAO.getAll();
    }

    public Game getGame(int id) {
        return gameDAO.getById(Game.class, id);
    }
}
