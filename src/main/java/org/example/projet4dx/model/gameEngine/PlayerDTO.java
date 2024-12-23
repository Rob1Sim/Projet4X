package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.tile.Tile;

import java.util.ArrayList;
import java.util.List;

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
        soldiers.add(new Soldier(this));
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
    public void recruiteSoldier(){
        if (this.productionPoint - 15 >= 0){
            Soldier soldier = new Soldier(this);
            Tile spawnCoordinates = GameInstance.getInstance().getMap().getAnEmptyTile();
            if (spawnCoordinates != null) {
                soldier.setCoordinates(spawnCoordinates.getCoordinates());
                this.addSoldier(soldier);
                GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, this.getLogin()+" a recruté un nouveau soldat !"));
                this.productionPoint -=15;
            }else {
                GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, "Il n'y a plus assez de place pour un nouveau soldat !"));
            }
        }else {
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM, this.getLogin()+" tu n'as pas assez de points de production pour recruter un nouveau soldat !"));
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

    public void setProductionPoint(int productionPoint) {
        this.productionPoint = productionPoint;
    }

    public void addProductionPoint(int productionPoint) {
        this.productionPoint += productionPoint;
    }
}
