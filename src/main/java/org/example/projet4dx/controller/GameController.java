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
        if (AuthenticationUtil.redirectToAuthentication(request, response)){
            return;
        }

        GameInstance gameInstance = GameInstance.getInstance();


        Player player = AuthenticationUtil.getCurrentPlayer(request);
        PlayerGame pg;
        PlayerDTO playerDTO;

        if (AuthenticationUtil.getCurrentPlayerGame(request) == null){
            PlayerGameService playerGameService = new PlayerGameService(PersistenceManager.getEntityManager());
            pg = playerGameService.createPlayerGame(player, gameInstance.getGame());

            playerDTO = new PlayerDTO(player);
            gameInstance.addPlayer(playerDTO);

            AuthenticationUtil.saveCurrentPlayerGame(pg,request);
            AuthenticationUtil.saveCurrentPlayerDTO(playerDTO,request);
        }else {
            playerDTO = AuthenticationUtil.getCurrentPlayerDTO(request);
        }
        request.setAttribute("gameMap", gameInstance.getMap());
        request.setAttribute("players", gameInstance.getPlayers());
        request.setAttribute("playerScore", playerDTO.getScore());
        request.setAttribute("productionPoints", playerDTO.getProductionPoint());
        request.setAttribute("playerTurn", gameInstance.getCurrentPlayer().getLogin());
        request.setAttribute("soldiers", gameInstance.getAllSoldiers());

        DisplayLayoutUtils.displayLayout("Partie","game.jsp",request,response);
    }
}
