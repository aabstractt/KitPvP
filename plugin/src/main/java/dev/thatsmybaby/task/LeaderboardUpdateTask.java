package dev.thatsmybaby.task;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.entity.LeaderboardEntity;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardUpdateTask extends Task {

    @Override
    public void onRun(int i) {
        List<PlayerStorage> list = MysqlProvider.getInstance().getLeaderboard();

        list.sort((o1, o2) -> Integer.compare(o2.getTotalKills(), o1.getTotalKills()));

        StringBuilder text = new StringBuilder(KitPvP.replacePlaceholders("LEADERBOARD_TITLE"));
        int x = 1;

        for (PlayerStorage playerStorage : list) {
            text.append(KitPvP.replacePlaceholders("LEADERBOARD_PLAYER", "<position>", String.valueOf(x), "<player>", playerStorage.getName(), "<kills>", String.valueOf(playerStorage.getTotalKills())));

            x++;
        }

        for (Entity entity : Arrays.stream(Server.getInstance().getDefaultLevel().getEntities()).filter(entity -> entity instanceof LeaderboardEntity).collect(Collectors.toList())) {
            entity.setNameTag(text.toString());
        }
    }
}