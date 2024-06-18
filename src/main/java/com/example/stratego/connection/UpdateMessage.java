package com.example.stratego.connection;

import com.example.stratego.session.Board;
import com.example.stratego.session.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public void setWinner(Color winner){
        this.winner = winner;
    }
    public Color getWinner() {
        return winner;
    }

    private Board board;
    private int initiator;
    private int lobbyID;

    @JsonProperty("winner")
    private Color winner;

}
