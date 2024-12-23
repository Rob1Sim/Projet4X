package org.example.projet4dx.model.gameEngine.engine.event;

/**
 * GameEventType enumerates the different types of events that can occur in a game session.
 */
public enum GameEventType {
    MOVEMENT("Mouvement"),
    ACTION("Action"),
    SYSTEM("Système");

    final String name;
    GameEventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
