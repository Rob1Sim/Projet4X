package org.example.projet4dx.model.gameEngine.engine.event;

/**
 * GameEventType enumerates the different types of events that can occur in a game session.
 */
public enum GameEventType {
    MOVEMENT("Mouvement","movement"),
    ACTION("Action","action"),
    SYSTEM("Système","system");

    final String name;
    final String jsonName;
    GameEventType(String name, String jsonName) {
        this.name = name;
        this.jsonName = jsonName;
    }

    public String getName() {
        return name;
    }

    public String getJsonName() {
        return jsonName;
    }
}
