    package com.example.stratego;

    import com.example.stratego.session.Board;
    import com.example.stratego.session.Color;
    import com.example.stratego.session.Piece;
    import com.example.stratego.session.Rank;


    public class GamePlaySession {
        /**
         * Checks the board state to determine if a player has captured a flag of the opponent
         * @param board game board
         * @param attackColor color of the attacker
         * @param x-coordinate of target piece
         * @param y-coordinate of target piece
         * @return true if the flag of the specified color has been captured, false otherwise.
         */
        public static boolean checkFlagCaptured(Board board, Color attackColor, int y, int x) {
            Piece targetPiece = board.getField(y, x);
            if (targetPiece != null && targetPiece.getRank() == Rank.FLAG && targetPiece.getColor() != attackColor) {
                return true; // The flag has been captured because it is of the opposite color and is attacked directly
            }
            return false;
        }


        /**
         * Checks if an attacker wins or loses TODO Check if the defender loses too if there is equality
         */
        public static boolean fight(Piece attacker, Piece defender) {
            // Check if either piece is null (invalid scenario)
            if (attacker == null || defender == null) {
                throw new IllegalArgumentException("Some Piece is missing!");
            }

            Boolean outcome = FightOutcomes.getFightOutcome(attacker.getRank(), defender.getRank());
            if (outcome != null) {
                if(!outcome) {
                    defender.setVisible(true);
                }
                return outcome;
            }

            // Default case, should never reach here if all cases are covered
            throw new IllegalStateException("Fight outcome not defined for the given ranks.");
        }

    }
