<%--
  Created by IntelliJ IDEA.
  User: robis
  Date: 24/12/2024
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="game-page">
    <div class="game-panel">
        <div class="game-grid">
            <!-- TODO:Implémenter la grille -->
            <table>

            </table>
        </div>
        <div class="game-control">
            <!-- TODO:Implémenter les actions -->
            <h3>Action</h3>
            <div class="control-btn">
                <div class="control-btn-display">
                    <div class="sn-btn">
                        <button class="g-btn">Nord</button>
                    </div>
                    <div class="ew-btn">
                        <button class="g-btn">Ouest</button>
                        <button class="g-btn">Est</button>
                    </div>
                    <div class="sn-btn">
                        <button class="g-btn">Sud</button>
                    </div>
                </div>
                <div class="action-btn">
                    <button class="g-btn">Soin</button>
                    <button class="g-btn">Déforester</button>
                    <button class="g-btn">Recruter</button>
                </div>
            </div>
        </div>
    </div>
    <div class="game-info">
        <div class="players-info">
            <h3>Joueurs</h3>
            <!-- TODO:Implémenter la listes des joueurs -->
            <ul>
                <li>Gaby</li>
                <li>Manu</li>
                <li>Michel</li>
                <li>François</li>
            </ul>
        </div>
        <div class="player-score-round">
            <div>
                <h3>Tour</h3>
                Manu
            </div>
            <div>
                <h3>Scores</h3>
                <!-- TODO:Implémenter le score -->
                2300
            </div>
            <div>
                <h3>Points de productions</h3>
                <!-- TODO:Implémenter les points de production -->
                2300
            </div>
        </div>
    </div>
    <div class="game-message">
        <h3>Message</h3>
        <div id="messages" style="border: 1px solid #000; height: 30vh; overflow-y: auto; padding: 1vw;">
        </div>
        <div class="textbox-send">
            <input type="text" id="messageInput" placeholder="Enter your message" />
            <button class="g-btn" onclick="sendMessage()">Send</button>
        </div>
    </div>
</div>