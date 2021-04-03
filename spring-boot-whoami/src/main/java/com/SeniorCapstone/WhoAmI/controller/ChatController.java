//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 7, 2021 
//I used the source code from the following website: https://github.com/MickeyNavarro/KaraokeMachine, https://github.com/Artur-Wisniewski/minesweeper
package com.SeniorCapstone.WhoAmI.controller;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.SeniorCapstone.WhoAmI.model.ChatMessage;
import com.SeniorCapstone.WhoAmI.model.GameState;
import com.SeniorCapstone.WhoAmI.model.RestartMessage;

@Controller
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	// method to receive the chat message
	@MessageMapping("/chat/{roomId}/sendMessage")
	public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
		// update the channel with the new message
		messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
	}

	// method to receive the new user who has join the game room
	@MessageMapping("/chat/{roomId}/addUser")
	public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor) {

		// get the room ID
		String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);

		// check if room ID currently exists
		if (currentRoomId != null) {
			System.out.println("current Room = " + currentRoomId);

			// if yes, create the message that the user is leaving the room
			ChatMessage leaveMessage = new ChatMessage();
			leaveMessage.setType(ChatMessage.MessageType.LEAVE);
			leaveMessage.setSender(chatMessage.getSender());

			// update the channel with the new message
			messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);

			// get the username of the user sending the message
			String username = (String) headerAccessor.getSessionAttributes().get("username");

			// check if gamestate has the same room ID
			if (GameController.GamesState.containsKey(currentRoomId)) {
				// reset the game
				resetTheGameIfNeeded(username, currentRoomId);
			}
		}

		// set the username of the sender
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		// update the channel with the new message
		messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
	}

	// method to check if the current player is in the game
	private boolean ifPlayerHaveAnImportantRoleInCurrentState(GameState gameState, String username) {
		//returns yes or no based on if the current gamestate is set & if the username exists in the gamestate 
		return (gameState.getState() == GameState.State.PLAYING && gameState.getPlayer1().equals(username)
				|| gameState.getPlayer2().equals(username)
				|| ((gameState.getState() == GameState.State.WIN1 || gameState.getState() == GameState.State.WIN2)
						&& gameState.getPlayer2().equals(username))
				|| (gameState.getState() == GameState.State.WAITING_FOR_PLAYERS
						&& (gameState.getPlayer1().equals(username) || gameState.getPlayer2().equals(username))));
	}

	//method to reset game 
	private void resetTheGameIfNeeded(String username, String currentRoomId) {
		GameState gameState = GameController.GamesState.get(currentRoomId);
		
		//checks if the user exists in the game
		if (ifPlayerHaveAnImportantRoleInCurrentState(gameState, username)) {
			//if yes, a new message is created to reset the game
			RestartMessage restartMessage = new RestartMessage();
			restartMessage.setRestart(true);
			
			//removes the room ID from the game state 
			GameController.GamesState.remove(currentRoomId);

			// update the channel with the new message
			messagingTemplate.convertAndSend(format("/game/%s", currentRoomId), restartMessage);
		}
	}
}
