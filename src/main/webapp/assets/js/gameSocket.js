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
    if (selectedSoldier != null && selectedCell !== undefined)
        updateHealthBar(selectedSoldier)
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
        changeButtonState(playerSession !== playerTurn)
    }
    if (payload.playersInfo !== undefined) {
        const currentPlayer = playerSession;
        payload.playersInfo.forEach(player => {
            if (player.login === currentPlayer) {
                document.getElementById("playerScore").textContent = player.score;
                document.getElementById("playerProductionPoint").textContent = player.productionPoints;
            }
        });
    }
    if(payload.gameState !== undefined) {
        const gamePage = document.getElementById("game-page");
        const waitingScreen = document.getElementById("waiting-screen");
        const victoryScreen = document.getElementById("victory-screen");

        switch (payload.gameState){
            case "waiting":
                gamePage.classList.add("display-none");
                waitingScreen.classList.remove("display-none");
                break;
            case "victory":
                gamePage.classList.add("display-none");
                victoryScreen.classList.remove("display-none");
                victoryScreen.querySelector("h1").textContent = payload.player+" à gagné avec un score de "+payload.score+" points !";
                setTimeout(function() {
                    window.location = contextPath+"/profile";
                }, 3000);
                break;
            default:
                gamePage.classList.remove("display-none");
                waitingScreen.classList.add("display-none");
                break;
           }
    }
    if (payload.soldiers !== undefined) {
        displaySoldiers(payload.soldiers)
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

    if (imgElement) {
        if (soldier.login === playerSession){
            selectedSoldier = soldier;
            tableCase.classList.add("selectedCell");
            selectedCell = tableCase;
            updateHealthBar(selectedSoldier)
            const soldierInfo = document.getElementById("soldier-info")
            soldierInfo.classList.add("soldier-display");

        }else {
            addMessageToChat("Système: Ce soldat ne t'appartiens pas !");
        }
    }
}

function moveSoldier(direction){
    if(doSelectedSoldierCanDoAnAction()){
        const cell = document.getElementById(selectedSoldier.id);
        const payload ={
            type: "moveSoldier",
            soldierId: selectedSoldier.id,
            oldCoordinates: selectedSoldier.coordinates,
            direction: direction,
        };
        socket.send(JSON.stringify(payload));
        cell.remove();
        endOfAnAction()
    }
}

function endTurn(){
    socket.send(JSON.stringify({ type: "endTurn" }));
    soldierMovedList= [];
}

function healSoldier(){
    if (doSelectedSoldierCanDoAnAction()){
        const payload = {
            type: "healSoldier",
            soldierId: selectedSoldier.id
        };
        socket.send(JSON.stringify(payload));
        endOfAnAction()
    }
}

function deforestAction(){
    if (doSelectedSoldierCanDoAnAction()) {
        const payload = {
            type: "deforestAction",
            soldierId: selectedSoldier.id
        };
        socket.send(JSON.stringify(payload));
        endOfAnAction();
    }
}

function recruitAction(){
    if (doSelectedSoldierCanDoAnAction()) {
        const payload = {
            type: "recruitAction",
        };
        socket.send(JSON.stringify(payload));
        endOfAnAction();
    }
}


/**
 * Updates the health bar for a given soldier based on their current health points (hp) and maximum health points (maxHP).
 *
 * @param {Object} soldier - The soldier object containing health information.
 * @param {number} soldier.hp - The current health points of the soldier.
 * @param {number} soldier.maxHP - The maximum health points of the soldier.
 *
 * @return {void} - This function does not return anything.
 */
function updateHealthBar(soldier){
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
}
/**
 * Check if the selected soldier can perform an action based on the current player session and turn
 * @return {boolean} Returns true if the selected soldier can perform an action, otherwise false
 */
function doSelectedSoldierCanDoAnAction(){
    if (playerSession === playerTurn ) {
        if(selectedSoldier){
            if(soldierMovedList.findIndex(move => move.id === selectedSoldier.id) === -1){
                return true;
            }else {
                addMessageToChat("Système: Ce soldat à déjà agis !");
            }
        }else {
            addMessageToChat("Système: Veuillez sélectionner un soldat !");
        }
    }else {
        addMessageToChat("Système: Ce n'est pas votre tour !");
    }
    return false;
}

/**
 * Marks the end of an action by updating variables and classes
 *
 * @return {void}
 */
function endOfAnAction(){
    soldierMovedList.push(selectedSoldier);
    selectedCell.classList.remove("selectedCell");
    selectedCell = null;
    selectedSoldier = null;
}

/**
 * Function to change the state of all buttons with class "g-btn".
 * @param {boolean} buttonState - The new state to set for the buttons (true for disabled, false for enabled).
 * @return {void}
 */
function changeButtonState(buttonState){
    const buttons = document.querySelectorAll(".g-btn");
    buttons.forEach(button => {
        button.disabled = buttonState;
    });
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
document.getElementById("heal-btn").addEventListener("click", () => healSoldier());
document.getElementById("deforest-btn").addEventListener("click", () => deforestAction());
document.getElementById("recruit-btn").addEventListener("click", () => recruitAction());