package com.SeniorCapstone.WhoAmI.model;

public class GameState {
    public enum State {
        WAITING_FOR_PLAYERS,
        DEPLOYING,
        LOST,WIN;
    }

    State state;
    Board board;
    Board visibleBoard;
    String player1 = "";
    String player2 =  "";

    public Board getVisibleBoard() {
        return visibleBoard;
    }

    public void setVisibleBoard(Board visibleBoard) {
        this.visibleBoard = visibleBoard;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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
