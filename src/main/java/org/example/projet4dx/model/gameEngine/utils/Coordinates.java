package org.example.projet4dx.model.gameEngine.utils;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void up(){
        y++;
    }

    public void down() {
        y--;
    }

    public void left() {
        x--;
    }

    public void right() {
        x++;
    }
}
