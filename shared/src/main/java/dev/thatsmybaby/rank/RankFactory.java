package dev.thatsmybaby.rank;

import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.provider.PlayerStorage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankFactory {

    @Getter
    private final static RankFactory instance = new RankFactory();

    private final List<Rank> rankList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void init(List<Object> list) {
        for (Object data : list) {
            if (!(data instanceof Map)) {
                continue;
            }

            this.rankList.add(new Rank(TextFormat.colorize((String) ((Map<String, Object>) data).get("format")), (int) ((Map<String, Object>) data).get("minKills"), (int) ((Map<String, Object>) data).get("maxKills")));
        }
    }

    public boolean tryUpdateRank(PlayerStorage playerStorage) {
        Rank currentRank = getRank(playerStorage.getRankName());
        Rank newRank = getPlayerRank(playerStorage);

        if (currentRank == null || newRank == null || currentRank.getName().equals(newRank.getName())) {
            return false;
        }

        playerStorage.setRankName(newRank.getName());

        return true;
    }

    public Rank getRank(String rankName) {
        return this.rankList.stream().filter(rank -> rank.getName().equals(rankName)).findAny().orElse(null);
    }

    public Rank getPlayerRank(PlayerStorage playerStorage) {
        for (Rank rank : this.rankList) {
            if (rank.getMinKills() >= playerStorage.getTotalKills() && rank.getMaxKills() >= playerStorage.getTotalKills()) {
                return rank;
            }
        }

        return null;
    }
}