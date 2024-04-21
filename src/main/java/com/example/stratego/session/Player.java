package com.example.stratego.session;

public class Player {
    private static int nextID = 0;
    private String username;
    private int id;

    public Player(String username) {
        this.id = nextID;
        this.username = username;
        nextID++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
