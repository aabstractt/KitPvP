package dev.thatsmybaby;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.scoreboard.ScoreboardBuilder;

import java.util.List;

public class ScoreboardUpdateTask extends Task {

    public static ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(KitPvP.getInstance().replacePlaceholders("SCOREBOARD_TITLE"), "KitPvP", ScoreboardBuilder.SIDEBAR, ScoreboardBuilder.DESCENDING);

    private final List<String> worlds;

    public ScoreboardUpdateTask(List<String> worlds) {
        this.worlds = worlds;
    }

    @Override
    public void onRun(int i) {
        for (String world : this.worlds) {
            Level level = Server.getInstance().getLevelByName(world);

            if (level == null) {
                continue;
            }

            for (Player player : level.getPlayers().values()) {
                injectScoreboard(player, false);
            }
        }
    }

    public static void injectScoreboard(Player player, boolean add) {
        PlayerStorage playerStorage = PlayerStorage.of(player);

        if (playerStorage == null) {
            return;
        }

        if (add) {
            scoreboardBuilder.addPlayer(player);
        }

        List<String> list = KitPvP.getInstance().getConfig().getStringList("scoreboard");

        for (int i = 0; i < list.size(); i++) {
            scoreboardBuilder.setLine(i, KitPvP.getInstance().replacePlaceholders(list.get(i),
                    "<totalKills>", String.valueOf(playerStorage.getTotalKills()),
                    "<kills>", String.valueOf(playerStorage.getKills()),
                    "<deaths>", String.valueOf(playerStorage.getDeaths()),
                    "<killStreak>", String.valueOf(playerStorage.getKillStreak()),
                    "<betterKillStreak>", String.valueOf(playerStorage.getBetterKillStreak()),
                    "<world_online>", String.valueOf(player.getLevel().getPlayers().size()),
                    "<online>", String.valueOf(Server.getInstance().getOnlinePlayers().size())
            ), player);
        }
    }
}