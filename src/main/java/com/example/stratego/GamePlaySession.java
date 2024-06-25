    package com.example.stratego;

    import com.example.stratego.session.Board;
    import com.example.stratego.session.Color;
    import com.example.stratego.session.Piece;
    import com.example.stratego.session.Rank;


    public class GamePlaySession {

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
