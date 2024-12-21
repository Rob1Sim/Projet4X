<%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 21/12/2024
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="login-container-container">
    <div class="login-container">
        <h2>Connexion/Inscription</h2>
        <form action="login" method="post">
            <div class="form-group">
                <label for="login">Login :</label>
                <input type="text" id="login" name="login" required>
            </div>
            <div class="form-group">
                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <button type="submit">Se connecter/S'inscrire</button>
            </div>
            <p class="errorMessage">${errorMessage}</p>
        </form>
    </div>

</div>
