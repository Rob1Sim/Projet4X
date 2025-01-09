package org.example.projet4dx.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.projet4dx.model.PlayerGameId;
import org.example.projet4dx.model.gameEngine.engine.GameInstance;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.model.PlayerGame;
import org.example.projet4dx.model.gameEngine.PlayerDTO;
import org.example.projet4dx.model.gameEngine.tile.Map;
import org.example.projet4dx.model.gameEngine.utils.Coordinates;
import org.example.projet4dx.service.PlayerGameService;
import org.example.projet4dx.util.AuthenticationUtil;
import org.example.projet4dx.util.DisplayLayoutUtils;
import org.example.projet4dx.util.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GameServlet", value="/game")
public class GameController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthenticationUtil.redirectToAuthentication(request, response)){
            return;
        }

        GameInstance gameInstance = GameInstance.getInstance();


        Player player = AuthenticationUtil.getCurrentPlayer(request);
        PlayerDTO playerDTO;
        PlayerGameService playerGameService = new PlayerGameService(PersistenceManager.getEntityManager());
        PlayerGame pg = playerGameService.getPlayerGame(new PlayerGameId(player.getId(),GameInstance.getInstance().getGame().getId()));
        if (AuthenticationUtil.getCurrentPlayerGame(request) == null ){
            if (pg == null){
                pg = playerGameService.createPlayerGame(player, gameInstance.getGame());
            }

            playerDTO = new PlayerDTO(player);
            gameInstance.addPlayer(playerDTO);

            AuthenticationUtil.saveCurrentPlayerGame(pg,request);
            AuthenticationUtil.saveCurrentPlayerDTO(playerDTO,request);
        }else {
            playerDTO = AuthenticationUtil.getCurrentPlayerDTO(request);
        }
        Map gameMap = gameInstance.getMap();

        List<List<java.util.Map<String, String>>> gridRows = new ArrayList<>();
        for (int y = 0; y < gameMap.getHeight(); y++) {
            List<java.util.Map<String, String>> row = new ArrayList<>();
            for (int x = 0; x < gameMap.getWidth(); x++) {
                String image = gameMap.getTileAtCoord(new Coordinates(x, y)).getImage();
                System.out.println(image);
                row.add(java.util.Map.of("x", String.valueOf(x), "y", String.valueOf(y), "image", image));
            }
            gridRows.add(row);
        }
        System.out.println(gridRows);
        request.setAttribute("gridRows", gridRows);
        request.setAttribute("players", gameInstance.getPlayers());
        request.setAttribute("playerScore", playerDTO.getScore());
        request.setAttribute("productionPoints", playerDTO.getProductionPoint());
        request.setAttribute("playerTurn", gameInstance.getCurrentPlayer().getLogin());
        request.setAttribute("soldiers", gameInstance.getAllSoldiers());
        request.setAttribute("playerSessionLogin", AuthenticationUtil.getCurrentPlayer(request).getLogin());

        DisplayLayoutUtils.displayLayout("Partie","game.jsp",request,response);
    }
}
