package org.example.projet4dx.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Utility class for displaying the layout of the website.
 */
public class DisplayLayoutUtils {
    /**
     * Manage the display of the layout view
     *
     * @param pageTitle the title of the page
     * @param pageJSP the JSP content to be displayed
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if the request could not be handled
     * @throws IOException if an I/O error occurs during the forwarding process
     */
    public static void displayLayout(String pageTitle, String pageJSP, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("content",pageJSP);
        request.setAttribute("currentPlayer",AuthenticationUtil.getCurrentPlayer(request));
        request.getRequestDispatcher("/views/layout.jsp").forward(request, response);
    }
}
