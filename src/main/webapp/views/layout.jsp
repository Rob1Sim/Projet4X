<%@ page import="org.example.projet4dx.model.Player" %><%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 21/12/2024
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="logo">
            <a href="#">4DX</a>
        </div>
        <ul class="nav-links">
            <li><a href="#" class="link-btn">Partie</a></li>
            <li><a href="${pageContext.request.contextPath}/profile"  class="link-btn">Profile</a></li>
            <% Player loggedInUser = (Player) session.getAttribute("loggedInUser");%>
            <li class="greeting">
                <% if (loggedInUser != null) { %>
                Bonjour, ${loggedInUser.getLogin()}
                <% } else { %>
                <a href="${pageContext.request.contextPath}/login" class="link-btn">Connexion/Inscription</a>
                <% } %>
            </li>
        </ul>
        <button class="burger">
            <span class="bar"></span>
            <span class="bar"></span>
            <span class="bar"></span>
        </button>
    </div>
</nav>
    <main><jsp:include page="${content}" /></main>
    <script src="../assets/js/animation.js"></script>
</body>
</html>

