package com.example.stratego;

import java.util.HashMap;
import java.util.Map;
import com.example.stratego.session.*;

class RankValues {
    private static final Map<Rank, Integer> getRankValues = new HashMap<>();

    static {
        getRankValues.put(Rank.MARSHAL, 10);
        getRankValues.put(Rank.GENERAL, 9);
        getRankValues.put(Rank.COLONEL, 8);
        getRankValues.put(Rank.MAJOR, 7);
        getRankValues.put(Rank.CAPTAIN, 6);
        getRankValues.put(Rank.LIEUTENANT, 5);
        getRankValues.put(Rank.SERGEANT, 4);
        getRankValues.put(Rank.MINER, 3);
        getRankValues.put(Rank.SCOUT, 2);
        getRankValues.put(Rank.SPY, 1);
        getRankValues.put(Rank.FLAG, 0);
        getRankValues.put(Rank.BOMB, -1);
        getRankValues.put(Rank.LAKE, -2);
    }

    public static int getRankValue(Rank rank) {
        return getRankValues.getOrDefault(rank, -99);
    }
}