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

    public static boolean isPieceMovable(Board board, Piece attackingPiece) {

        int[] coordinates = findPieceById(attackingPiece.getId(), board);

        if (coordinates == null) {
            return false;  // The piece is not found on the board
        }
        // Retrieve piece at point coordinates
        attackingPiece = board.getField(coordinates[0], coordinates[1]);

        // Check if the piece exists and is movable (not a flag, bomb, or lake)
        if (attackingPiece != null && attackingPiece.isMovable()) {
            // Check all four possible move directions
            return canMove(board, coordinates[0], coordinates[1]);
        }
        return false; // No piece at the position or the piece is not movable
    }

/*
Helper methods for isPieceMovable (findPieceById, canMove, checkMove)
 */
    /**
     * Finds the coordinates of a piece by its ID.
     * @param id  of the piece
     * @param board where the pieces are located
     * @return a Point representing the coordinates of the piece, or null if not found.
     */
    public static int[] findPieceById(int id, Board board) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece piece = board.getField(y, x);
                if (piece != null && piece.getId() == id) {
                    return new int[] {y, x};  // coordinates were attacking piece is located
                }
            }
        }
        return null;  // Return null if the piece is not found
    }

    private static boolean canMove(Board board, int y, int x) {
        // Check moves in four directions: up, down, left, right
        return checkMove(board, y - 1, x) ||
                checkMove(board, y + 1, x) ||
                checkMove(board, y, x - 1) ||
                checkMove(board, y, x + 1);
    }

    private static boolean checkMove(Board board, int newY, int newX) {
        if (newY < 0 || newY >= 10 || newX < 0 || newX >= 10) {
            return false; // Out of bounds
        }
        Piece pieceAtNewLocation = board.getField(newY, newX);
        // Ensure the move does not go into a square occupied by a lake or another piece of the same player
        return (pieceAtNewLocation == null || pieceAtNewLocation.getRank() != Rank.LAKE);
    }


    /**
     * Checks if an attacker wins or loses TODO Check if the defender loses too if there is equality
     */
    public static boolean fight(Piece attacker, Piece defender) {
        // Check if either piece is null (invalid scenario)
        if (attacker == null || defender == null) {
            throw new IllegalArgumentException("Some Piece is missing!");
        }

        // First handle special pieces interactions
        return handleSpecialCases(attacker, defender);
    }
    private static boolean handleSpecialCases(Piece attacker, Piece defender) {
        // Check for Spy attacking a Marshal
        if (attacker.getRank() == Rank.SPY && defender.getRank() == Rank.MARSHAL) {
            return true; // Spy wins when attacking Marshal
        }

        // Check for Miner attacking a Bomb
        if (attacker.getRank() == Rank.MINER && defender.getRank() == Rank.BOMB) {
            return true; // Miner defuses Bomb
        }

        // Bomb destroys any attacker except Miner
        if (defender.getRank() == Rank.BOMB) {
            return false; // Any piece other than Miner attacking a Bomb loses
        }

        // TODO think of more edge cases

        // If no special cases apply, fall back to general fight resolution
        return resolveGeneralFight(attacker, defender);
    }
    private static boolean resolveGeneralFight(Piece attacker, Piece defender) {
        // Retrieve values from the RankValues map
        int attackerValue = RankValues.getRankValue(attacker.getRank());
        int defenderValue = RankValues.getRankValue(defender.getRank());

        // Handle case where both have the same rank
        if (attackerValue == defenderValue) {
            return false; // Indicate that the fight results in a tie (both pieces are removed)
        }

        // Higher rank wins
        return attackerValue > defenderValue;
    }



}
