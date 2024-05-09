package com.example.stratego;

import com.example.stratego.session.Board;
import com.example.stratego.session.Piece;
import com.example.stratego.session.Player;
import com.example.stratego.session.Rank;


public class GamePlaySession {

    private Board board = new Board();

    /**
     *
     */
    public boolean checkFlagCaptured() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getRank() == Rank.FLAG) {
                    return false; // Flag still on board
                }
            }
        }
        return true; //flag captured
    }

    /**
     *
     */

    public boolean hasMovablePieces(Player player) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getColor() == player.getId() && piece.isMovable()) {//piece.getId() == player.getId() how to match them?
                    // check squares at boarder if move possible
                    if (canMove(y, x)) return true;
                }
            }
        }
        return false;
    }

    private boolean canMove(int y, int x) {
        // Check moves in directions up, down, left, right
        // and ensure move does not go out of bounds or into lakes
        return checkMove(y - 1, x) || checkMove(y + 1, x) || checkMove(y, x - 1) || checkMove(y, x + 1);
    }

    private boolean checkMove(int y, int x) {
        if (y < 0 || y >= 10 || x < 0 || x >= 10) return false; // Out of bounds
        Piece piece = board.getField(y, x);
        return (piece == null || (piece.getRank() != Rank.LAKE && piece.getColor() != currentTurn)); // der der gerade gezogen hat darf nicht ziehen
    }


}
