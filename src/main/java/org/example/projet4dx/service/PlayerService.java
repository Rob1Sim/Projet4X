package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.dao.PlayerDAO;
import org.example.projet4dx.model.Player;

import java.util.List;

/**
 * PlayerService class provides methods for managing Player entities using PlayerDAO.
 */
public class PlayerService {
    private final PlayerDAO playerDAO;

    /**
     * Initialize a new PlayerService with the provided EntityManager.
     *
     * @param em the EntityManager to be used for database operations
     */
    public PlayerService(EntityManager em) {
        this.playerDAO = new PlayerDAO(em);
    }

    /**
     * Retrieves all players from the database.
     *
     * @return a List of Player entities representing all players in the database
     */
    public List<Player> getAllPlayers() {
        return playerDAO.getAll();
    }

    /**
     * Retrieves a player entity by its ID.
     *
     * @param id the ID of the player to retrieve
     * @return the player entity with the given ID, or null if not found
     */
    public Player getPlayerById(Long id) {
        return playerDAO.getById(Player.class,id);
    }

    /**
     * Registers a new player with the provided login and password.
     *
     * @param login the login name for the new player
     * @param password the password for the new player
     */
    public void registerPlayer(String login, String password) {
        Player player = new Player();
        player.setLogin(login);
        player.setPassword(password);
        playerDAO.create(player);
    }

    /**
     * Retrieves a player entity by the provided login.
     *
     * @param login the login of the player to retrieve
     * @return the player entity with the specified login
     */
    public Player getByLogin(String login) {
        return playerDAO.getByLogin(login);
    }
}
