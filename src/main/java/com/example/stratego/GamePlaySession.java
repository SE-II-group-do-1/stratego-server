package com.example.stratego;

import com.example.stratego.session.Board;
import com.example.stratego.session.Color;
import com.example.stratego.session.Piece;
import com.example.stratego.session.Player;
import com.example.stratego.session.Rank;
import com.example.stratego.session.SessionService;


public class GamePlaySession {

    private Board board;
    private SessionService session;
    private Player currentPlayer;
    private Color playerColor;

    public GamePlaySession(Player player1, Player player2) {
        this.board = new Board();
        this.session = new SessionService(player1);  // Initialize the session with the first player
        session.setPlayerRed(player2);               // Set the second player
        this.currentPlayer = session.getCurrentTurn();
        this.playerColor = currentPlayer.getColor();// how to get the color?
    }

    /**
     * checks the board state to determine if a player has captured the flag
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
     * check if a specific player in the game has any pieces that can legally move
     */

    public boolean hasMovablePieces() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getColor() == playerColor && piece.isMovable()) {
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
        return (piece == null || (piece.getRank() != Rank.LAKE && piece.getColor() != playerColor));
    }


}
