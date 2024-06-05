package com.example.stratego;

import java.util.HashMap;
import java.util.Map;
import com.example.stratego.session.*;

class FightOutcomes {
    private static final Map<String, Boolean> fightOutcomes = new HashMap<>();

    static {
        // Special cases and predefined outcomes
        addFightOutcome(Rank.SPY, Rank.MARSHAL, true);  // Spy beats Marshal
        addFightOutcome(Rank.MINER, Rank.BOMB, true);   // Miner beats Bomb

        // General outcomes based on rank values
        initializeGeneralOutcomes();
    }

    private static void addFightOutcome(Rank attacker, Rank defender, boolean outcome) {
        fightOutcomes.put(generateKey(attacker, defender), outcome);
    }

    private static String generateKey(Rank attacker, Rank defender) {
        return attacker.name() + "-" + defender.name();
    }

    private static void initializeGeneralOutcomes() {
        Rank[] ranks = Rank.values();
        for (Rank attacker : ranks) {
            for (Rank defender : ranks) {
                // Special case for BOMB: it wins against all except MINER
                if (defender == Rank.BOMB) {
                    if (attacker != Rank.MINER) {
                        addFightOutcome(attacker, defender, false); // Bomb wins against all except Miner
                    }
                } else if (!fightOutcomes.containsKey(generateKey(attacker, defender))) {
                    int attackerValue = RankValues.getRankValue(attacker);
                    int defenderValue = RankValues.getRankValue(defender);
                    if (attackerValue == defenderValue) {
                        addFightOutcome(attacker, defender, false); // Tie
                    } else {
                        addFightOutcome(attacker, defender, attackerValue > defenderValue);
                    }
                }
            }
        }
    }

    public static Boolean getFightOutcome(Rank attacker, Rank defender) {
        return fightOutcomes.get(generateKey(attacker, defender));
    }
}