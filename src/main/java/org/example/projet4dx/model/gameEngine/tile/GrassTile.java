package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;

/**
 * Represents a type of tile in the game that is associated with grass.
 * Implements the ITileType interface.
 */
public class GrassTile implements ITileType{

    @Override
    public void use() {
        return;
    }

    @Override
    public boolean canCollide(Tile tile, Soldier soldier) {
        return Soldier.getSoldierByCoordinates(tile.getCoordinates()) == null;
    }
}
