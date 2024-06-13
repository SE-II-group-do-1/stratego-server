package com.example.stratego.connection;

import com.example.stratego.session.Player;

public class LobbyMessage {

    private int lobbyID;
    private Player blue;
    private Player red;

    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
    }

    public Player getBlue() {
        return blue;
    }

    public void setBlue(Player blue) {
        this.blue = blue;
    }

    public Player getRed() {
        return red;
    }

    public void setRed(Player red) {
        this.red = red;
    }
}
