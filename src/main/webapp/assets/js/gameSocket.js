let socket;
let retries = 0;
let selectedCell;
let selectedSoldier;
let soldierMovedList= [];
let playerTurn;

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

/**
 * Updates the game information based on the provided payload.
 *
 * @param {Object} payload - The payload containing information to update the game.
 * @param {Array} payload.players - An array of player objects.
 * @param {string} payload.playerTurn - The player whose turn it is.
 * @param {number} payload.playerScore - The score of the player.
 * @param {number} payload.productionPoints - The production points of the player.
 * @param {Array} payload.soldiers - An array of soldier information.
 * @param {string} payload.playerSession - The session information of the player.
 *
 * @return {void}
 */
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
        playerTurn = payload.playerTurn;
    }
    if (payload.playerScore !== undefined) {
        document.getElementById("playerScore").textContent = payload.playerScore;
    }
    if (payload.productionPoints !== undefined) {
        document.getElementById("playerProductionPoint").textContent = payload.productionPoints;
    }
    if (payload.soldiers !== undefined) {
        displaySoldiers(payload.soldiers)
        console.log(payload.soldiers);
    }
}

function addMessageToChat(message) {
    const messagesDiv = document.getElementById("messages");
    const p = document.createElement("p");
    p.textContent = message;
    messagesDiv.appendChild(p);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

/**
 * Display soldiers on the game board.
 *
 * @param {Array} soldiers - Array of soldier objects to be displayed.
 *
 * @return {void}
 */
function displaySoldiers(soldiers){
    let i = 0;
    selectedSoldier = null;
    if (selectedCell !== undefined && selectedCell !== null) {
        selectedCell.classList.remove("selectedCell");
    }
    selectedCell = null;

    const soldierImages = document.getElementsByClassName("soldierImage");
    console.log(soldierImages);
    const soldierImageArray= Array.from(soldierImages);
    soldierImageArray.forEach(soldier => {
        soldier.remove()
    })
    soldiers.forEach(soldier => {
        const tableCase = document.getElementById(soldier.coordinates.x+"-"+soldier.coordinates.y)
        const image = document.createElement("img");
        image.src = contextPath+"/assets/images/icons/Small/soldier.png";
        image.classList.add("foreground_img");
        image.classList.add("case");
        image.classList.add("soldierImage");
        image.id = soldier.id;
        tableCase.appendChild(image);
        tableCase.addEventListener('click',()=>selectSoldier(soldier));
        i++;
    })
}

/**
 * Selects a soldier on the game board.
 *
 * @param {Object} soldier - The soldier object to select, with coordinates property.
 * @return {void}
 */
function selectSoldier(soldier){
    const tableCase = document.getElementById(soldier.coordinates.x+"-"+soldier.coordinates.y)

    if (selectedCell) {
        selectedCell.classList.remove("selectedCell")
        selectedCell = null;
    }
    const imgElement = tableCase.querySelector(`img[id="${soldier.id}"]`);
    const soldierInfo = document.getElementById("soldier-info")

    if (imgElement) {
        if (soldier.login === playerSession){
            selectedSoldier = soldier;
            tableCase.classList.add("selectedCell");
            selectedCell = tableCase;
            const healthBar = document.getElementById('health-bar');
            const healthPercentage = (soldier.hp / soldier.maxHP) * 100;
            healthBar.style.width = healthPercentage + '%';

            if (healthPercentage > 50) {
                healthBar.style.backgroundColor = 'green';
            } else if (healthPercentage > 20) {
                healthBar.style.backgroundColor = 'orange';
            } else {
                healthBar.style.backgroundColor = 'red';
            }
            soldierInfo.classList.add("soldier-display");
        }else {
            addMessageToChat("Système: Ce soldat ne t'appartiens pas !");
        }
    }
}

function moveSoldier(direction){
    if (playerSession === playerTurn ) {
        if(selectedSoldier){
            if(soldierMovedList.findIndex(move => move.id === selectedSoldier.id) === -1){
                const cell = document.getElementById(selectedSoldier.id);
                const payload ={
                    type: "moveSoldier",
                    soldierId: selectedSoldier.id,
                    oldCoordinates: selectedSoldier.coordinates,
                    direction: direction,
                };
                socket.send(JSON.stringify(payload));

                soldierMovedList.push(selectedSoldier);

                selectedCell.classList.remove("selectedCell");
                selectedCell = null;
                selectedSoldier = null;
                cell.remove();
            }else {
                addMessageToChat("Système: Ce soldat à déjà bougé !");
            }
        }else {
            addMessageToChat("Système: Veuillez sélectionner un soldat avant de déplacer !");
        }
    }else {
        addMessageToChat("Système: Ce n'est pas votre tour !");
    }
}

function endTurn(){
    socket.send(JSON.stringify({ type: "endTurn" }));
    soldierMovedList= [];
}

document.addEventListener("click", (e) => {
    if (!e.target.classList.contains('case')) {
        if (selectedCell) {
            selectedCell.classList.remove('selectedCell');
            const soldierInfo = document.getElementById("soldier-info")
            soldierInfo.classList.remove("soldier-display");

            selectedCell = null;
            selectedSoldier = null;
        }
    }
})


document.getElementById("north-btn").addEventListener("click", () => moveSoldier("north"));
document.getElementById("south-btn").addEventListener("click", () => moveSoldier("south"));
document.getElementById("west-btn").addEventListener("click", () => moveSoldier("west"));
document.getElementById("east-btn").addEventListener("click", () => moveSoldier("east"));
document.getElementById("end-turn-btn").addEventListener("click", () => endTurn());