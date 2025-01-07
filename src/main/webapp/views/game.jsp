<%@ page import="org.example.projet4dx.model.gameEngine.tile.Map" %>
<%@ page import="org.example.projet4dx.model.gameEngine.utils.Coordinates" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.projet4dx.model.gameEngine.PlayerDTO" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<div class="game-page">
    <div class="game-panel">
        <div class="game-grid">
            <table id="table">
            <% Map map = (Map)request.getAttribute("gameMap");
                for (int y = 0; y < map.getHeight(); y++){
                    %>
                <tr id="l-<%=y%>">
            <%
                for (int x = 0; x < map.getWidth(); x++){
                      %>
                    <td id="<%=x%>-<%=y%>" class="case">
                        <img class="background_img case" src="${pageContext.request.contextPath}/assets/images/icons/Small/<%=map.getTileAtCoord(new Coordinates(x,y)).getImage()%>" alt="icone">
                    </td>
                    <%
                }
                }
            %>
                </tr>
            </table>
        </div>
        <div class="game-control">
            <!-- TODO:Implémenter les actions -->
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
            <!-- TODO:Implémenter la listes des joueurs -->
            <ul>
            <% List<PlayerDTO> players = (List<PlayerDTO>) request.getAttribute("players");
                if (players != null){
                    for(PlayerDTO player : players){%>
                        <li><%=player.getLogin()%></li><%
                    }
                }
                %>
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
        <div id="messages" style="border: 1px solid #000; height: 30vh; overflow-y: auto; padding: 1vw;">
        </div>
        <div class="textbox-send">
            <input type="text" id="messageInput" placeholder="Enter your message" />
            <button class="g-btn" onclick="sendMessage()">Send</button>
        </div>
    </div>
</div>
<script>
    const contextPath = "${pageContext.request.contextPath}";
    const playerSession = "${playerSessionLogin}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/gameSocket.js"></script>

