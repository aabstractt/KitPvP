package dev.thatsmybaby.rank;

import dev.thatsmybaby.provider.PlayerStorage;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankFactory {

    @Getter
    private final static RankFactory instance = new RankFactory();

    private final Map<String, Rank> rankMap = new HashMap<>();

    public void init(Map<String, List<Integer>> data) {
        for (Map.Entry<String, List<Integer>> entry : data.entrySet()) {
            this.rankMap.put(entry.getKey(), new Rank(entry.getKey(), entry.getValue().get(0), entry.getValue().get(1)));
        }
    }

    public Rank getPlayerCurrentRank(PlayerStorage playerStorage) {
        for (Rank rank : this.rankMap.values()) {
            if (rank.getMinKills() > playerStorage.getTotalKills() && rank.getMaxKills() < playerStorage.getTotalKills()) {
                return rank;
            }
        }

        return null;
    }
}