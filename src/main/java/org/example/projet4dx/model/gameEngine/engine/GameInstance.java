package org.example.projet4dx.model.gameEngine.engine;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.Game;
import org.example.projet4dx.model.gameEngine.*;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.tile.ForestTile;
import org.example.projet4dx.model.gameEngine.tile.Map;
import org.example.projet4dx.model.gameEngine.utils.Coordinates;
import org.example.projet4dx.model.gameEngine.utils.Direction;
import org.example.projet4dx.service.GameService;
import org.example.projet4dx.util.PersistenceManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a singleton instance of a game session. It manages the game state, players, and game turns.
 */
public class GameInstance {
    private static GameInstance instance;
    private final Game game;
    private final List<PlayerDTO> players;
    private final Map map;
    private int currentPlayerTurn;



    private GameInstance() {
        GameService gameService = new GameService(PersistenceManager.getEntityManager());
        game = gameService.createGame();
        map = new Map();
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

    public static synchronized boolean isCreated() {
        return instance != null;
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

    /**
     * Retrieves a player object from the list of players based on the provided PlayerDTO object.
     *
     * @param player The PlayerDTO object to search for in the player list.
     * @return The found PlayerDTO object if present, otherwise null.
     */
    public PlayerDTO getPlayerByPlayer(PlayerDTO player) {
        return players.stream().filter(p -> p.equals(player)).findFirst().orElse(null);
    }

    /**
     * Retrieves all the soldiers from all players in the game instance.
     *
     * @return A list of Soldier objects representing all the soldiers in the game.
     */
    public List<Soldier> getAllSoldiers(){
        List<Soldier> allSoldiers = new ArrayList<>();
        for (PlayerDTO player : players) {
            allSoldiers.addAll(player.getSoldiers());
        }
        return allSoldiers;
    }


    /**
     * Checks if the Soldier is on a ForestTile and triggers the use() method of the tile.
     *
     * @param soldier the Soldier object to check if on a ForestTile
     */
    public static void useForestAction(Soldier soldier) {
        if (GameInstance.getInstance().getMap().getTileAtCoord(soldier.getCoordinates()).getType() instanceof ForestTile) {
            GameInstance.getInstance().getMap().getTileAtCoord(soldier.getCoordinates()).getType().use();
        }
    }

    /**
     * Heals the specified Soldier by a random amount of HP based on a probability.
     *
     * @param soldier the Soldier object to be healed
     */
    public static void healSoldierAction(Soldier soldier) {
        if (soldier.getHP() <= Soldier.MAX_HP) {
            Random random = new Random();
            int healAmount = random.nextInt(3) + 1;
            int probability = random.nextInt(100);
            if (probability < 50) {
                healAmount = 1;
            }
            soldier.setHp(Math.min(soldier.getHP() + healAmount, Soldier.MAX_HP));
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,"Le soldat de "+soldier.getPlayerDTO().getLogin()+" récupère "+healAmount+ " !"));

        }
    }

    /**
     * Moves the Soldier object in the specified direction.
     *
     * @param soldier the Soldier object to be moved
     * @param direction the direction in which to move the Soldier
     */
    public void moveSoldier(Soldier soldier, Direction direction) {
        Coordinates newCoordinates = new Coordinates(soldier.getCoordinates().getX(), soldier.getCoordinates().getY());
        switch (direction) {
            case EAST:
                newCoordinates.right();
                break;
            case SOUTH:
                newCoordinates.down();
                break;
            case NORTH:
                newCoordinates.up();
                break;
            case WEST:
                newCoordinates.left();
                break;
        }

        Soldier potentialSoldier = Soldier.getSoldierByCoordinates(newCoordinates);
        if (potentialSoldier != null && soldier.getPlayerDTO().getSoldierBySoldier(potentialSoldier) == null) {
            soldier.attack(potentialSoldier);
        }
        if (GameInstance.getInstance().getMap().getTileAtCoord(newCoordinates).collide(soldier)){
            soldier.setCoordinates(newCoordinates);
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.MOVEMENT,"Le soldat se déplace vers "+direction+" !"));
        }else
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.MOVEMENT,"Le soldat ne peut pas se déplacer vers "+direction+" !"));
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }
}
