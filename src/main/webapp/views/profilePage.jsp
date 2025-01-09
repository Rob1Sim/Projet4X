<%@ taglib prefix="c" uri="jakarta.tags.core" %><%--
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
            <c:forEach var="history" items="${historyData}">
                <jsp:include page="historyContainer.jsp">
                    <jsp:param name="date" value="${history.date}" />
                    <jsp:param name="score" value="${history.score}" />
                </jsp:include>
            </c:forEach>
        </div>
    </div>
    <div class="stats-container">
        <h2 class="pp-title">Stats</h2>
        <p>Score moyen: ${meanScore}</p>
        <p>Score Minimum: ${minScore}</p>
        <p>Score Maximum: ${maxScore}</p>
    </div>
</div>
