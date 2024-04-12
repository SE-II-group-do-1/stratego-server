package com.example.stratego.session;

public interface SessionServiceI {
    void updateBoard(int y, int x, Piece piece, Player initiator) throws InvalidPlayerTurnException;

    boolean checkOverlap(int y, int x);
    void setPieces(Board playerBoard);
}
