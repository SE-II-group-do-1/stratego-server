package com.example.stratego.session;

import com.example.stratego.session.exceptions.WrongConstructorException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Piece {
    private Rank rank;

    private boolean isVisible;
    private boolean isMovable;

    private Color color;

    public Piece(Rank rank, Color color){
        this.color = color;
        this.rank = rank;
        this.isVisible = false;
        this.isMovable = rank != Rank.LAKE && rank != Rank.FLAG && rank != Rank.BOMB;
    }

    /**
     * Construcor specific for Lakes. ID is always -1 (do not affect next ID), have no color.
     * @param rank should be Lake, otherwise use other constructor
     * @throws IllegalArgumentException if not given Lake rank
     */
    public Piece(Rank rank) throws WrongConstructorException {
        if(rank != Rank.LAKE) throw new WrongConstructorException();
        this.rank = rank;
        this.isVisible = true;
        this.isMovable = false;
        this.color = null;
    }

    @JsonCreator
    public Piece(@JsonProperty("color") Color color,
                 @JsonProperty("isMovable") boolean isMovable,
                 @JsonProperty("isVisible") boolean isVisible,
                 @JsonProperty("rank") Rank rank) {
        this.color = color;
        this.isMovable = isMovable;
        this.isVisible = isVisible;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public boolean isMovable() {
        return isMovable;
    }

    public Color getColor() {
        return color;
    }

}
