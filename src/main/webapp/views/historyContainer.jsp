<%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 21/12/2024
  Time: 18:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="history-single-container">
    <!--TODO: Remplacer par les données-->
    <%
        String date = request.getParameter("date");
        String score = request.getParameter("score");
        if (date == null) {
            date = "Pas de date";
        }
        if (score == null) {
            score = "0";
        }
    %>
    <p class="history-date"><%=date%></p>
    <p>Score: <%=score%></p>
</div>
