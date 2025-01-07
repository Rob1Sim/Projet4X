package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.tile.CityTile;
import org.example.projet4dx.model.gameEngine.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a data transfer object (DTO) for a Player in a game session.
 * It contains information such as login, score, production point, and a list of soldiers.
 */
public class PlayerDTO {
    private final String login;
    private int score;
    private int productionPoint;
    private final List<Soldier> soldiers;


    public PlayerDTO(Player player) {
        this.login = player.getLogin();
        this.score = 0;
        this.productionPoint = 0;
        this.soldiers = new ArrayList<>();
        spawnFirstSoldier();
    }

    private void addSoldier(Soldier soldier) {
        this.soldiers.add(soldier);
    }
    public List<Soldier> getSoldiers() {
        return soldiers;
    }
    public void removeSoldier(Soldier soldier) {
        this.soldiers.remove(soldier);
    }

    /**
     * Recruits a Soldier for the player if enough production points are available.
     * The player must have at least 15 production points to recruit a Soldier.
     * If there are enough production points, a new Soldier object is created and placed on an empty tile.
     * If no empty tile is available, an IllegalStateException is thrown.
     * If there are not enough production points, an IllegalStateException is thrown.
     */
    public void recruitSoldier(){
        List<CityTile> cities = GameInstance.getInstance().getMap().getAllCitiesFromAPlayer(this);
        if (this.productionPoint - 15 >= 0 ){
            if(!cities.isEmpty()){
                Soldier soldier = new Soldier(this);
                Tile spawnCoordinates = GameInstance.getInstance().getMap().getEmptyCity(this);
                if (spawnCoordinates != null) {
                    soldier.setCoordinates(spawnCoordinates.getCoordinates());
                    this.addSoldier(soldier);
                    GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, this.getLogin()+" a recruté un nouveau soldat !"));
                    this.productionPoint -=15;
                }else {
                    GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, "Toutes vos villes sont occupées, vous ne pouvez pas recruter de soldat !"));
                }
            }else {
                GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, this.getLogin()+" tu n'as pas de villes pour recruter un nouveau soldat !"));
            }

        }else {
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, this.getLogin()+" tu n'as pas assez de points de production pour recruter un nouveau soldat !"));
        }
    }

    /**
     * Spawns the first soldier for the player on an empty tile.
     * If an empty city tile is found, a new Soldier object is created and added to the player's list of soldiers.
     * If no empty city tile is available, no soldier is spawned.
     */
    public void spawnFirstSoldier(){
        Soldier soldier = new Soldier(this);
        Tile spawnCoordinates = GameInstance.getInstance().getMap().getAnEmptyTile();
        if (spawnCoordinates != null) {
            soldier.setCoordinates(spawnCoordinates.getCoordinates());
            this.addSoldier(soldier);
        }
    }
    /**
     * Retrieves a Soldier from the list of soldiers based on an input Soldier object.
     *
     * @param soldier The Soldier object to search for.
     * @return The Soldier object found in the list of soldiers that matches the input Soldier, or null if not found.
     */
    public Soldier getSoldierBySoldier(Soldier soldier) {
        return soldiers.stream().filter(s -> s.equals(soldier)).findFirst().orElse(null);
    }

    public Soldier getSoldierById(String id) {
        return soldiers.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public String getLogin() {
        return login;
    }

    public int getScore() {
        return score;
    }

    public int getProductionPoint() {
        return productionPoint;
    }


    public void addScore(int score) {
        this.score += score;
    }


    public void addProductionPoint(int productionPoint) {
        this.productionPoint += productionPoint;
    }

    @Override
    public String toString() {
        return "{" +
                "login:'" + login + '\'' +
                ",\n score:" + score +
                ",\n productionPoint:" + productionPoint +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return score == playerDTO.score && productionPoint == playerDTO.productionPoint && Objects.equals(login, playerDTO.login) && Objects.equals(soldiers, playerDTO.soldiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, score, productionPoint, soldiers);
    }
}
