package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.Player;

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
        soldiers.add(new Soldier());
    }

    public void addSoldier(Soldier soldier) {
        this.soldiers.add(soldier);
    }
    public List<Soldier> getSoldiers() {
        return soldiers;
    }
    public void removeSoldier(Soldier soldier) {
        this.soldiers.remove(soldier);
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

    public void setScore(int score) {
        this.score = score;
    }

    public void setProductionPoint(int productionPoint) {
        this.productionPoint = productionPoint;
    }
}
