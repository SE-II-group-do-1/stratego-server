package com.example.stratego.session;

import com.example.stratego.session.exceptions.InvalidPlayerTurnException;

public interface SessionServiceI {
    void updateBoard(int y, int x, Piece piece, Player initiator) throws InvalidPlayerTurnException;

    boolean checkOverlap(int y, int x);
    void setPieces(Board playerBoard);
}
