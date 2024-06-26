package com.example.stratego.connection;

import com.example.stratego.session.Board;
import com.example.stratego.session.Color;
import com.example.stratego.session.Position;
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
    public void setClose(boolean close){
        this.close = close;
    }
    public boolean getClose(){
        return this.close;
    }

    public void setCheat(boolean cheat){
        this.cheat = cheat;
    }
    public boolean getCheat(){
        return this.cheat;
    }
    public void setCheck(boolean check){
        this.check = check;
    }
    public boolean getCheck(){
        return this.check;
    }

    private Board board;
    private int initiator;
    private int lobbyID;

    @JsonProperty("winner")
    private Color winner;
    @JsonProperty("close")
    private boolean close;
    @JsonProperty("cheat")
    private boolean cheat;
    @JsonProperty("check")
    private boolean check;

    public Position getOldPos() {
        return oldPos;
    }

    public void setOldPos(Position oldPos) {
        this.oldPos = oldPos;
    }

    public Position getNewPos() {
        return newPos;
    }

    public void setNewPos(Position newPos) {
        this.newPos = newPos;
    }

    @JsonProperty("oldPos")
    private Position oldPos;
    @JsonProperty("newPos")
    private Position newPos;

}
