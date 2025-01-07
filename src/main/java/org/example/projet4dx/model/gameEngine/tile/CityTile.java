package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.ICombat;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;

/**
 * Represents a City Tile within the game.
 * This class implements ITileType and ICombat interfaces.
 */
public class CityTile implements ITileType, ICombat {
    /**
     *  Represents the city's HPs.
     */
    private int defendPoint;
    /**
     *
     * Represents whether the CityTile is currently taken by a player.
     */
    private boolean isTaken;
    /**
     * Represents a PlayerDTO object containing data transfer information for a player in a game session.
     */
    private PlayerDTO player;

    /**
     * Constructs a CityTile with the specified defense points.
     *
     * @param defendPoint The defense points of the CityTile.
     */
    public CityTile(int defendPoint) {
        this.defendPoint = defendPoint;
        this.isTaken = false;
    }


    /**
     * Increases the player's production points by 5 if the player associated with the tile is not null.
     * This method is intended to be called when a player interacts with a city tile.
     */
    @Override
    public void use() {
        int productionPoint = 5;
        if (player != null && isTaken) {
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,player.getLogin()+" récupère "+productionPoint+" points de production !"));
            player.addProductionPoint(productionPoint);
        }
    }

    /**
     * Determines if a Soldier can collide with a Tile based on certain conditions.
     *
     * @param tile The Tile object the Soldier is attempting to collide with.
     * @param soldier The Soldier object that is attempting to collide with the Tile.
     * @return true if the Soldier can collide with the Tile based on specific rules, false otherwise.
     */
    @Override
    public boolean canCollide(Tile tile, Soldier soldier) {
        if (Soldier.getSoldierByCoordinates(tile.getCoordinates()) == null
                && !isTaken() && soldier.attack(this)) {//Sois la ville n'est pas prise donc tu peux l'attaquer pour la prendre
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION, player.getLogin() + " prends une ville !"));
            return true;
        }
        //Sois la ville est prise par ton joueur donc tu peux te ballader dessus, sinon tu ne peux pas
        return isTaken() && player == soldier.getPlayerDTO();

    }

    /**
     * Retrieves the defense points of the City Tile.
     *
     * @return The defense points of the City Tile.
     */
    public int getDefendPoint() {
        return defendPoint;
    }

    /**
     * Sets the defense point value for the CityTile.
     *
     * @param defendPoint The defense point value to be set for the CityTile.
     */
    public void setDefendPoint(int defendPoint) {
        this.defendPoint = defendPoint;
    }

    /**
     * Checks if the CityTile is taken by a player.
     *
     * @return true if the CityTile is taken, false otherwise.
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Reduces the defend points of the CityTile by the specified damage amount.
     *
     * @param damage The amount of damage to apply to the CityTile's defend points.
     * @param soldier The Soldier inflicting the damage.
     */
    @Override
    public void takeDamage(int damage, Soldier soldier) {
        defendPoint -= damage;
        PlayerDTO attackerPlayer = soldier.getPlayerDTO();
        if (defendPoint <= 0) {
            player = attackerPlayer;
            isTaken = true;

            int scoreWin = 100;
            player.addScore(scoreWin);

            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,"La cité est tombée aux mains de "+player.getLogin()+" ! Il gagne "+scoreWin+" points."));


        }else
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,attackerPlayer.getLogin()+" attaque  "+this.getName()+" ! Mais il lui reste encore "+defendPoint+" points de défense!"));
    }

    @Override
    public int getHP() {
        return defendPoint;
    }

    @Override
    public String getName() {
        return "une ville";
    }

    public PlayerDTO getPlayer() {
        return player;
    }
}
