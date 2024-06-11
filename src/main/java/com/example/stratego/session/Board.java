package com.example.stratego.session;

import com.example.stratego.session.exceptions.WrongConstructorException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)

public class Board {

    @JsonProperty("fields")
    private Piece[][] fields;

    public Board(){
        this.fields =  new Piece[10][10];
        // set lakes
        try {
            fields[4][2] = new Piece(Rank.LAKE);
            fields[4][3] = new Piece(Rank.LAKE);
            fields[5][2] = new Piece(Rank.LAKE);
            fields[5][3] = new Piece(Rank.LAKE);

            fields[4][6] = new Piece(Rank.LAKE);
            fields[4][7] = new Piece(Rank.LAKE);
            fields[5][6] = new Piece(Rank.LAKE);
            fields[5][7] = new Piece(Rank.LAKE);
        } catch (WrongConstructorException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void setField(int y, int x, Piece piece){
        fields[y][x] = piece;
    }
    public Piece getField(int y, int x){
        return fields[y][x];
    }
    public Piece[][] getBoard(){
        return fields;
    }

    public void setBoard(Piece[][] newFields){
        for(int y=0; y<10;y++){
            for(int x=0; x<10; x++){
                this.fields[y][x] = newFields[y][x];
            }
        }
    }
}
