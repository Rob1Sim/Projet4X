package org.example.projet4dx.model.gameEngine.engine.event;

/**
 * The GameEvent class represents an event that occurs in a game session.
 */
public class GameEvent {
    private String eventName;
    private String message;

    /**
     * Constructs a GameEvent object with the provided GameEventType and message.
     *
     * @param gameEventType the type of the game event
     * @param message the message describing the game event
     */
    public GameEvent(GameEventType gameEventType, String message) {
        this.eventName = gameEventType.getName();
        this.message = message;
    }

    /**
     * Retrieves the name of the event.
     *
     * @return The name of the event as a String.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     *
     * @param eventName the name of the event to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the message associated with this GameEvent.
     *
     * @return The message of the GameEvent.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message for this GameEvent.
     *
     * @param message the message to be set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
