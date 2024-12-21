<%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 21/12/2024
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="profile-page-container">
    <div class="history-container">
        <h2 class="pp-title">Historique</h2>
        <div class="history-multiple-container">
            <!--TODO: Remplacer par un for each Ga-->
            <% for (int i = 0; i<10; i++){%>
            <jsp:include page="historyContainer.jsp" />
            <%}%>
        </div>

    </div>
    <div class="stats-container">
        <h2 class="pp-title">Stats</h2>
        <p>Score moyen: ${meanScore}</p>
        <p>Score Minimum: ${minScore}</p>
        <p>Score Maximum: ${maxScore}</p>
    </div>
</div>
