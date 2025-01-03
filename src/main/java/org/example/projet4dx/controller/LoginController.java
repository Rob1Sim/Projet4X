package org.example.projet4dx.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.projet4dx.service.PlayerService;
import org.example.projet4dx.util.DisplayLayoutUtils;
import org.example.projet4dx.util.PersistenceManager;
import org.example.projet4dx.util.StringEscapeUtils;
import org.example.projet4dx.util.exceptions.AuthenticationException;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginController extends HttpServlet {
    String errorMessage = "";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        PlayerService playerService = new PlayerService(PersistenceManager.getEntityManager());

        try {
            if(playerService.loginPlayer(login,password,request)){
                response.sendRedirect("/profile");
            }else{
                errorMessage = "Le login existe déjà ou le mot de passe est incorrecte.";
                doGet(request, response);
            }
            return;
        } catch (AuthenticationException e) {
            errorMessage = "Le login et le mot de passe sont nécéssaire.";
            doGet(request, response);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("errorMessage",errorMessage);
        DisplayLayoutUtils.displayLayout("Login","login.jsp",request,response);

    }
}
