package com.example.stratego.session;

public class Piece {
    private static int nextID = 0;
    private Rank rank;
    private boolean isVisible;
    private boolean isMovable;

    private Color color;
    private int id;

    public Piece(Rank rank, Color color){
        this.id = nextID;
        this.color = color;
        this.rank = rank;
        this.isVisible = false;
        this.isMovable = rank != Rank.LAKE && rank != Rank.FLAG && rank != Rank.BOMB;
        nextID++;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }
}
