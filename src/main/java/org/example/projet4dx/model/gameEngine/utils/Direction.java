package org.example.projet4dx.model.gameEngine.utils;

/**
 * Represents the cardinal directions used in a game.
 */
public enum Direction {
    EAST("Est"),
    SOUTH("Sud"),
    NORTH("Nord"),
    WEST("Ouest");
    final String nom;
    Direction(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
