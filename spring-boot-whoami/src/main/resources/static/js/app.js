//Almicke Navarro 
//CST-452 
//March 1, 2021 
//I used the source code from the following websites: https://github.com/code-sketch/memory-game, https://github.com/Artur-Wisniewski/minesweeper

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

var cards = []; 

//differest states of the game
var States = {
    WAITING: 1,
    PLAYING: 2,
	FINALGUESS1: 3,
	FINALGUESS2: 4,
	WIN1: 5, 
	WIN2 : 6
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
var startOfGame = true;
var endOfGame = false;
var myRole = Roles.OBSERVER;
var currentState = States.WAITING;

//method to restart the server
function initRestart(){
    //first restart server
    let restart = {
        restart:true
    };
    stompClient.send(`${gameTopic}/restart`, {}, JSON.stringify(restart));
}
//method to play again - reset roles & state/recreate board/choose new mystery card (METHOD NOT CURRENTLY USED)
function playAgain(){
    //restart client
    player2 = true;
    player1 = true;
    startOfGame = true;
	endOfGame = false;
    myRole = Roles.OBSERVER;
    currentState = States.WAITING;
	shuffle();
    $("#PLAY-AGAIN").hide();
    $("#PLAYER1").show();
    $("#PLAYER1").prop("disabled",false);
    $("#PLAYER2").show();
    $("#PLAYER2").prop("disabled",false);
    $("#FINAL-GUESS").hide();
	showNotification("Ready to play again!")
}

//method to create the board based on size - needs size, numOfImages, and linkToImages to be populated
function initBoard(){
	//create an htmlString to hold the game board and chosen mystery card
    var playingCardsHtml = "";
	var chosenCardHtml = ""; 
	
	//check the size is not null
	if (size) { 
		
		//create the board based on the size of the board
		if (size == 3) {
			//loop to add a new playing card
	   		for (var i = 1; i < numOfImages+1; i++) {
			    playingCardsHtml += '<div class="size3x3 playing-card">' +
						      '<img class="front-face" src="'+ linkToImages[i] +'"/>' +
						      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card" />' + 
						    '</div>';
			}
	   	
	   		console.log("Playing Cards:" + playingCardsHtml); 
			
		}
		else if (size == 4) {
			//loop to add a new playing card
	   		for (var i = 1; i < numOfImages+1; i++) {
			    playingCardsHtml += '<div class="size4x3 playing-card">' +
						      '<img class="front-face" src="'+ linkToImages[i] +'"/>' +
						      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card" />' + 
						    '</div>';
			}
	   	
	   		console.log(playingCardsHtml);
			
		}
		else if (size == 5) {
			//loop to add a new playing card
	   		for (var i = 1; i < numOfImages+1; i++) {
			    playingCardsHtml += '<div class="size5x3 playing-card">' +
						      '<img class="front-face" src="'+ linkToImages[i] +'"/>' +
						      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card" />' + 
						    '</div>';
			}
	   	
	   		//console.log(playingCardsHtml);
		}
		
		//generate a random number
		let ran = Math.floor(Math.random() * numOfImages);
		
		//add the chosen playing card generated by the random number
		chosenCardHtml += '<div class="chosen-playing-card">' +
					      '<img class="front-face" src="'+ linkToImages[ran] +'"/>' +
					      '<img class="back-face" src="/images/Card1.png" alt="Back Face of Who Am I Card">' +
					      '</div>'; 

		//console.log(chosenCardHtml);
		
		//find the divs & add the htmlStrings to it 
	   	document.getElementById("memory-game").innerHTML = playingCardsHtml;
	   	document.getElementById("chosenCard").innerHTML = chosenCardHtml;
		
	}

}

//determine the id location of the message on the html page
msgInput = $('#message');

//var to hold different colors for the avatar background
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

//CREATE GAME FUNCTIONS

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

//method to create the new game room with the user 
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
        $("#create4").attr("class","");
	
		//create the new endpoint
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
        
    }
}

//method to connect to the determined room 
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
    	
		//hide & show certain divs
        $("#join1").hide();  
        $("#join2").attr("class","");

		//create the new endpoint
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
}

//method to initiate the new room once they are connected to the endpoint
function onConnected() {
    enterRoom($("#room-id").val());
    $(".connecting").hide();
}
//method to handle the error of not being able to connec to the endpoint
function onError(error) {
    $(".connecting").css("color","red");
    $(".connecting").text('Could not connect to WebSocket server. Please refresh this page to try again!');
}

//method to create the board, set the correct endpoints, and set the subscriptions/listeners
function enterRoom(newRoomId) {
    initBoard(); //add the board to the room
    addClick(); //add the click function to each card
	shuffle(); //shuffle the cards
    roomId = newRoomId;
    $("#room-id-display").text(roomId);
    topic = `/app/chat/${newRoomId}`;
    gameTopic = `/app/game/${newRoomId}`;

	//check if subscriptions are null
    if (currentSubscription || gameSubscription) {
		//reset the current subscriptions to ensure they are empty 
        currentSubscription.unsubscribe();
        gameSubscription.unsubscribe();
    }

	//set passive listeners to these endpoints
    currentSubscription = stompClient.subscribe(`/channel/${roomId}`, onMessageReceived);
    gameSubscription = stompClient.subscribe(`/game/${roomId}`, onGameMessageReceived);
	
	//sends the new user to the gamecontroller
    stompClient.send(`${topic}/addUser`, {}, JSON.stringify({sender: username, type: 'JOIN'}));
    stompClient.send(`${gameTopic}/joinToTheGame`, {}, JSON.stringify({sender: username, type: 'JOIN'}));
    
	//check if the bundle vars are already set 
	if (size !== null && numOfImages !== null && linkToImages != null) {
		
		//send the bundle vars
		sendBundle();  		
	}
	
}

//SEND FUNCTIONS

//method to send the bundle vars to the endpoint
function sendBundle() {
	//convert dictionary to list
	var listOfLinks = [];
	for (var i = 1; i < numOfImages+1; i++) {
		listOfLinks.push(linkToImages[i]);
	}

	//define bundleMessage - basically, sendBundle() will receive this as the bundle POJO
    var BundleMessage ={
		size: parseInt(size),
		numOfImages: parseInt(numOfImages),
		linkToImages: listOfLinks
    }
    console.log(JSON.stringify(BundleMessage));

	//sends the BundleMessage to this url - interfacts with the controller
    stompClient.send(`${gameTopic}/sendBundle`, {}, JSON.stringify(BundleMessage));
}

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

//method to send the player role who is asking the final guess to the endpoint
function sendFinalGuess() {
	//console.log("I am inside sendFinalGuess()"); 
	//check if the role and stomp client is not null
    if (myRole && stompClient) {
	finalGuess =""; 
    
    	//define the attributes of the final guess 
        var FinalGuessMessage = {
			role: myRole 
        };
        //console.log("ready to sendFinalGuess() to controller"); 

        $("#FINAL-GUESS").hide();

        //send the final game message to the game 
        stompClient.send(`${gameTopic}/sendFinalGuess`, {}, JSON.stringify(FinalGuessMessage));
    }
}

//method to send who has won
function sendWin(theWinner){
    var win = {
        winner: theWinner
    };
    stompClient.send(`${gameTopic}/sendWin`, {}, JSON.stringify(win));
}

//method to send user to the create page 
function sendToCreate(){
	location.href = "create"; 
}
//method to send user to the join page 
function sendToJoin(){
	location.href = "join"; 
}

//MESSAGE FUNCTIONS

//method determine the needed action for the game message received
function onGameMessageReceived(payload) {
    var gameMessage = JSON.parse(payload.body);
	
	//checks if the user's 
	if(Object.keys(linkToImages).length === 0) {
		console.log("New player! Let's give you the board!");
		
		//console.log(gameMessage.bundle.linkToImages); 
		
		//set the bundle vars
		numOfImages = gameMessage.bundle.numOfImages; 
		size = gameMessage.bundle.size; 
		
		//loop to add the images to a local array 
		for(i=1;i<numOfImages+1;i++) { 	
			
			//set the linkToImages var	
			linkToImages[i] = gameMessage.bundle.linkToImages[i-1];  
			
			console.log("linkToImages" + i + " " + linkToImages[i]); 
		}
		
		//re-send & set the board
		initBoard(); 
	    addClick(); 
		shuffle();
		sendBundle(); //Persists the bundle in the endpoint
	}
	//check if game is to be restarted
    if(gameMessage.restart){
        //showNotification("RESTART!");
        //restart();
    }
    if(gameMessage.player1 && player1){
	
		//outputs the player 1 role 
        showNotification(gameMessage.player1 + " is Player 1!")
        player1 = false;

		//removes the button from all game rooms
        $( "#PLAYER1" ).hide();
    }
    if(gameMessage.player2 && player2){
	
		//outputs the player 2 role 
        showNotification(gameMessage.player2 + " is Player 2!")

		//removes the button from all game rooms
        $( "#PLAYER2" ).hide();
        player2 = false;
    }
	//check if game is ready to be played
    if(gameMessage.state == 'PLAYING'){
        if(startOfGame)
        showNotification(gameMessage.player1 + " is asking first!")
        startOfGame=false;
        currentState = States.PLAYING;
        $("#FINAL-GUESS").attr("class","primary inline");
        $("#FINAL-GUESS").prop("disabled",false);
        $("#FINAL-GUESS").show();

    }//check if player 1 is making a final guess
    if(gameMessage.state == 'FINALGUESS1'){
        showNotification(gameMessage.player1 + " is ready to ask their final question!")
        showNotification(gameMessage.player2 + ", please respond either YES or NO to the question")
        currentState = States.FINALGUESS1;
        $("#FINAL-GUESS").hide();

    }//check if player 2 is making a final guess
    if(gameMessage.state == 'FINALGUESS2'){
        showNotification(gameMessage.player2 + " is ready to ask their final question!")
        showNotification(gameMessage.player1 + ", please respond either YES or NO to the question")
        currentState = States.FINALGUESS2;
        $("#FINAL-GUESS").hide();

    }//check if player 1 as won 
	if(gameMessage.state == "WIN1"){
		//output winner
        showNotification(gameMessage.player1+": WIN!");
        showNotification(gameMessage.player2+": LOST!");

		//add the buttons 
		/*$("#PLAY-AGAIN").attr("class","primary inline");
        $("#PLAY-AGAIN").prop("disabled",false);
        $("#PLAY-AGAIN").show();*/        
		$("#CREATE-AGAIN").attr("class","primary inline");
        $("#CREATE-AGAIN").prop("disabled",false);
        $("#CREATE-AGAIN").show();
		$("#JOIN-AGAIN").attr("class","primary inline");
        $("#JOIN-AGAIN").prop("disabled",false);
        $("#JOIN-AGAIN").show();

    }//check if player 2 as won 
	if(gameMessage.state == "WIN2"){
		//output winner
        showNotification(gameMessage.player1+": LOST!");
        showNotification(gameMessage.player2+": WIN!");

		//add the buttons 
        /*$("#PLAY-AGAIN").attr("class","primary inline");
        $("#PLAY-AGAIN").prop("disabled",false);
        $("#PLAY-AGAIN").show();*/
		$("#CREATE-AGAIN").attr("class","primary inline");
        $("#CREATE-AGAIN").prop("disabled",false);
        $("#CREATE-AGAIN").show();
		$("#JOIN-AGAIN").attr("class","primary inline");
        $("#JOIN-AGAIN").prop("disabled",false);
        $("#JOIN-AGAIN").show(); 
    }
}

//method to determine the needed action for the chat message received
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

	//check for message type
    if (message.type == 'JOIN') {
		//output the new user to join
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type == 'LEAVE') {
		//output the user who left
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
		//output the user & their message 
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender); //gets the avatar color to appear correctly in the other users' messages 

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

	//dynamically creates the new message on the page's html & updates the pages 
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    $('#messageArea').append(messageElement);
    $('#messageArea').scrollTop($('#messageArea').prop('scrollHeight'));
}

//method to dynamically create a notification - used for when users join, leave, get a player role, or win/lose
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

//method to send the needed message to the same endpoint 
function sendMessage() {
	//get the message content & trim white spaces
    var messageContent = $('#message').val().trim();

	//check if winner
		//check if the final answer is YES
		if (messageContent.startsWith('YES') && currentState && endOfGame == false) {
			//check if player 1 is asking & player 2 is answering
			if (currentState == "3" && myRole=="3") { 
				sendWin("1"); //send that player 1 wins & player 2 loses
			}
			//check if player 2 is asking & player 1 is answering
			else if (currentState == "4" && myRole=="2"){
				sendWin("2"); //send that player 2 wins & player 1 loses
			}
			
			endOfGame = true; 
		}
		//check if the final answer is NO
		else if (messageContent.startsWith('NO') && currentState && endOfGame == false) {
			//check if player 1 is asking & player 2 is answering
			if (currentState == "3"&& myRole=="3") { 
				sendWin("2"); //send that player 1 loses & player 2 win
				
			}
			//check if player 2 is asking & player 1 is answering
			else if(currentState == "4"&& myRole=="2"){
				sendWin("1"); //send that player 2 loses & player 1 win
			}
			
			endOfGame = true; 
		}
	//check if message & stompclient is not null 
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };
		//send the complete chat Message - interacts with controller at this point
        stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(chatMessage));
    }
    $('#message').val( '');
}

//method to get the color of the avatar so it will show correctly in other users' game chat
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

//CARD FUNCTIONS 
function addClick() { 
	//get the playing-card divs to represent cards
	cards = document.querySelectorAll('.playing-card');
	
	//add the click functionality to each card
	cards.forEach(card => card.addEventListener('click', flipCard));

}

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

//method to shuffle the cards
function shuffle() {
	//console.log("EVERYDAY IM SHUFFLING THE CARDS"); 
  	cards.forEach(card => {
    let randomPos = Math.floor(Math.random() * numOfImages);
    card.style.order = randomPos;
  })
}

//set the functions of the certain buttons 
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
    $("#FINAL-GUESS").click(function () { sendFinalGuess()});
    $("#CREATE-AGAIN").click(function () { sendToCreate()});
    $("#JOIN-AGAIN").click(function () { sendToJoin()});
    $("#PLAY-AGAIN").click(function () { playAgain()}); //NOT CURRENTLY IN USE 
});
