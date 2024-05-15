package com.example.stratego.session;

import com.example.stratego.session.exceptions.InvalidPlayerTurnException;

public interface SessionServiceI {
    void updateBoard(Board board, int initiator) throws InvalidPlayerTurnException;

    boolean checkOverlap(Piece oldPiece, Piece newPiece, int y, int x);
}
