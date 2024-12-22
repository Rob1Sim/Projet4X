package org.example.projet4dx.service;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.dao.PlayerDAO;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.util.AuthenticationUtil;
import org.example.projet4dx.util.StringEscapeUtils;
import org.example.projet4dx.util.exceptions.AuthenticationException;

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



    /**
     * Logs in a player with the provided login and password, saving the player in the session if successful.
     *
     * @param login the login name of the player
     * @param password the password of the player
     * @param req the HttpServletRequest object for session management
     * @return true if the player is successfully logged in, false otherwise
     * @throws AuthenticationException if login or password is empty or authentication fails
     */
    public boolean loginPlayer(String login, String password, HttpServletRequest req) throws AuthenticationException {
        login = StringEscapeUtils.sanitizeString(login);
        password = StringEscapeUtils.sanitizeString(password);
        if (login.isEmpty() || password.isEmpty()) {
            throw new AuthenticationException("Login and password are empty");
        }
        Player player = getByLogin(login);
        if (player != null && player.getPassword().equals(password)){
            AuthenticationUtil.saveCurrentPlayer(player,req);
            return true;
        }
        if (player == null) {
            registerPlayer(login, password);
            Player newPlayer = getByLogin(login);
            AuthenticationUtil.saveCurrentPlayer(newPlayer,req);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of PlayerGame entities associated with the given Player.
     *
     * @param em the EntityManager for performing database operations
     * @param player the Player entity for whom to retrieve the list of games
     * @return a List of PlayerGame entities for the specified Player
     */
    public List<PlayerGame> getPlayerGames(EntityManager em, Player player) {
        PlayerGameService playerGameService = new PlayerGameService(em);
        return playerGameService.getPlayerGamesByPlayerId(player.getId());
    }

    /**
     * Calculates the mean score of a given player based on their games.
     *
     * @param em the EntityManager for performing database operations
     * @param player the Player entity for which to calculate the mean score
     * @return the mean score of the player, or 0 if no games are found
     */
    public double getPlayerMeanScore(EntityManager em, Player player) {
        List<PlayerGame> playerGames = getPlayerGames(em, player);
        double meanScore = 0;
        if (!playerGames.isEmpty()) {
            double totalScore = 0;
            for (PlayerGame playerGame : playerGames) {
                totalScore += playerGame.getScore();
            }
            meanScore = totalScore / playerGames.size();
        }
        return meanScore;
    }

    /**
     * Retrieves the minimum score achieved by a player in any games.
     *
     * @param em the EntityManager used for database operations
     * @param player the Player entity for whom to get the minimum score
     * @return the minimum score achieved by the player in their games, or 0 if no games are found
     */
    public int getMinimumPlayerScore(EntityManager em, Player player) {
        List<PlayerGame> playerGames = getPlayerGames(em, player);
        if (playerGames.isEmpty()) {
            return 0;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (PlayerGame playerGame : playerGames) {
                if (playerGame.getScore() < minScore) {
                    minScore = playerGame.getScore();
                }
            }
            return minScore;
        }
    }

    /**
     * Retrieves the maximum score achieved by the specified player in any game.
     *
     * @param em the EntityManager to interact with the database
     * @param player the Player for whom to retrieve the maximum score
     * @return the highest score achieved by the player, or 0 if the player has no recorded games
     */
    public int getMaximumPlayerScore(EntityManager em, Player player) {
        List<PlayerGame> playerGames = getPlayerGames(em, player);
        if (playerGames.isEmpty()) {
            return 0;
        } else {
            int maxScore = Integer.MIN_VALUE;
            for (PlayerGame playerGame : playerGames) {
                if (playerGame.getScore() > maxScore) {
                    maxScore = playerGame.getScore();
                }
            }
            return maxScore;
        }
    }

}
