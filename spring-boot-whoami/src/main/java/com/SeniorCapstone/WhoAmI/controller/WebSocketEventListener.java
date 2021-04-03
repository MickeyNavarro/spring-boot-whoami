//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app 
package com.SeniorCapstone.WhoAmI.controller;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.SeniorCapstone.WhoAmI.model.ChatMessage;
import com.SeniorCapstone.WhoAmI.model.GameState;
import com.SeniorCapstone.WhoAmI.model.RestartMessage;

@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	// method that receives if any web socket connections are created
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection."); // log the information
	}

	// method that receives if any web socket connections are disconnected
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		// get the username and room id of the user trying to disconnect
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");
		// check if username is filled
		if (username != null) {
			logger.info("User Disconnected: " + username); // log the user who has destroyed their web socket connection

			// create a new chat message for user who left - this will apprear in the chat of the game room
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(ChatMessage.MessageType.LEAVE);
			chatMessage.setSender(username);

			// update the game/chat room
			messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
		}

		//get the gamestate & remove the room ID for this player
		GameState gameState = GameController.GamesState.get(roomId);
		RestartMessage restartMessage = new RestartMessage();
		restartMessage.setRestart(true);
		// check if the game is still in session by any user 
		if (gameState.getState() == GameState.State.PLAYING && gameState.getPlayer1().equals(username)
				|| ((gameState.getState() == GameState.State.WIN1 || gameState.getState() == GameState.State.WIN2)
						&& gameState.getPlayer2().equals(username))
				|| (gameState.getState() == GameState.State.WAITING_FOR_PLAYERS
						&& (gameState.getPlayer1().equals(username) || gameState.getPlayer2().equals(username)))) {
			
			GameController.GamesState.remove(roomId);

			// update the game/chat room
			messagingTemplate.convertAndSend(format("/game/%s", roomId), restartMessage);
		}

	}
}
