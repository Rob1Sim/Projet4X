package org.example.projet4dx.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.projet4dx.model.Player;
import org.example.projet4dx.service.PlayerService;
import org.example.projet4dx.util.AuthenticationUtil;
import org.example.projet4dx.util.DisplayLayoutUtils;
import org.example.projet4dx.util.PersistenceManager;

import java.io.IOException;

@WebServlet(name = "ProfilePageServlet", value = "/profile")
public class ProfilePageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = PersistenceManager.getEntityManager();
        PlayerService playerService = new PlayerService(em);

        if (AuthenticationUtil.redirectToAuthentication(request, response)){
            return;
        }
        Player player = AuthenticationUtil.getCurrentPlayer(request);

        request.setAttribute("meanScore",playerService.getPlayerMeanScore(em,player));
        request.setAttribute("maxScore",playerService.getMaximumPlayerScore(em,player));
        request.setAttribute("minScore",playerService.getMinimumPlayerScore(em,player));
        request.setAttribute("playerGameList",playerService.getPlayerGames(em,player));

        DisplayLayoutUtils.displayLayout("Profile","profilePage.jsp",request,response);


    }
}
