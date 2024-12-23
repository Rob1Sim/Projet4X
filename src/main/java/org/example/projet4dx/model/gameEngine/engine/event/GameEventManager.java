package org.example.projet4dx.model.gameEngine.engine.event;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameEventManager class represents a manager for game events in the application.
 * It allows adding GameEventListeners to receive game events and notifying them of game event occurrences.
 */
public class GameEventManager {
    private final List<GameEventListener> listeners;
    private static GameEventManager instance;

    /**
     * This class represents a manager for game events in the application.
     */
    private GameEventManager() {
        listeners = new ArrayList<>();
    }

    /**
     * This method returns the singleton instance of the GameEventManager class.
     * If the instance has not been created yet, a new instance is created and returned.
     *
     * @return The singleton instance of the GameEventManager class
     */
    public static synchronized GameEventManager getInstance() {
        if (instance == null) {
            instance = new GameEventManager();
        }
        return instance;
    }

    /**
     * Adds a GameEventListener to receive game events.
     *
     * @param listener the GameEventListener to be added
     */
    public void addGameEventListener(GameEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered GameEventListeners of a game event occurrence.
     *
     * @param event the GameEvent object representing the game event that occurred
     */
    public void notifyGameEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onGameEvent(event);
        }
    }
}
