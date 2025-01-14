package org.example.projet4dx.model.gameEngine.engine;

import org.example.projet4dx.model.Game;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.gameEngine.*;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.tile.CityTile;
import org.example.projet4dx.model.gameEngine.tile.ForestTile;
import org.example.projet4dx.model.gameEngine.tile.Map;
import org.example.projet4dx.model.gameEngine.tile.Tile;
import org.example.projet4dx.model.gameEngine.utils.Coordinates;
import org.example.projet4dx.model.gameEngine.utils.Direction;
import org.example.projet4dx.service.GameService;
import org.example.projet4dx.service.PlayerGameService;
import org.example.projet4dx.service.PlayerService;
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
    private static final List<String> lieux = new ArrayList<>();


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
        useCity();
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
     * Iterates through all CityTiles on the map and triggers their use() method if the CityTile is owned by the current player.
     * Only the CityTiles belonging to the current player are interacted with.
     */
    private void useCity(){
        PlayerDTO playerDTO = getCurrentPlayer();
        for (CityTile cityTile: map.getAllCitiesFromAPlayer(playerDTO)){
            cityTile.use();
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
    public boolean moveSoldier(Soldier soldier, Direction direction) {
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
        if (newCoordinates.getX() >= getMap().getWidth() || newCoordinates.getY() >= getMap().getHeight()) {
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.MOVEMENT,"Le soldat ne peut pas se déplacer vers "+direction+" !"));
            return false;
        }

        Soldier potentialSoldier = Soldier.getSoldierByCoordinates(newCoordinates);
        if (potentialSoldier != null && soldier.getPlayerDTO().getSoldierBySoldier(potentialSoldier) == null) {
            soldier.attack(potentialSoldier);
            if (potentialSoldier.getHP() >0){
                return true;
            }
        }
        Tile futureTile = GameInstance.getInstance().getMap().getTileAtCoord(newCoordinates);
        if (futureTile.collide(soldier)){
            soldier.setCoordinates(newCoordinates);
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.MOVEMENT,"Le soldat se déplace vers "+direction+" !"));
            return true;
        }else{
            if (futureTile.getType() instanceof CityTile cityTile && cityTile.getDefendPoint()>0){
                return true;
            }
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.MOVEMENT,"Le soldat ne peut pas se déplacer vers "+direction+" !"));
            return false;
        }
    }


    /**
     * Checks if any player has won the game based on their score or available soldiers.
     *
     * @return The PlayerDTO object of the winning player if someone has won the game, or null if no one has won yet.
     */
    public PlayerDTO hasSomeoneWon(){
        for (PlayerDTO player : players) {
            if (player.getScore() >= 500 ) {
                GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, "Le joueur " + player.getLogin() + " a gagné la partie !"));
                persistGameScore();
                GameInstance.resetInstance();
                return player;
            }
            if(player.getSoldiers().isEmpty()){
                persistGameScore();
                players.remove(player);
                if (players.size() <= 1){
                    if (players.isEmpty()){
                        GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, "Personne n'a gagné !"));
                        return null;
                    }
                    GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, "Le joueur " + players.get(0).getLogin() + " a gagné la partie !"));
                    GameInstance.resetInstance();
                    return players.get(0);
                }
            }
        }
        return null;
    }

    /**
     * Persists the game scores for each player by updating their scores in the database.
     * Iterates through all players in the game instance and retrieves their corresponding Player and PlayerGame entities.
     * Updates the player's score in the PlayerGame entity with the new score value from the PlayerDTO object.
     */
    private void persistGameScore(){
        for (PlayerDTO player : players) {
            PlayerService playerService = new PlayerService(PersistenceManager.getEntityManager());
            PlayerGameService playerGameService = new PlayerGameService(PersistenceManager.getEntityManager());
            Player player1 = playerService.getByLogin(player.getLogin());
            PlayerGame pg = playerGameService.getPlayerGameByPlayerIdAndGameId(player1.getId(),game.getId());
            playerGameService.updatePlayerScore(pg.getId(), player.getScore());
        }
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    /**
     * Generates a unique city name by selecting a random city from the available cities in the GameInstance class.
     * If no cities are available, a default city name "cité" is returned.
     *
     * @return A unique city name that has been randomly selected from the list of cities.
     */
    public static String getUniqueCityName(){
        if (GameInstance.lieux.isEmpty()){
            return "cité";
        }
        Random random = new Random();
        int index = random.nextInt(GameInstance.lieux.size());
        String city = GameInstance.lieux.get(index);
        GameInstance.lieux.remove(index);
        return city;
    }

    public static void addCity(){
        lieux.add("Whiterun");
        lieux.add("Solitude");
        lieux.add("Windhelm");
        lieux.add("Riften");
        lieux.add("Markarth");
        lieux.add("Winterhold");
        lieux.add("Falkreath");
        lieux.add("Dawnstar");
        lieux.add("Morthal");

        // Ajout des villages
        lieux.add("Riverwood");
        lieux.add("Ivarstead");
        lieux.add("Kynesgrove");
        lieux.add("Rorikstead");
        lieux.add("Dragon Bridge");
        lieux.add("Shor's Stone");
        lieux.add("Helgen");

        // Ajout des forteresses
        lieux.add("Fort Greymoor");
        lieux.add("Fort Amol");
        lieux.add("Fort Sungard");
        lieux.add("Fort Greenwall");
        lieux.add("Fort Snowhawk");
        lieux.add("Fort Hraggstad");
        lieux.add("Fort Dawnguard");

        // Ajout des autres lieux
        lieux.add("High Hrothgar");
        lieux.add("Blackreach");
        lieux.add("Labyrinthian");
        lieux.add("Sky Haven Temple");
        lieux.add("Throat of the World");
        lieux.add("Bleak Falls Barrow");
        lieux.add("Ustengrav");
        lieux.add("Sovngarde");
        lieux.add("The Ragged Flagon");
        lieux.add("Castle Volkihar");
        lieux.add("The Bannered Mare");
    }
}
