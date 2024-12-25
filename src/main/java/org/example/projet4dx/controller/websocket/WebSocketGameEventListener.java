package org.example.projet4dx.controller.websocket;

import jakarta.websocket.Session;
import org.example.projet4dx.model.gameEngine.engine.event.GameEvent;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventListener;

import static org.example.projet4dx.controller.websocket.GameWebSocket.textToJsonSystemMessage;

/**
 * WebSocketGameEventListener is a class that implements the GameEventListener interface to handle game events
 * by converting them to JSON format and broadcasting them to all WebSocket sessions.
 */
public class WebSocketGameEventListener implements GameEventListener {
    Session session;
    public WebSocketGameEventListener(Session session) {
        this.session = session;
    }

    /**
     * Handles a game event by converting it to JSON format and broadcasting it to all WebSocket sessions.
     *
     * @param event the GameEvent object representing the game event that occurred
     */
    @Override
    public void onGameEvent(GameEvent event) {
        try {
            String json = convertEventToJson(event);
            session.getBasicRemote().sendText(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a GameEvent object to a JSON string representing a system event.
     *
     * @param event the GameEvent object to be converted
     * @return a JSON string representing the system event
     */
    private String convertEventToJson(GameEvent event) {
        return textToJsonSystemMessage(event.toString());
    }

    public Session getSession() {
        return session;
    }
}
