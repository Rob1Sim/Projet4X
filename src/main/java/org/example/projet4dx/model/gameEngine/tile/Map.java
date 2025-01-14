package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.utils.Coordinates;

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

    /**
     * Creates a new Map object with dimensions 10x10 and randomly generates Tiles based on the provided width and height.
     * Each Tile is initialized with a random type and image.
     */
    public Map (){
        width = 8;
        height = 8;
        tiles = new ArrayList<>();
        Random rand = new Random();
        GameInstance.addCity();
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
        if (index <= 5) {
            newTile.setType(new CityTile(20));
            newTile.setImage("city.png");
        }else if (index <=15) {
            newTile.setType(new ForestTile());
            newTile.setImage("forest.png");
        }else if(index <=85 ) {
            newTile.setType(new GrassTile());
            newTile.setImage("grass.png");
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
        List<Tile> emptyTiles = new ArrayList<>();
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

    /**
     * Retrieves an empty City Tile owned by the specified player from the map.
     *
     * @param playerDTO The PlayerDTO object representing the player whose empty City Tile is to be retrieved.
     * @return An empty City Tile owned by the specified player, or null if no empty City Tiles are found.
     */
    public Tile getEmptyCity(PlayerDTO playerDTO){
        List<Tile> emptyTiles = new ArrayList<>();
        Random r = new Random();
        for (Tile tile : tiles) {
            if (tile.getType() instanceof CityTile cityTile && cityTile.getPlayer() != null && cityTile.getPlayer().equals(playerDTO) && Soldier.getSoldierByCoordinates(tile.getCoordinates())==null){
                emptyTiles.add(tile);
            }
        }
        if (emptyTiles.isEmpty()){
            return null;
        }
        return emptyTiles.get(r.nextInt(emptyTiles.size()));
    }

    /**
     * Retrieves a list of all City Tiles from the map.
     *
     * @return A List containing all City Tiles present on the map.
     */
    public List<CityTile> getAllCities(){
        List<CityTile> cities = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.getType() instanceof CityTile cityTile){
                cities.add((cityTile));
            }
        }
        return cities;
    }

    /**
     * Retrieves all cities owned by a specific player.
     *
     * @param p The PlayerDTO object representing the player whose cities are to be retrieved.
     * @return A List of CityTile objects owned by the specified player.
     */
    public List<CityTile> getAllCitiesFromAPlayer(PlayerDTO p){
        List<CityTile> cities = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.getType() instanceof CityTile cityTile && cityTile.getPlayer() != null && cityTile.getPlayer().equals(p)){
                cities.add((cityTile));
            }
        }
        return cities;
    }
}
