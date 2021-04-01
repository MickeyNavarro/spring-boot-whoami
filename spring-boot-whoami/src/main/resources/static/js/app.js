//variables
var stompClient = null;
var currentSubscription;
var gameSubscription;
var username = null;
var roomId = null;
var topic = null;
var gameTopic = null;

//variables for create module
var size = null; 
var numOfImages = null; 
var linkToImages = {}; 

//differest states of the game
var States = {
    WAITING: 1,
    DEPLOYING: 2,
};

//different roles
var Roles = {
    OBSERVER : 1,
    PLAYER1 : 2,
    PLAYER2 : 3,

}

//game
var player2 = true;
var player1 = true;
var iknowBro = true;
var DeployingFirstTime = true;
var myRole = Roles.OBSERVER;
var currentState = States.WAITING;
var flagCounter = 0;
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
    player2 = true;
    player1 = true;
    iknowBro = true;
    DeployingFirstTime = true;
    myRole = Roles.OBSERVER;
    currentState = States.WAITING;
    flagCounter = 0;
    board = [];
    visibleBoard = [];
    clearBoard();
    initBoard();
    $("#RESTART").hide();
    $("#PLAYER1").show();
    $("#PLAYER1").prop("disabled",false);
    $("#PLAYER2").show();
    $("#PLAYER2").prop("disabled",false);
    $("#COMMIT").hide();
}

function initBoard(){

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

//method to set the size of the gameboard 
function setSize(){

	//check if variables are empty
    if( $('#size').val() == "")
    {
        preventDefault();
    }
    
    //trim white space
    size = $("#size").val().trim();
    
    //check if size is not null
    if (size) {
    
    	//output the board size
    	console.log("Size of board: " + size); 
    	
    	//hide the create1 div
        $("#create1").hide();
        
        //create an htmlString to hold the form 
        var htmlString = "";
	   	htmlString += '<tr>' +
						'<th><label>Image #</label></th>' +
						'<th><label>Link to Image</label></th>' +
					'</tr>';
		
		//define how many images are needed 
		numOfImages = size * 3; 
		console.log("Number of Images Needed: " + numOfImages);
		
		//loop to add a new input based on the numOfImages
	   	for(i=1;i<numOfImages+1;i++)
	   	{
	       	htmlString += '<tr>' +
							'<td>'+ i +'</td>' +
							'<td><input type="text" id="imageLink'+ i +'" >' +
							'</td>' +
						'</tr>'; 
	   	}
	   	
	   	console.log(htmlString); 
	   	
	   	//find the images div & add the htmlString to it 
	   	document.getElementById("imageUpload").innerHTML = htmlString;
        
        //show the create2 div 
        $("#create2").attr("class","");
    }
}

//method to set the images of the gameboard
function setImages() { 
	console.log("setImages() is called"); 
	
	//loop to add the images to a local array 
	for(i=1;i<numOfImages+1;i++) { 
		//console.log(document.getElementById("imageLink" + i).value); 
		
		linkToImages[i] = document.getElementById("imageLink" + i).value; 
		
		console.log("linkToImages" + i + " " + linkToImages[i]); 
	}
	
	//hide the create2 div
    $("#create2").hide();
        
    //show the create3 div 
    $("#create3").attr("class","");

}

function create(){

	//check if the room id or name variables are empty
    if( $('#room-id').val() == "" ||  $('#name').val() == "" )
    {
        preventDefault();
    }
    
    //trim white space
    username = $("#name").val().trim();
    
    //check if username is not null
    if (username) {
    
    	//hide & show the certain divs
        $("#create3").hide();
        
        //create an htmlString to hold the game board 
        var htmlString = "";
		
		//loop to add a new playing card
	   	for (var i = 1; i < numOfImages+1; i++) {
		    htmlString += '<div class="size3x3 playing-card">' +
					      '<img class="front-face" src="'+ linkToImages[i] +'"/>' +
					      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card" />' + 
					    '</div>';
		}
	   	
	   	console.log(htmlString); 
	   	
	   	//find the images div & add the htmlString to it 
	   	document.getElementById("memory-game").innerHTML = htmlString;
        
        $("#create4").attr("class","");

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
        
        sendImages();  
    }
}
function connect(){

	//check if the room id or name variables are empty
    if( $('#room-id').val() == "" ||  $('#name').val() == "" )
    {
        preventDefault();
    }
    
    //trim white space
    username = $("#name").val().trim();
    
    //check if username is not null
    if (username) {
    
        $("#join1").hide();
        
        //create an htmlString to hold the game board 
        var htmlString = "";
		
		//loop to add a new playing card
	   	for (var i = 1; i < numOfImages+1; i++) {
		    htmlString += '<div class="size3x3 playing-card">' +
					      '<img class="front-face" src="'+ linkToImages[i] +'"/>' +
					      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card" />' + 
					    '</div>';
		}
	   	
	   	console.log(htmlString); 
	   	
	   	//find the images div & add the htmlString to it 
	   	document.getElementById("memory-game").innerHTML = htmlString;
        
        $("#join2").attr("class","");

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

//CARD FUNCTIONS 
const cards = document.querySelectorAll('.playing-card');

//method to flip a card from its front face to its back face & vice versa
function flipCard() {

	//check if card is already flipped
  	if(this.classList.contains('flip')) { 
  		//unflip the card - reveal front face
   	 this.classList.remove('flip'); 
  	}
  	else {
  		//flip the card - reveal back face
    	this.classList.add('flip');
  	}
}

function disableCards() {
  firstCard.removeEventListener('click', flipCard);
  secondCard.removeEventListener('click', flipCard);

  resetBoard();
}

function unflipCards() {
  lockBoard = true;

  setTimeout(() => {
    firstCard.classList.remove('flip');
    secondCard.classList.remove('flip');

    resetBoard();
  }, 1500);
}

function resetBoard() {
  [hasFlippedCard, lockBoard] = [false, false];
  [firstCard, secondCard] = [null, null];
}

(function shuffle() {
  cards.forEach(card => {
    let randomPos = Math.floor(Math.random() * 12);
    card.style.order = randomPos;
  });
})();

//add the click functionality to each card
cards.forEach(card => card.addEventListener('click', flipCard));

//method to set the player 1 role
function sendPLAYER1Role() {
	//send the player role to the stomp client
    sendRole("PLAYER1");
    
    //disable to the PLAYER2 button for the player 1 
    $("#PLAYER2").prop("disabled",true);
    
    //set the the user's role to player 1
    myRole = Roles.PLAYER1;
}

//method to set the player 1 role
function sendPLAYER2Role() {

	//send the player role to the stomp client
    sendRole("PLAYER2");
    
    //disable to the PLAYER1 button for the player 2
    $("#PLAYER1").prop("disabled",true);
    
    //set the the user's role to player 1    
    myRole = Roles.PLAYER2;
}

//method to send the player role to the game
function sendRole(roleToSend) {
	//check if the role and stomp client is not null
    if (roleToSend && stompClient) {
    
    	//define the attributes of the role message with the username and chosen role
        var RoleMessage = {
            sender: username,
            role: roleToSend,
        };
        
        //send the role message to the game 
        stompClient.send(`${gameTopic}/sendRole`, {}, JSON.stringify(RoleMessage));
    }
}
function sendImages() {
    console.log(JSON.stringify(linkToImages));
    stompClient.send(`${gameTopic}/sendImages`, {}, JSON.stringify(linkToImages));
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
    if(gameMessage.player1 && player1){
        showNotification(gameMessage.player1 + " is Player 1!")
        player1 = false;

        $( "#PLAYER1" ).hide();
    }
    if(gameMessage.player2 && player2){
        showNotification(gameMessage.player2 + " is Player 2!")
        $( "#PLAYER2" ).hide();
        player2 = false;
    }
    if(gameMessage.state == 'DEPLOYING'){
        if(DeployingFirstTime)
        showNotification(gameMessage.player1 + " is asking first!")
        DeployingFirstTime=false;
        currentState = States.DEPLOYING;
        $("#COMMIT").attr("class","primary inline");
        $("#COMMIT").prop("disabled",false);
        $("#COMMIT").show();
        if(myRole != Roles.PLAYER1)
            $("#COMMIT").hide();
        else
            addListeners();
        if(gameMessage.flagCounter!=null)
        $("#licznikFlag").text(gameMessage.flagCounter);
    }if(gameMessage.state == "LOST" && iknowBro){

        iknowBro = false;
        showNotification(gameMessage.player2+": LOST!");

        $("#licznikFlag").text(gameMessage.flagCounter);
    }if(gameMessage.state == "WIN" && iknowBro){

        iknowBro = false;
        showNotification(gameMessage.player2+": WIN!");
        $("#licznikFlag").text(gameMessage.flagCounter);
    }
    //if( gameMessage.visibleBoard.fields)setBoardView(gameMessage.visibleBoard.fields);
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
    $( "#create-size" ).click(function() { setSize(); });
    $( "#create-images" ).click(function() { setImages(); });
    $( "#create-game" ).click(function() { create(); });
    $( "#join" ).click(function() { connect(); });
    $("#send").click(function () {  sendMessage()});
    $( "#PLAYER1" ).click(function() { sendPLAYER1Role(); });
    $("#PLAYER2").click(function () {  sendPLAYER2Role()});
    $("#RESTART").click(function () { initRestart()});
});
