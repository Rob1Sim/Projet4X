package org.example.projet4dx.model.gameEngine;

import org.example.projet4dx.model.gameEngine.tile.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a game Map composed of Tiles.
 */
public class Map {
    private final List<Tile> tiles;
    private final int width;
    private final int height;

    public List<Tile> getTiles() {
        return tiles;
    }

    public Map (){
        width = 10;
        height = 10;
        tiles = new ArrayList<>();
        Random rand = new Random();
        for ( int x = 0; x < width; x++ ) {
            for ( int y = 0; y < height; y++ ) {
                tiles.add(getRandomTileType(x, y, rand));
            }
        }
    }

    /**
     *
     * Generates a random Tile based on the given x and y coordinates and a Random object.
     *
     * @param x The x-coordinate of the Tile.
     * @param y The y-coordinate of the Tile.
     * @param rand The Random object used to generate random integers.
     * @return A Tile object with a randomly chosen type and image based on the generated random index value.
     */
    private Tile getRandomTileType(int x, int y, Random rand) {
        Tile newTile = new Tile(x,y,null,"");
        int index = rand.nextInt(100);
        if (index <= 15) {
            newTile.setType(new CityTile(20));
            newTile.setImage("city.png");
        }else if (index <=35) {
            newTile.setType(new ForestTile());
            newTile.setImage("forest.png");
        }else if(index <=85 ) {
            newTile.setType(new GrassTile());
            newTile.setImage("");
        }else{
            newTile.setType(new MountainTile());
            newTile.setImage("mountain.png");
        }
        return newTile;
    }

    /**
     * Retrieves the Tile at the specified Coordinates.
     *
     * @param coordinates The Coordinates object representing the x and y values to search for.
     * @return The Tile object found at the specified Coordinates, or null if no Tile is found.
     */
    public Tile getTileAtCoord(Coordinates coordinates){
        for (Tile tile : tiles) {
            if (tile.getCoordinates().getX() == coordinates.getX() && tile.getCoordinates().getY() == coordinates.getY())
                return tile;
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Retrieves an empty Tile from the list of tiles. An empty Tile is defined as a Tile with a type of GrassTile and no Soldier on its coordinates.
     *
     * @return An empty Tile from the list, or null if no empty Tiles are found.
     */
    public Tile getAnEmptyTile(){
        List<Tile> emptyTiles = new ArrayList<Tile>();
        for (Tile tile : tiles) {
            if (tile.getType() instanceof GrassTile && Soldier.getSoldierByCoordinates(tile.getCoordinates()) == null) {
                emptyTiles.add(tile);
            }
        }
        if (emptyTiles.isEmpty())
            return null;
        Random r = new Random();
        return emptyTiles.get(r.nextInt(emptyTiles.size()));
    }
}
