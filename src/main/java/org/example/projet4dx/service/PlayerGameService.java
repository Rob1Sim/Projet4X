package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.Game;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGameId;
import org.example.projet4dx.model.dao.PlayerGameDao;
import org.example.projet4dx.model.PlayerGame;

import java.util.List;

public class PlayerGameService {
    private final PlayerGameDao playerGameDao;

    public PlayerGameService(EntityManager em) {
        this.playerGameDao = new PlayerGameDao(em);
    }

    public PlayerGame getPlayerGame(PlayerGameId id) {
        return playerGameDao.getById( id);
    }

    public List<PlayerGame> getAllPlayerGames() {
        return playerGameDao.getAll();
    }

    public List<PlayerGame> getPlayerGamesByPlayerId(long id) {
        return playerGameDao.getByPlayerId(id);
    }

    public List<PlayerGame> getPlayerGamesByGameId(long id) {
        return playerGameDao.getByGameId(id);
    }

    /**
     * Creates a new PlayerGame entity for the given Player and Game instances.
     *
     * @param player the Player entity to be associated with the PlayerGame
     * @param game the Game entity to be associated with the PlayerGame
     * @return the newly created PlayerGame entity
     */
    public PlayerGame createPlayerGame(Player player, Game game) {
        PlayerGame playerGame = new PlayerGame(player, game);
        playerGameDao.create(playerGame);
        return playerGame;
    }

    /**
     * Updates the score of a player in a game.
     *
     * @param id the ID of the player's game
     * @param score the new score to update for the player
     */
    public void updatePlayerScore(PlayerGameId id, int score) {
        PlayerGame playerGame = playerGameDao.getById(id);
        playerGame.setScore(score);
        playerGameDao.update(playerGame);
    }

}
