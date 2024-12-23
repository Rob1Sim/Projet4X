package org.example.projet4dx.model.gameEngine.tile;

import org.example.projet4dx.model.gameEngine.Coordinates;
import org.example.projet4dx.model.gameEngine.Soldier;

public class Tile {
    private Coordinates coordinates;
    private ITileType type;
    private String image;

    public Tile(int x, int y, ITileType type, String image) {
        this.coordinates = new Coordinates(x, y);
        this.type = type;
        this.image = image;
    }

    public boolean collide(Soldier soldier) {
        return type.canCollide(this,soldier);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ITileType getType() {
        return type;
    }

    public void setType(ITileType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
