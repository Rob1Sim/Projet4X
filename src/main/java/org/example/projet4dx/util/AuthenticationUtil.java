package org.example.projet4dx.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGame;

import java.io.IOException;

public class AuthenticationUtil {
    private final static String LOGGED_USER = "LOGGED_USER";
    private final static String CURRENT_PLAYER_GAME = "CURRENT_PLAYER_GAME";
    /**
     * Redirects the user to the authentication page if the user is not logged in.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if the request could not be handled
     * @throws IOException      if an I/O error occurs during the redirection
     */
    public static void redirectToAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute(LOGGED_USER);
        if ( player == null){
            request.getRequestDispatcher("/login").forward(request,response);
            return;
        }
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
     * Removes the current PlayerGame associated with the player from the session.
     *
     * @param request the HttpServletRequest object from which the session is accessed
     */
    public static void removeCurrentPlayerGame(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(CURRENT_PLAYER_GAME);
    }
}
