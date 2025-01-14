<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<%@ page contentType="text/html;charset=UTF-8" %>
<div class="game-page" id="game-page">
    <div class="game-panel">
        <div class="game-grid">
            <table id="table">
                <c:forEach var="row" items="${gridRows}">
                    <tr>
                        <c:forEach var="tile" items="${row}">
                            <td id="${tile.x}-${tile.y}" class="case">
                                <img class="background_img case" src="${pageContext.request.contextPath}/assets/images/icons/Small/${tile.image}" alt="icone">
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="game-control">
            <h3>Action</h3>
            <div class="control-btn">
                <div class="control-btn-display">
                    <div class="sn-btn">
                        <button id="north-btn" class="g-btn">Nord</button>
                    </div>
                    <div class="ew-btn">
                        <button id="west-btn" class="g-btn">Ouest</button>
                        <button id="east-btn" class="g-btn">Est</button>
                    </div>
                    <div class="sn-btn">
                        <button id="south-btn" class="g-btn">Sud</button>
                    </div>
                </div>
                <div class="action-btn">
                    <button id="heal-btn" class="g-btn">Soin</button>
                    <button id="deforest-btn" class="g-btn">Déforester</button>
                    <button id="recruit-btn" class="g-btn">Recruter</button>
                    <button id="end-turn-btn" class="g-btn">Fin de tour</button>

                </div>
                <div id="soldier-info" class="soldier-info">
                    <h4>Soldat:</h4>
                    <div id="health-bar-container" style="width: 200px; height: 20px; border: 1px solid #000;">
                        <div id="health-bar" style="width: 100%; height: 100%; background-color: green;"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <div class="game-info">
        <div class="players-info">
            <h3>Joueurs</h3>
            <ul>
                <c:forEach var="player" items="${players}">
                    <li>${player.login}</li>
                </c:forEach>
            </ul>
        </div>
        <div class="player-score-round">
            <div>
                <h3>Tour</h3>
                <p id="playerTurn">${playerTurn}</p>
            </div>
            <div>
                <h3>Scores</h3>
                <p id="playerScore">${playerScore}</p>
            </div>
            <div>
                <h3>Points de productions</h3>
                <p id="playerProductionPoint">${productionPoints}</p>
            </div>
        </div>
    </div>
    <div class="game-message">
        <h3>Message</h3>
        <div class="message-box" id="messages" >
        </div>
        <div class="textbox-send">
            <input class="message-send-box" type="text" id="messageInput" placeholder="Enter your message" />
            <button class="send-btn" onclick="sendMessage()">Send</button>
        </div>
    </div>
</div>
<div class="waiting-screen display-none" id="waiting-screen">
    <h1>En attente d'autres joueurs ...</h1>
</div>
<div class="waiting-screen display-none" id="victory-screen">
    <h1></h1>
</div>
<script>
    const serverIp = window.location.hostname;
    const contextPath = "${pageContext.request.contextPath}";
    const playerSession = "${playerSessionLogin}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/gameSocket.js"></script>

