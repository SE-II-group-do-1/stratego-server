package com.example.stratego.session;

class Move {
    int origY;
    int origX;
    int newY;
    int newX;
    Piece piece;

    Move(int origY, int origX, int newY, int newX, Piece piece) {
        this.origY = origY;
        this.origX = origX;
        this.newY = newY;
        this.newX = newX;
        this.piece = piece;
    }
}
