package com.example.stratego.session;

import com.example.stratego.session.exceptions.WrongConstructorException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Piece {
    private Rank rank;

    private boolean visible;

    private boolean movable;

    private Color color;

    @JsonCreator
    public Piece(@JsonProperty("rank") Rank rank, @JsonProperty("color") Color color){
        this.color = color;
        this.rank = rank;
        this.visible = false;
        this.movable = rank != Rank.LAKE && rank != Rank.FLAG && rank != Rank.BOMB;
    }

    /**
     * Construcor specific for Lakes. ID is always -1 (do not affect next ID), have no color.
     * @param rank should be Lake, otherwise use other constructor
     * @throws IllegalArgumentException if not given Lake rank
     */
    public Piece(Rank rank) throws WrongConstructorException {
        if(rank != Rank.LAKE) throw new WrongConstructorException();
        this.rank = rank;
        this.visible = true;
        this.movable = false;
        this.color = null;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isMovable() {
        return movable;
    }

    public Color getColor() {
        return color;
    }

}
