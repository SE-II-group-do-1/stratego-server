package com.example.stratego.connection;

import com.example.stratego.session.Board;
import com.example.stratego.session.Player;

public class UpdateMessage {
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getInitiator() {
        return initiator;
    }

    public void setInitiator(int initiator) {
        this.initiator = initiator;
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
    }

    public void setWinner(Player winner){
        this.winner = winner;
    }
    public Player getWinner() {
        return winner;
    }

    private Board board;
    private int initiator;
    private int lobbyID;

    private Player winner;

}
