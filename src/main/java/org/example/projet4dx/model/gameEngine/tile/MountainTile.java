package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;

/**
 * Represents a mountain tile in a game map.
 * Mountain tiles are not interactive and do not allow soldier collisions.
 */
public class MountainTile implements ITileType{

    @Override
    public void use() {
        return;
    }

    @Override
    public boolean canCollide(Tile tile, Soldier soldier) {
        return false;
    }
}
