package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.GameInstance;
import org.example.projet4dx.model.gameEngine.tile.ForestTile;

import java.util.Random;

public class Soldier implements ICombat {
    private Coordinates coordinates;
    private final PlayerDTO playerDTO;
    private int hp;
    private final int MAX_HP = 10;

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
     * Moves the Soldier object in the specified direction.
     *
     * @param soldier the Soldier object to be moved
     * @param direction the direction in which to move the Soldier
     */
    public static void moveSoldier(Soldier soldier, Direction direction) {
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
            System.out.println("Le soldat à bougé vers "+direction);
        }
        System.out.println("Le soldat n'a pas bougé.");
    }

    /**
     * Attacks an entity in combat by inflicting a random amount of damage.
     *
     * @param entity The entity to be attacked in combat.
     * @return true if the entity is vanquished from the attack, false otherwise.
     */
    public boolean attack(ICombat entity){
        Random attackPoint = new Random();
        entity.takeDamage(attackPoint.nextInt(10), this);
        return entity.getHP() <= 0;
    }




    /**
     * Performs a static action for the Soldier object based on the current game state.
     * If the Soldier is on a ForestTile, it will deforest it.
     * Otherwise, if the Soldier's hit points are less than or equal to the maximum hit points,
     * it randomly determines a healing amount between 1 and 3.
     * There is a 50% chance for the healing amount to be set to 1.
     * The Soldier's hit points are then increased by the determined healing amount,
     * capped at the maximum hit points.
     * If it's HP are at their maximum then nothing happen.
     */
    public void staticSoldierAction() {
        if (GameInstance.getInstance().getMap().getTileAtCoord(coordinates).getType() instanceof ForestTile) {
            GameInstance.getInstance().getMap().getTileAtCoord(coordinates).getType().use();
        }else if (hp <= MAX_HP) {
            Random random = new Random();
            int healAmount = random.nextInt(3) + 1;
            int probability = random.nextInt(100);
            if (probability < 50) {
                healAmount = 1;
            }
            hp = Math.min(hp + healAmount, MAX_HP);
        }
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
            GameInstance.getInstance().getPlayerByPlayer(this.getPlayerDTO()).removeSoldier(this);
        }
    }

    @Override
    public int getHP() {
        return hp;
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
}
