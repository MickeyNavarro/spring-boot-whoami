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

import com.SeniorCapstone.WhoAmI.model.Board;
import com.SeniorCapstone.WhoAmI.model.GameBundle;
import com.SeniorCapstone.WhoAmI.model.GameState;
import com.SeniorCapstone.WhoAmI.model.IfWinMessage;
import com.SeniorCapstone.WhoAmI.model.Image;
import com.SeniorCapstone.WhoAmI.model.RestartMessage;
import com.SeniorCapstone.WhoAmI.model.RoleMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

import java.io.IOException;

@Controller
public class GameController {
	
	//initialize the game board bundle 
	private static GameBundle theGameBundle = new GameBundle(); 

    static HashMap<String, GameState> GamesState = new HashMap<String, GameState>();
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

  	
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
            gameState.setState(GameState.State.DEPLOYING);
        }
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
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
    @MessageMapping("/game/{roomId}/sendBoard")
    public void sendBoard(@DestinationVariable String roomId, @Payload Board board) {

        GameState gameState = GamesState.get(roomId);

        gameState.setBoard(board);//tu ustawia bomby
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/sendMove")
    public void sendMove(@DestinationVariable String roomId, @Payload Board board) {
        GameState gameState = GamesState.get(roomId);
        gameState.setVisibleBoard(board);//tu ustawia to co widzi
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }

    @MessageMapping("/game/{roomId}/sendState")
    public void sendState(@DestinationVariable String roomId, @Payload IfWinMessage ifWinMessage) {
        GameState gameState = GamesState.get(roomId);


        if(ifWinMessage.getWin())
            gameState.setState(GameState.State.WIN);//tu ustawia to co widzi
        else gameState.setState(GameState.State.LOST);//tu ustawia to co widzi
        messagingTemplate.convertAndSend(format("/game/%s", roomId), gameState);
    }
    @MessageMapping("/game/{roomId}/restart")
    public void restart(@DestinationVariable String roomId, @Payload RestartMessage restartMessage ) {

        if(GamesState.containsKey(roomId));
            GamesState.remove(roomId);
        messagingTemplate.convertAndSend(format("/game/%s", roomId), restartMessage);
    }


}
