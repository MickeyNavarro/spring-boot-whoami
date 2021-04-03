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

    
    @MessageMapping("/game/{roomId}/joinToTheGame")
    public void joinToTheGame(@DestinationVariable String roomId, @Payload RoleMessage roleMessage) {
        if(!GamesState.containsKey(roomId)){
            GamesState.put(roomId, new GameState());
            GameState gameState = GamesState.get(roomId);
            gameState.setState(GameState.State.WAITING_FOR_PLAYERS);
        }
        GameState gameState = GamesState.get(roomId);
        //jest w trakcie rozbrajania
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/sendBundle")
    public void sendBundle(@DestinationVariable String roomId, @Payload Bundle bundle) { 

        GameState gameState = GamesState.get(roomId);

        gameState.setBundle(bundle);
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/sendRole")
    public void sendRole(@DestinationVariable String roomId, @Payload RoleMessage roleMessage) {

        if(!GamesState.containsKey(roomId)){
            GamesState.put(roomId, new GameState());
            GameState gameState = GamesState.get(roomId);
            gameState.setState(GameState.State.WAITING_FOR_PLAYERS);
        }
        GameState gameState = GamesState.get(roomId);

        if(roleMessage.getRole().equals(RoleMessage.Role.PLAYER1) && gameState.getPlayer1().isEmpty()){
            gameState.setPlayer1(roleMessage.getSender());
        }else if(roleMessage.getRole().equals(RoleMessage.Role.PLAYER2) && gameState.getPlayer2().isEmpty()){
            gameState.setPlayer2(roleMessage.getSender());
        }

        if(!gameState.getPlayer2().isEmpty() && !gameState.getPlayer1().isEmpty()){
            gameState.setState(GameState.State.PLAYING);
        }
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/sendFinalGuess")
    public void sendFinalGuess(@DestinationVariable String roomId, @Payload FinalGuessMessage finalGuessMessage) {

        GameState gameState = GamesState.get(roomId);

        if(finalGuessMessage.getRole().equals("2")){
            gameState.setState(GameState.State.FINALGUESS1);
        }else if(finalGuessMessage.getRole().equals("3") ){
            gameState.setState(GameState.State.FINALGUESS2);
        }

        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/sendWin")
    public void sendWin(@DestinationVariable String roomId, @Payload WinnerMessage winMessage) {

        GameState gameState = GamesState.get(roomId);

        if(winMessage.getWinner().equals("1")){
            gameState.setState(GameState.State.WIN1);
        }else if(winMessage.getWinner().equals("2") ){
            gameState.setState(GameState.State.WIN2);
        }

        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }

    @MessageMapping("/game/{roomId}/restart")
    public void restart(@DestinationVariable String roomId, @Payload RestartMessage restartMessage ) {

        if(GamesState.containsKey(roomId));
            GamesState.remove(roomId);
        messagingTemplate.convertAndSend(format("/game/%s", roomId), restartMessage);
    }


}
