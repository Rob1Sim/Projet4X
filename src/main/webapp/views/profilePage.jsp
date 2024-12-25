<%@ page import="org.example.projet4dx.model.PlayerGame" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 21/12/2024
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="profile-page-container">
    <div class="history-container">
        <h2 class="pp-title">Historique</h2>
        <div class="history-multiple-container">
            <!--TODO: Remplacer par un for each Ga-->
            <%
                List<PlayerGame> playerGameList = (List<PlayerGame>) request.getAttribute("playerGames");
                if (playerGameList != null) {

                    for (int i =playerGameList.size()-1 ; i>=0; i--){
                        PlayerGame pg = playerGameList.get(i);
                        String date = pg.getGame().getDate().toString();
                        String score = String.valueOf(pg.getScore());
            %>
            <jsp:include page="historyContainer.jsp" >
                <jsp:param name="date" value="<%= date %>" />
                <jsp:param name="score" value="<%= score %>" />
            </jsp:include>
            <%}}%>
        </div>

    </div>
    <div class="stats-container">
        <h2 class="pp-title">Stats</h2>
        <p>Score moyen: ${meanScore}</p>
        <p>Score Minimum: ${minScore}</p>
        <p>Score Maximum: ${maxScore}</p>
    </div>
</div>
