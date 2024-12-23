package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.Soldier;

/**
 * Represents a type of tile in the game map.
 */
public interface ITileType {
    void use();
    boolean canCollide(Tile tile, Soldier soldier);
}
