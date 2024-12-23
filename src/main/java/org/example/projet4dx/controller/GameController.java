package org.example.projet4dx.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.service.PlayerGameService;
import org.example.projet4dx.util.AuthenticationUtil;
import org.example.projet4dx.util.DisplayLayoutUtils;
import org.example.projet4dx.util.PersistenceManager;

import java.io.IOException;

@WebServlet(name = "GameServlet", value="/game")
public class GameController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GameInstance gameInstance = GameInstance.getInstance();

        AuthenticationUtil.redirectToAuthentication(request,response);

        Player player = AuthenticationUtil.getCurrentPlayer(request);

        if (AuthenticationUtil.getCurrentPlayerGame(request) == null){
            PlayerGameService playerGameService = new PlayerGameService(PersistenceManager.getEntityManager());
            PlayerGame pg = playerGameService.createPlayerGame(player, gameInstance.getGame());
            //TODO: Enregistré les scores à la fin de la partie/tour

            PlayerDTO playerDTO = new PlayerDTO(player);
            gameInstance.addPlayer(playerDTO);

            AuthenticationUtil.saveCurrentPlayerGame(pg,request);
            //TODO: Penser à set CurrentPlayerGame à null à la fin d'une partie

            request.setAttribute("gameMap", gameInstance.getMap());
            DisplayLayoutUtils.displayLayout("Partie","game.jsp",request,response);
        }
    }
}
