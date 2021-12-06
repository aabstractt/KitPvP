package dev.thatsmybaby.rank;

import dev.thatsmybaby.provider.PlayerStorage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankFactory {

    @Getter
    private final static RankFactory instance = new RankFactory();

    private final List<Rank> rankList = new ArrayList<>();

    public void init(List<Object> list) {
        for (Object data : list) {
            if (!(data instanceof Map)) {
                continue;
            }

            this.rankList.add(new Rank((String) ((Map<?, ?>) data).get("format"), (int) ((Map<?, ?>) data).get("minKills"), (int) ((Map<?, ?>) data).get("maxKills")));
        }
    }

    public void tryUpdateRank(PlayerStorage playerStorage) {
        Rank currentRank = getRank(playerStorage.getRankName());
        Rank newRank = getPlayerRank(playerStorage);

        if (currentRank.getName().equals(newRank.getName())) {
            return;
        }

        playerStorage.setRankName(newRank.getName());
    }

    public Rank getRank(String rankName) {
        return this.rankList.stream().filter(rank -> rank.getName().equals(rankName)).findAny().orElse(null);
    }

    public Rank getPlayerRank(PlayerStorage playerStorage) {
        for (Rank rank : this.rankList) {
            if (rank.getMinKills() > playerStorage.getTotalKills() && rank.getMaxKills() < playerStorage.getTotalKills()) {
                return rank;
            }
        }

        return null;
    }
}