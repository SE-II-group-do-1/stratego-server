package com.example.stratego;

import com.example.stratego.session.Board;
import com.example.stratego.session.Color;
import com.example.stratego.session.Piece;
import com.example.stratego.session.Rank;


public class GamePlaySession {


    /**
     * Checks the board state to determine if a player has captured a flag of the opponent
     * @param board game board
     * @param targetFlagColor color of the flag
     * @return true if the flag of the specified color has been captured, false otherwise.
     */
    public static boolean checkFlagCaptured(Board board, Color targetFlagColor) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getRank() == Rank.FLAG && piece.getColor() == targetFlagColor) {
                    return true; // Flag of the specified color has been captured
                }
            }
        }
        return false; // No flag of the specified color captured
    }


    /**
     * Checks if a player has any pieces that can legally move.
     */
    public static boolean hasMovablePieces(Board board, Color targetColor) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getColor() == targetColor && piece.isMovable()) {
                    // check if piece can move
                    if (canMove(board, y, x)) {
                        return true; // yes move possible
                    }
                }
            }
        }
        return false; // No move possible
    }

    private static boolean canMove(Board board, int y, int x) {
        // Check moves in four directions: up, down, left, right
        return checkMove(board, y - 1, x) || checkMove(board, y + 1, x) ||
                checkMove(board, y, x - 1) || checkMove(board, y, x + 1);
    }

    private static boolean checkMove(Board board, int newY, int newX) {
        if (newY < 0 || newY >= 10 || newX < 0 || newX >= 10) {
            return false; // Out of bounds
        }
        Piece pieceAtNewLocation = board.getField(newY, newX);
        // Ensure the move does not go into a square occupied by a lake or another piece of the same player
        return (pieceAtNewLocation == null || pieceAtNewLocation.getRank() != Rank.LAKE);
    }



}
