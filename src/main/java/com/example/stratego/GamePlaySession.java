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
