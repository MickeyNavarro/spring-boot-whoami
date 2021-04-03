//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 1, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.model;

public class GameState {
	//attribute to hold the different states of the game 
    public enum State {
        WAITING_FOR_PLAYERS,
        PLAYING,
        FINALGUESS1, FINALGUESS2, 
        WIN1,WIN2;
    }

    //attributes
    State state;
    Bundle bundle;
    String player1 = "";
    String player2 =  "";

    //getters & setters
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

}
