package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;

/**
 * Represents a type of tile on the game map that is associated with a forest.
 * When a player's soldier collides with this type of tile, the player gains production points.
 */
public class ForestTile implements ITileType{
    private boolean hasBeenExploited = false;
    private PlayerDTO player;

    /**
     * Increases the production points of the associated player by 10 if the player is not null and the tile has not been exploited before.
     */
    @Override
    public void use() {
        if (player != null && !hasBeenExploited) {
            int productionPoint = 10;
            player.addProductionPoint(productionPoint);
            hasBeenExploited = true;
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.ACTION,player.getLogin()+" récupère "+productionPoint+", mais la forêt n'est plus utilisable !"));
        }
        GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM,"Cette forêt a déjà été défriché, tu ne peux plus l'utiliser !"));
    }

    /**
     * Determines if a Soldier can collide with a specific Tile on the game map.
     *
     * @param tile the Tile to check collision with
     * @param soldier the Soldier trying to collide with the Tile
     * @return true if the Soldier can collide with the Tile, false otherwise
     */
    @Override
    public boolean canCollide(Tile tile, Soldier soldier) {
        if (Soldier.getSoldierByCoordinates(tile.getCoordinates()) == null) {
            player = soldier.getPlayerDTO();
            int scoreWin = 25;
            player.addScore(scoreWin);
            GameEventManager.getInstance().notifyGameEvent(new GameEvent(GameEventType.SYSTEM,"Une forêt est réccupérée par "+player.getLogin()+" ! Il gagne "+scoreWin+" points."));

            return true;
        }
        return false;
    }
}
