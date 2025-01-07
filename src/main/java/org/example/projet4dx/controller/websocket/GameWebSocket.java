package org.example.projet4dx.controller.websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventType;
import org.example.projet4dx.model.gameEngine.utils.Direction;
import org.example.projet4dx.util.StringifyUtil;

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
            broadcastPlayersInfoUpdate();
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
            broadcastPlayersInfoUpdate();
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

        PlayerSession playerSession = getPlayerBySession(session);
        payload.addProperty("playerTurn", GameInstance.getInstance().getCurrentPlayer().getLogin());
        payload.addProperty("playerScore", playerSession.getPlayerDTO().getScore());
        payload.addProperty("productionPoints", playerSession.getPlayerDTO().getProductionPoint());
        payload.add("soldiers", StringifyUtil.jsonifySoldierList(GameInstance.getInstance().getAllSoldiers()));
        json.add("payload", payload);

        String jsonString = json.toString();
        basicBroadcast(jsonString);
    }

    /**
     * Broadcasts an update message containing players' information to all connected WebSocket sessions.
     * The method iterates through the playerSessions list and constructs a JSON payload that includes the login name of each player.
     * This information is then broadcasted to all WebSocket sessions using the basicBroadcast method.
     */
    public static void broadcastPlayersInfoUpdate(){
        JsonObject json = new JsonObject();
        JsonArray players = new JsonArray();
        json.addProperty("type", "update");
        JsonObject payload = new JsonObject();
        playerSessions.forEach(playerSession -> {
            JsonObject player = new JsonObject();
            player.addProperty("login", playerSession.getPlayerDTO().getLogin());
            players.add(player);
        });
        payload.add("players", players);
        json.add("payload", payload);
        String jsonString = json.toString();
        basicBroadcast(jsonString);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("Un message est reçue");
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();

            if (!json.has("type")) {
                throw new IllegalArgumentException("Message JSON mal formé : " + message);
            }

            String type = json.get("type").getAsString();
            PlayerSession playerSession = getPlayerBySession(session);

            if ("message".equals(type)) {


                String text = json.get("text").getAsString();
                basicBroadcast(textToJsonSystemMessage(playerSession.getPlayerDTO().getLogin()+": "+ text));
            }

            if ("moveSoldier".equals(type)) {
                String soldierId = json.get("soldierId").getAsString();
                String direction = json.get("direction").getAsString();
                Soldier soldier = playerSession.getPlayerDTO().getSoldierById(soldierId);

                Direction directionEnum = Direction.getDirectionByName(direction);
                if (directionEnum == null) {
                    throw new IllegalArgumentException("Direction not recognized: " + direction);
                }
                GameInstance.getInstance().moveSoldier(soldier, directionEnum);
            }

            if("endTurn".equals(type)){
                GameInstance.getInstance().nextTurn();
            }
            broadcastGameUpdate(session);
            broadcastPlayersInfoUpdate();
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
