package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.dao.PlayerGameDao;
import org.example.projet4dx.model.PlayerGame;

import java.util.List;

public class PlayerGameService {
    private final PlayerGameDao playerGameDao;

    public PlayerGameService(EntityManager em) {
        this.playerGameDao = new PlayerGameDao(em);
    }

    public PlayerGame getPlayerGame(long id) {
        return playerGameDao.getById(PlayerGame.class, id);
    }

    public List<PlayerGame> getAllPlayerGames() {
        return playerGameDao.getAll();
    }

    public List<PlayerGame> getPlayerGamesByPlayerId(long id) {
        return playerGameDao.getByPlayerId(id);
    }
}
