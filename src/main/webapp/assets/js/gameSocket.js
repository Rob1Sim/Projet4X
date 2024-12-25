let socket;
let retries = 0;

window.onload = () => {
    connectWebSocket();
};

function connectWebSocket() {
    try{
        socket = new WebSocket("ws://localhost:8080/gameSocket");

        socket.onopen = () => {
            console.log("WebSocket connecté !");
            retries = 0;
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log("Un message est reçue "+event.data.message);
            console.log(data);
            if (data.type === "update") {
                updateGameInfo(data.payload);
            } else if (data.type === "system") {
                addMessageToChat(data.message);
            }
        };

        socket.onclose = () => {
            console.log("WebSocket déconnecté !");
        };

        socket.onerror = (error) => {
            console.error("WebSocket Error: ", error);
            if (retries < 5) {
                retries++;
                console.log("Tentative de reconnexion dans 2 secondes...");
                setTimeout(connectWebSocket, 2000);
            } else {
                console.error("Impossible de se connecter après plusieurs tentatives.");
            }
        };
    }catch (e){
        console.error("WebSocket Error: ", e);
    }

}

function sendMessage() {
    const messageInput = document.getElementById("messageInput");
    const message = messageInput.value;
    if (message.trim() !== "") {
        const payload = {
            type: "message",
            text: message
        };
        socket.send(JSON.stringify(payload));
        messageInput.value = "";
    }
}

function updateGameInfo(payload) {
    if (payload.players) {
        const playerList = document.querySelector(".players-info ul");
        playerList.innerHTML = "";
        payload.players.forEach(player => {
            const li = document.createElement("li");
            li.textContent = player.login;
            playerList.appendChild(li);
        });
    }
    if (payload.playerTurn !== undefined) {
        document.getElementById("playerTurn").textContent = payload.playerTurn;
    }
    if (payload.playerScore !== undefined) {
        document.getElementById("playerScore").textContent = payload.playerScore;
    }
    if (payload.productionPoints !== undefined) {
        document.getElementById("playerProductionPoint").textContent = payload.productionPoints;
    }
    if (payload.soldiers !== undefined) {
        //TODO: Mettre à jour la position des soldats
    }
}

function addMessageToChat(message) {
    const messagesDiv = document.getElementById("messages");
    const p = document.createElement("p");
    console.log("Un message devrait être créer ?")
    p.textContent = message;
    messagesDiv.appendChild(p);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
