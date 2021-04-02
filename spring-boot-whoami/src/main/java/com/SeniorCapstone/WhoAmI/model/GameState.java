package com.SeniorCapstone.WhoAmI.model;

public class GameState {
    public enum State {
        WAITING_FOR_PLAYERS,
        DEPLOYING,
        LOST,WIN;
    }

    State state;
    Bundle bundle;
    String player1 = "";
    String player2 =  "";

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
