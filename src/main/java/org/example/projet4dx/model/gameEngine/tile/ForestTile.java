package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;

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
            player.addProductionPoint(10);
            hasBeenExploited = true;
        }
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
            return true;
        }
        return false;
    }
}
