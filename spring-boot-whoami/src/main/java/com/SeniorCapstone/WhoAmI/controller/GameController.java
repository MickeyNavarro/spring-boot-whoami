//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//January 12, 2021 
//I used the source code from the following website: https://github.com/MickeyNavarro/KaraokeMachine, https://github.com/Artur-Wisniewski/minesweeper
package com.SeniorCapstone.WhoAmI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.SeniorCapstone.WhoAmI.model.Bundle;
import com.SeniorCapstone.WhoAmI.model.FinalGuessMessage;
import com.SeniorCapstone.WhoAmI.model.GameState;
import com.SeniorCapstone.WhoAmI.model.RestartMessage;
import com.SeniorCapstone.WhoAmI.model.RoleMessage;
import com.SeniorCapstone.WhoAmI.model.WinnerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

import java.io.IOException;

@Controller
public class GameController {
	
	//initialize the game state  
    static HashMap<String, GameState> GamesState = new HashMap<String, GameState>();
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    //method to receive the user that needs to be added to the game 
    @MessageMapping("/game/{roomId}/joinToTheGame")
    public void joinToTheGame(@DestinationVariable String roomId, @Payload RoleMessage roleMessage) {
    	//check if the gamestate already has a room ID 
    	if(!GamesState.containsKey(roomId)){
            GamesState.put(roomId, new GameState());
            GameState gameState = GamesState.get(roomId);
            gameState.setState(GameState.State.WAITING_FOR_PLAYERS);
        }
        GameState gameState = GamesState.get(roomId);
        
        //update the gamestate 
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    
    //method to receive the game bundle 
    @MessageMapping("/game/{roomId}/sendBundle")
    public void sendBundle(@DestinationVariable String roomId, @Payload Bundle bundle) { 

        GameState gameState = GamesState.get(roomId);
        
        //set the bundle in the gamestate so that the endpoint will hold the same bundle
        gameState.setBundle(bundle);
        
        //update the gamestate 
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    
    //method to receive which user will take on which role
    @MessageMapping("/game/{roomId}/sendRole")
    public void sendRole(@DestinationVariable String roomId, @Payload RoleMessage roleMessage) {
    	//check if the gamestate already has a room ID 
        if(!GamesState.containsKey(roomId)){
        	//if not, set the new room ID & gamestate
            GamesState.put(roomId, new GameState());
            GameState gameState = GamesState.get(roomId);
            gameState.setState(GameState.State.WAITING_FOR_PLAYERS);
        }
        
        GameState gameState = GamesState.get(roomId);

        //check if what role is trying to be filled & if the role is empty 
        if(roleMessage.getRole().equals(RoleMessage.Role.PLAYER1) && gameState.getPlayer1().isEmpty()){
        	//set the role of player 1
            gameState.setPlayer1(roleMessage.getSender());
        }else if(roleMessage.getRole().equals(RoleMessage.Role.PLAYER2) && gameState.getPlayer2().isEmpty()){
        	//set the role of player 2
            gameState.setPlayer2(roleMessage.getSender());
        }

        //check if both roles are already set
        if(!gameState.getPlayer2().isEmpty() && !gameState.getPlayer1().isEmpty()){
        	//set the new gamestate 
            gameState.setState(GameState.State.PLAYING);
        }
        
        //update the gamestate in the endpoint 
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    
    //method to receive who is asking the final guess & update the game state 
    @MessageMapping("/game/{roomId}/sendFinalGuess")
    public void sendFinalGuess(@DestinationVariable String roomId, @Payload FinalGuessMessage finalGuessMessage) {

        GameState gameState = GamesState.get(roomId);

        //check which player is asking the final guess & set the new gamestate
        if(finalGuessMessage.getRole().equals("2")){
            gameState.setState(GameState.State.FINALGUESS1);
        }else if(finalGuessMessage.getRole().equals("3") ){
            gameState.setState(GameState.State.FINALGUESS2);
        }

        //update the gamestate in the endpoint
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    
    //method to receive the winner & update the game state 
    @MessageMapping("/game/{roomId}/sendWin")
    public void sendWin(@DestinationVariable String roomId, @Payload WinnerMessage winMessage) {

        GameState gameState = GamesState.get(roomId);

        //check which player is the winner & set the new gamestate
        if(winMessage.getWinner().equals("1")){
            gameState.setState(GameState.State.WIN1);
        }else if(winMessage.getWinner().equals("2") ){
            gameState.setState(GameState.State.WIN2);
        }
        
        //update the gamestate in the endpoint 
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }

    //method to restart the endpoint 
    @MessageMapping("/game/{roomId}/restart")
    public void restart(@DestinationVariable String roomId, @Payload RestartMessage restartMessage ) {

    	//check if room ID exists
        if(GamesState.containsKey(roomId));
        	//remove the room ID from the state
            GamesState.remove(roomId);
            
        //update the gamestate in the endpoint    
        messagingTemplate.convertAndSend(format("/game/%s", roomId), restartMessage);
    }


}
