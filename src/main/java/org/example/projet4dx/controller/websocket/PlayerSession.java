package org.example.projet4dx.controller.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.Soldier;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.gameEngine.engine.event.GameEventManager;
import org.example.projet4dx.service.PlayerGameService;
import org.example.projet4dx.util.AuthenticationUtil;
import org.example.projet4dx.util.PersistenceManager;

import java.util.List;
import java.util.stream.Collectors;


/**
 * PlayerSession represents a session object for a player in the game. It holds the player's data transfer object, a WebSocket game event listener,
 * and the session object itself.
 */
public class PlayerSession {
    private final PlayerDTO playerDTO;
    private final WebSocketGameEventListener gameEventListener;
    private final Session session;
    private final HttpSession httpSession;

    public PlayerSession(Session session, PlayerDTO playerDTO, HttpSession httpSession) {
        this.session = session;
        this.gameEventListener = new WebSocketGameEventListener(session);
        this.httpSession = httpSession;
        GameEventManager.getInstance().addGameEventListener(this.gameEventListener);
        this.playerDTO = playerDTO;
    }

    /**
     * Persists the player DTO's score in the database for the current player game session.
     *
     * @throws IllegalStateException if an error occurs while saving the user data
     */
    private void persistDTO(){
        PlayerGame game = AuthenticationUtil.getCurrentPlayerGame(this.httpSession);
        if (game != null) {
            PlayerGameService pgs = new PlayerGameService(PersistenceManager.getEntityManager());
            pgs.updatePlayerScore(game.getId(), this.playerDTO.getScore());
        }else {
            throw new IllegalStateException("An error occured while saving the user data's.");
        }
    }

    /**
     * The leaveGame method is responsible for handling the actions needed when a player decides to leave the game.
     * It first persists the player's data transfer object's score into the database for the current player game session.
     * Then, it identifies all soldiers belonging to the player who is leaving and removes them from the current game.
     * Finally, it resets the current player's data transfer object and player game in the session.
     */
    public void leaveGame(){
        persistDTO();
        List<Soldier> soldiersToRemove = GameInstance.getInstance()
                .getAllSoldiers()
                .stream()
                .filter(soldier -> soldier.getPlayerDTO().equals(this.playerDTO))
                .collect(Collectors.toList());
        GameInstance.getInstance().getAllSoldiers().removeAll(soldiersToRemove);


        GameInstance.getInstance().getPlayers().remove(this.playerDTO);

        this.removeGameEventListener();
        AuthenticationUtil.resetCurrentPlayerDTO(this.httpSession);
        AuthenticationUtil.resetCurrentPlayerGame(this.httpSession);
    }

    public PlayerDTO getPlayerDTO() {
        return playerDTO;
    }

    public WebSocketGameEventListener getGameEventListener() {
        return gameEventListener;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Removes the current object's GameEventListener from the list of listeners in the GameEventManager.
     * This method is called to stop receiving game events for the associated object.
     */
    private void removeGameEventListener() {
        GameEventManager.getInstance().removeGameEventListener(this.gameEventListener);
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }
}
