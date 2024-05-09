package com.example.stratego;

import com.example.stratego.session.Board;
import com.example.stratego.session.Piece;
import com.example.stratego.session.Player;
import com.example.stratego.session.Rank;
import com.example.stratego.session.SessionService;

import jakarta.websocket.Session;

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


}
