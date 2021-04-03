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

  @MessageMapping("/chat/{roomId}/sendMessage")
  public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
  }

  @MessageMapping("/chat/{roomId}/addUser")
  public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
                      SimpMessageHeaderAccessor headerAccessor) {



    String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
    //gdyby bylo cos tutaj to znaczyloby ze uzytkownik byl juz w jakims pokoju a to musimy obsluzyc
    if (currentRoomId != null) {
      System.out.println("currnet Room = " + currentRoomId);
      ChatMessage leaveMessage = new ChatMessage();
      leaveMessage.setType(ChatMessage.MessageType.LEAVE);
      leaveMessage.setSender(chatMessage.getSender());
      messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);


      //why it is here, because at start we used addUser not joinToTheGame
      String username = (String) headerAccessor.getSessionAttributes().get("username");
      if( GameController.GamesState.containsKey(currentRoomId)){
          resetTheGameIfNeeded(username,currentRoomId);
      }
    }
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
  }
  private boolean ifPlayerHaveAnImportantRoleInCurrentState(GameState gameState, String username){
    return (gameState.getState() == GameState.State.PLAYING &&
            gameState.getPlayer1().equals(username) ||
            gameState.getPlayer2().equals(username) ||
            ((gameState.getState() == GameState.State.WIN1 ||
                    gameState.getState() == GameState.State.WIN2)
                    && gameState.getPlayer2().equals(username)) ||
            ( gameState.getState() == GameState.State.WAITING_FOR_PLAYERS
                    && (gameState.getPlayer1().equals(username) || gameState.getPlayer2().equals(username))));
  }
  private void resetTheGameIfNeeded(String username,String currentRoomId){
    GameState gameState =  GameController.GamesState.get(currentRoomId);
    if(ifPlayerHaveAnImportantRoleInCurrentState(gameState,username)){
      RestartMessage restartMessage = new RestartMessage();
      restartMessage.setRestart(true);
      GameController.GamesState.remove(currentRoomId);
      messagingTemplate.convertAndSend(format("/game/%s", currentRoomId), restartMessage);
    }
  }
}
