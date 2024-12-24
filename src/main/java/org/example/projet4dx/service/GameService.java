package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.dao.GameDAO;
import org.example.projet4dx.model.Game;

import java.time.LocalDate;
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


    /**
     * Creates a new game instance with the current date and persists it using the GameDAO.
     *
     * @return the newly created Game object
     */
    public Game createGame() {
        Game game = new Game();
        game.setDate(LocalDate.now());
        gameDAO.create(game);
        return game;
    }
}
