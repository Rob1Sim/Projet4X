package org.example.projet4dx.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.gameEngine.PlayerDTO;

import java.io.IOException;

public class AuthenticationUtil {
    public final static String LOGGED_USER = "LOGGED_USER";
    public final static String CURRENT_PLAYER_GAME = "CURRENT_PLAYER_GAME";
    public final static String CURRENT_PLAYER_DTO = "CURRENT_PLAYER_DTO";

    /**
     * Redirects the user to the authentication page if the player is not logged in.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @return true if the player is not logged in and redirection is performed, false otherwise
     * @throws ServletException if there is an issue with the servlet
     * @throws IOException      if there is an issue with input/output
     */
    public static boolean redirectToAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute(LOGGED_USER);
        if ( player == null){
            request.getRequestDispatcher("/login").forward(request,response);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the current logged-in player based on the session attribute "loggedInUser".
     *
     * @param request the {@link HttpServletRequest} object containing the session
     * @return the current player (null if not logged in)
     */
    public static Player getCurrentPlayer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Player) session.getAttribute(LOGGED_USER);
    }

    public static Player getCurrentPlayer(HttpSession session) {
        return (Player) session.getAttribute(LOGGED_USER);
    }

    /**
     * Saves the current player in the session.
     *
     * @param player the Player object to be saved as the current player
     * @param request the HttpServletRequest object used to access the session
     */
    public static void saveCurrentPlayer(Player player, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED_USER, player);
    }

    /**
     * Retrieves the current PlayerGame associated with the player from the session.
     *
     * @param request the HttpServletRequest object from which the session is accessed
     * @return the current PlayerGame (null if none set)
     */
    public static PlayerGame getCurrentPlayerGame(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (PlayerGame) session.getAttribute(CURRENT_PLAYER_GAME);
    }

    public static PlayerGame getCurrentPlayerGame(HttpSession session) {
        return (PlayerGame) session.getAttribute(CURRENT_PLAYER_GAME);
    }

    /**
     * Retrieves the current PlayerGame DTO associated with the player from the session.
     *
     * @param request the HttpServletRequest object from which the session is accessed
     * @return the current Player DTO (null if none set)
     */
    public static PlayerDTO getCurrentPlayerDTO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (PlayerDTO) session.getAttribute(CURRENT_PLAYER_DTO);
    }

    public static PlayerDTO getCurrentPlayerDTO(HttpSession session) {
        return (PlayerDTO) session.getAttribute(CURRENT_PLAYER_DTO);
    }

    /**
     * Saves the current player's game in the session.
     *
     * @param game the PlayerGame object to be saved as the current player's game
     * @param request the HttpServletRequest object used to access the session
     */
    public static void saveCurrentPlayerGame(PlayerGame game, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PLAYER_GAME, game);
    }

    /**
     * Resets the current player's game information stored in the HttpSession.
     *
     * @param session the HttpSession object from which to remove the current player's game information
     */
    public static void resetCurrentPlayerGame(HttpSession session) {
        session.removeAttribute(CURRENT_PLAYER_GAME);
    }

    /**
     * Resets the current PlayerDTO in the HttpSession by removing the attribute with the key CURRENT_PLAYER_DTO.
     *
     * @param session the HttpSession from which to remove the current PlayerDTO
     */
    public static void resetCurrentPlayerDTO(HttpSession session) {
        session.removeAttribute(CURRENT_PLAYER_DTO);
    }

    /**
     * Saves the current player DTO in the session.
     *
     * @param playerDTO the PlayerDTO object to be saved as the current player DTO
     * @param request the HttpServletRequest object used to access the session
     */
    public static void saveCurrentPlayerDTO(PlayerDTO playerDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PLAYER_DTO, playerDTO);
    }

    /**
     * Removes the current PlayerDTO from the provided HttpSession.
     *
     * @param session the HttpSession from which to remove the current PlayerDTO
     */
    public static void removeCurrentPlayerDTO(HttpSession session) {
        session.removeAttribute(CURRENT_PLAYER_DTO);
    }

    /**
     * Removes the current PlayerGame associated with the player from the session.
     *
     * @param request the HttpServletRequest object from which the session is accessed
     */
    public static void removeCurrentPlayerGame(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(CURRENT_PLAYER_GAME);
    }

    public static void removeCurrentPlayerGame(HttpSession session) {
        session.removeAttribute(CURRENT_PLAYER_GAME);
    }
}
