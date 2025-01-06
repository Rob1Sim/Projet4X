package org.example.projet4dx.model.gameEngine.utils;

/**
 * Represents the cardinal directions used in a game.
 */
public enum Direction {
    EAST("east"),
    SOUTH("south"),
    NORTH("north"),
    WEST("west");
    final String nom;
    Direction(String nom) {
        this.nom = nom;
    }

    public static Direction getDirectionByName(String name) {
        switch (name.toLowerCase()) {
            case "east":
                return EAST;
            case "west":
                return WEST;
            case "south":
                return SOUTH;
            case "north":
                return NORTH;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return nom;
    }
}
