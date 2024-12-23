package org.example.projet4dx.model.gameEngine.engine.event;

/**
 * The GameEventListener interface represents a listener for game events.
 */
public interface GameEventListener {
    /**
     * This method is called when a game event occurs. Implementations of this method should handle the specified game event.
     *
     * @param event the GameEvent object representing the game event that occurred
     */
    void onGameEvent( GameEvent event );
}
