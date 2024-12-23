package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.utils.Coordinates;

import java.util.Random;

/**
 * Represents a soldier entity in the game that implements combat capabilities.
 */
public class Soldier implements ICombat {
    private Coordinates coordinates;
    private final PlayerDTO playerDTO;
    private int hp;
    public static final int MAX_HP = 10;

    public Soldier(PlayerDTO playerDTO) {
        coordinates = new Coordinates();
        this.playerDTO = playerDTO;
        hp = MAX_HP;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Attacks an entity in combat by inflicting a random amount of damage.
     *
     * @param entity The entity to be attacked in combat.
     * @return true if the entity is vanquished from the attack, false otherwise.
     */
    public boolean attack(ICombat entity){
        Random attackPoint = new Random();
        int damage = attackPoint.nextInt(10);
        entity.takeDamage(damage, this);
        GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,this.getPlayerDTO().getLogin()+" attaque "+entity.getName()+" et inflige "+damage+" points de dégats !"));
        return entity.getHP() <= 0;
    }


    public PlayerDTO getPlayerDTO() {
        return playerDTO;
    }

    /**
     * Reduces the hit points of the Soldier by the specified damage amount.
     *
     * @param damage the amount of damage to apply to the Soldier's hit points
     * @param soldier the Soldier inflicting the damage
     */
    @Override
    public void takeDamage(int damage, Soldier soldier) {
        hp = hp - damage;
        if (hp <= 0) {
            int scoreWin = 50;
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,"Le soldat de "+this.getPlayerDTO().getLogin()+" est mort !"+soldier.getPlayerDTO().getLogin()+" gagne "+scoreWin+" points"));
            GameInstance.getInstance().getPlayerByPlayer(this.getPlayerDTO()).removeSoldier(this);
            soldier.getPlayerDTO().addScore(scoreWin);
        }else
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,"Il ne rest que "+hp+"au soldat de "+this.getPlayerDTO().getLogin()+" !"));
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public String getName() {
        return "un soldat";
    }

    /**
     * Retrieves a Soldier object based on the provided Coordinates.
     *
     * @param coordinates The coordinates to search for a Soldier.
     * @return The Soldier object found at the specified coordinates, or null if no Soldier is found.
     */
    public static Soldier getSoldierByCoordinates(Coordinates coordinates) {
        return GameInstance.getInstance().getAllSoldiers().stream()
                .filter(soldier -> soldier.getCoordinates().getX() == coordinates.getX() && soldier.getCoordinates().getY() == coordinates.getY())
                .findFirst().orElse(null);
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
