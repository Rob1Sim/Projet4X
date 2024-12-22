package org.example.projet4dx.model;

import org.example.projet4dx.model.gameEngine.PlayerDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a singleton instance of a game session. It manages the game state, players, and game turns.
 */
public class GameInstance {
    private static GameInstance instance;
    private Game game;
    private final List<PlayerDTO> players;
    private Map map;
    private int currentPlayerTurn;

    private GameInstance() {
        game = new Game();
        game.setDate(LocalDate.now());
        players = new ArrayList<>();
        this.currentPlayerTurn = 0;
    }

    /**
     * This method returns the singleton instance of the GameInstance class.
     * If the instance has not been created yet, a new instance is created and returned.
     *
     * @return The singleton instance of the GameInstance class.
     */
    public static synchronized GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }

    /**
     * Adds a player to the game session. Players represent participants in the game.
     *
     * @param player the Player object to be added to the game session
     */
    public synchronized void addPlayer(PlayerDTO player) {
        players.add(player);
    }

    /**
     * This method advances the turn to the next player in the game session.
     * If the current player at the end of the list, it wraps around to the first player in the list.
     */
    public synchronized void nextTurn() {
        currentPlayerTurn = (currentPlayerTurn + 1) % players.size();
    }

    /**
     * Returns the current player taking their turn in the game session.
     *
     * @return The current player object representing the player taking the turn.
     */
    public synchronized PlayerDTO getCurrentPlayer() {
        return players.get(currentPlayerTurn);
    }

    /**
     * Retrieves the map associated with the game instance.
     *
     * @return The map object representing the game map.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Resets the singleton instance of the GameInstance class to null, allowing for a new instance to be created upon the next call to getInstance().
     * This method is synchronized to ensure thread safety when resetting the instance.
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    public Game getGame() {
        return game;
    }
}
