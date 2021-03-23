
//variables
var stompClient = null;
var currentSubscription;
var gameSubscription;
var username = null;
var roomId = null;
var topic = null;
var gameTopic = null;

//differest states of the game
var States = {
    WAITING: 1,
    DEPLOYING: 2,
};

//different roles
var Roles = {
    OBSERVER : 1,
    BOMBER: 2,
    SAPER: 3,

};
//game
var sekundy = 0;
var czasUplywa = false;
var saper = true;
var bomber = true;
var iknowBro = true;
var DeployingFirstTime = true;
var odkryto = {};
var myRole = Roles.OBSERVER;
var currentState = States.WAITING;
var boardSize = 18;
var boombsCounter = 0;
var flagCounter = 0;
var boardBombs = [];
var iloscOdkrytychPol = 0;
var board = [];
var visibleBoard = [];

//method to restart the server
function initRestart(){
    //first restart server
    let restart = {
        restart:true
    };
    stompClient.send(`${gameTopic}/restart`, {}, JSON.stringify(restart));
}
function restart(){
    //restart client
    saper = true;
    bomber = true;
    iknowBro = true;
    DeployingFirstTime = true;
    odkryto = {};
    myRole = Roles.OBSERVER;
    currentState = States.WAITING;
    boombsCounter = 0;
    flagCounter = 0;
    boardBombs = [];
    iloscOdkrytychPol = 0;
    board = [];
    visibleBoard = [];
    czasUplywa = false;
    sekundy = 0;
    clearBoard();
    initBoard();
    $("#RESTART").hide();
    $("#BOMBER").show();
    $("#BOMBER").prop("disabled",false);
    $("#SAPER").show();
    $("#SAPER").prop("disabled",false);
    $("#COMMIT").hide();
}

function initBoard(){

    $("#licznikFlag").html(boombsCounter);
}

function sendLost(){
    var lost = {
        win: false
    };
    console.log(JSON.stringify(lost));
    stompClient.send(`${gameTopic}/sendState`, {}, JSON.stringify(lost));
}
function sendWin(){
    var win = {
        win: true
    };
    console.log(JSON.stringify(flagCounter));
    stompClient.send(`${gameTopic}/sendState`, {}, JSON.stringify(win));
}
msgInput = $('#message');
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(){
    if( $('#room-id').val() == "" ||  $('#name').val() == "" )
    {
        preventDefault();
    }
    username = $("#name").val().trim();
    if (username) {
        $("#stage1").hide();
        $("#stage2").attr("class","");


        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
}
function onConnected() {
    enterRoom($("#room-id").val());
    $(".connecting").hide();
}

function onError(error) {
    $(".connecting").css("color","red");
    $(".connecting").text('Could not connect to WebSocket server. Please refresh this page to try again!');
}
function enterRoom(newRoomId) {
    //initBoard(); //ADD THE BOARD TO THE ROOM 
    roomId = newRoomId;
    $("#room-id-display").text(roomId);
    topic = `/app/chat/${newRoomId}`;
    gameTopic = `/app/game/${newRoomId}`;


    if (currentSubscription || gameSubscription) {

        currentSubscription.unsubscribe();
        gameSubscription.unsubscribe();
    }
    currentSubscription = stompClient.subscribe(`/channel/${roomId}`, onMessageReceived);
    gameSubscription = stompClient.subscribe(`/game/${roomId}`, onGameMessageReceived);

    stompClient.send(`${topic}/addUser`, {}, JSON.stringify({sender: username, type: 'JOIN'}));
    stompClient.send(`${gameTopic}/joinToTheGame`, {}, JSON.stringify({sender: username, type: 'JOIN'}));
}
//do poprawienia wszyyyystko
function sendBOMBERRole() {
    sendRole("BOMBER");
    $("#SAPER").prop("disabled",true);
    myRole = Roles.BOMBER;
}
function sendSAPERRole() {
    sendRole("SAPER");
    $("#BOMBER").prop("disabled",true);
    myRole = Roles.SAPER;
}
function sendRole(roleToSend) {
    if (roleToSend && stompClient) {
        var RoleMessage = {
            sender: username,
            role: roleToSend,
        };
        stompClient.send(`${gameTopic}/sendRole`, {}, JSON.stringify(RoleMessage));
    }
}
function sendMove(board) {
    var BoardMessage ={
        fields: board,
    }
    console.log(JSON.stringify(BoardMessage));
    stompClient.send(`${gameTopic}/sendMove`, {}, JSON.stringify(BoardMessage));
}
function processText(inputText) {
    var output = [];
    console.log(inputText);
    var text = ["element1","element2","element3"];
    console.log(text);
    var i = 0;
    var outputout =  [];
    inputText.forEach(function (item) {
        output.push(item.replace(/\'/g, '').split(/(\d+)/).filter(Boolean));
        outputout.push(output[i][1]);
        i++;
    });
    return outputout;
}
function onGameMessageReceived(payload) {
    var gameMessage = JSON.parse(payload.body);
    console.log(gameMessage);
    if(gameMessage.restart){
        showNotification("RESTART!");
        restart();
    }
    if(gameMessage.bomber && bomber){
        showNotification(gameMessage.bomber + " is Player 1!")
        bomber = false;

        $( "#BOMBER" ).hide();
    }
    if(gameMessage.saper && saper){
        showNotification(gameMessage.saper + " is Player 2!")
        $( "#SAPER" ).hide();
        saper = false;
    }
    if(gameMessage.state == 'DEPLOYING'){
        if(DeployingFirstTime)
        showNotification(gameMessage.bomber + " is DEPLOYING!")
        DeployingFirstTime=false;
        currentState = States.DEPLOYING;
        $("#COMMIT").attr("class","primary inline");
        $("#COMMIT").prop("disabled",false);
        $("#COMMIT").show();
        if(myRole != Roles.BOMBER)
            $("#COMMIT").hide();
        else
            addListeners();
        if(gameMessage.flagCounter!=null)
        $("#licznikFlag").text(gameMessage.flagCounter);
    }if(gameMessage.state == "LOST" && iknowBro){

        iknowBro = false;
        showNotification(gameMessage.saper+": LOST!");

        $("#licznikFlag").text(gameMessage.flagCounter);
    }if(gameMessage.state == "WIN" && iknowBro){

        iknowBro = false;
        showNotification(gameMessage.saper+": WIN!");
        $("#licznikFlag").text(gameMessage.flagCounter);
    }
    if( gameMessage.visibleBoard.fields)setBoardView(gameMessage.visibleBoard.fields);
}
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    if (message.type == 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type == 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    $('#messageArea').append(messageElement);
    $('#messageArea').scrollTop($('#messageArea').prop('scrollHeight'));
}
function showNotification(notification){
    var messageElement = document.createElement('li');
    messageElement.classList.add('event-message');
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(notification);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    $('#messageArea').append(messageElement);
    $('#messageArea').scrollTop($('#messageArea').prop('scrollHeight'));
}
function sendMessage() {
    var messageContent = $('#message').val().trim();
    if (messageContent.startsWith('/join ')) {

        var newRoomId = messageContent.substring('/join '.length);
        if(newRoomId != roomId){
            restart();
            clearBoard();
            enterRoom(newRoomId);
            $('#messageArea').empty();
        }

    } else if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };
        stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(chatMessage));
    }
    $('#message').val( '');
}
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#join" ).click(function() { connect(); });
    $("#send").click(function () {  sendMessage()});
    $( "#BOMBER" ).click(function() { sendBOMBERRole(); });
    $("#SAPER").click(function () {  sendSAPERRole()});
    $("#RESTART").click(function () { initRestart()});
});
