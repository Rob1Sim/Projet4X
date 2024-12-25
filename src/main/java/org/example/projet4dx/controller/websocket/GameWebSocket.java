package org.example.projet4dx.controller.websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.util.AuthenticationUtil;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/gameSocket", configurator = WebSocketConfig.class)
public class GameWebSocket {
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static final Set<PlayerSession> playerSessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        System.out.println("Une sessions est en ouverture");

        PlayerDTO playerDTO = (PlayerDTO) config.getUserProperties().get("player");
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("session");

        if (playerDTO != null && GameInstance.isCreated() && GameInstance.getInstance().getPlayerByPlayer(playerDTO) != null && httpSession != null){
            sessions.add(session);
            PlayerSession playerSession = new PlayerSession(session, playerDTO,httpSession);
            playerSessions.add(playerSession);
            basicBroadcast(textToJsonSystemMessage(playerSession.getPlayerDTO().getLogin()+" à rejoins la partie !"));
            broadcastGameUpdate(session);
        }else {
            System.out.println("La session a été fermé car elle le joueur n'était pas dans une partie !");
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (sessions.contains(session)) {
            PlayerSession currentPlayerSession = getPlayerBySession(session);
            sessions.remove(session);
            currentPlayerSession.leaveGame();
            basicBroadcast(textToJsonSystemMessage(currentPlayerSession.getPlayerDTO().getLogin() + " à quitter la partie !"));
            playerSessions.remove(currentPlayerSession);

            if (sessions.isEmpty()) {
                GameInstance.resetInstance();
            }

        }
    }

    /**
     * Broadcasts a text message to all connected WebSocket sessions.
     *
     * @param text the message to broadcast
     */
    private static void basicBroadcast(String text){
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Broadcasts a game update message to all connected player sessions, providing information about the current game state.
     *
     * @param session the Session object of the player triggering the game update
     */
    private static void broadcastGameUpdate(Session session) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "update");

        JsonObject payload = new JsonObject();
        JsonArray players = new JsonArray();
        playerSessions.forEach(playerSession -> {
            JsonObject player = new JsonObject();
            player.addProperty("login", playerSession.getPlayerDTO().getLogin());
            players.add(player);
        });

        PlayerSession playerSession = getPlayerBySession(session);

        payload.add("players", players);
        payload.addProperty("playerTurn", GameInstance.getInstance().getCurrentPlayer().getLogin());
        payload.addProperty("playerScore", playerSession.getPlayerDTO().getScore());
        payload.addProperty("productionPoints", playerSession.getPlayerDTO().getProductionPoint());
        json.add("payload", payload);

        String jsonString = json.toString();
        basicBroadcast(jsonString);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("Un message est reçue");
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();

            if (!json.has("type") || !json.has("text")) { //Ajouter d'autre validations
                throw new IllegalArgumentException("Message JSON mal formé : " + message);
            }

            String type = json.get("type").getAsString();

            if ("message".equals(type)) {

                PlayerSession playerSession = getPlayerBySession(session);

                String text = json.get("text").getAsString();
                basicBroadcast(textToJsonSystemMessage(playerSession.getPlayerDTO().getLogin()+": "+ text));
            }
            broadcastGameUpdate(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts text message to a JSON string representing a system message for the game.
     *
     * @param text the text message to be converted to JSON
     * @return a JSON string representing the system message with type and message properties
     */
    public static String textToJsonSystemMessage(String text) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", GameEventType.SYSTEM.getJsonName());
        jsonObject.addProperty("message", text);
        return jsonObject.toString();
    }

    /**
     * Retrieves the PlayerSession associated with the provided Session object.
     *
     * @param session the Session object to search for in the playerSessions list
     * @return the PlayerSession object that corresponds to the provided Session
     * @throws IllegalStateException if the player session associated with the provided Session is not found
     */
    private static PlayerSession getPlayerBySession(Session session) {
        return playerSessions.stream()
                .filter(playerSession -> playerSession.getSession().equals(session))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Session joueur introuvable !"));
    }

}
