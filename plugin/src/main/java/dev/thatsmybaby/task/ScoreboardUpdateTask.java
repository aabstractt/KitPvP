package dev.thatsmybaby.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.scoreboard.ScoreboardBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreboardUpdateTask extends Task {

    public static ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(KitPvP.replacePlaceholders("SCOREBOARD_TITLE"), "KitPvP", ScoreboardBuilder.SIDEBAR, ScoreboardBuilder.DESCENDING);

    public static List<String> worlds;

    public ScoreboardUpdateTask(List<String> worlds) {
        ScoreboardUpdateTask.worlds = worlds;
    }

    @Override
    public void onRun(int i) {
        for (String world : worlds) {
            Level level = Server.getInstance().getLevelByName(world);

            if (level == null) {
                continue;
            }

            for (Player player : level.getPlayers().values()) {
                injectScoreboard(player);
            }
        }
    }

    public static void injectScoreboard(Player player) {
        PlayerStorage playerStorage = PlayerStorage.of(player);

        if (playerStorage == null) {
            return;
        }

        scoreboardBuilder.removePlayer(player);
        scoreboardBuilder.addPlayer(player);

        List<String> lines = new ArrayList<>();
        List<String> list = KitPvP.getInstance().getConfig().getStringList("scoreboard");

        for (String line : list) {
            String finalLine = KitPvP.replacePlaceholders(line,
                    "<totalKills>", String.valueOf(playerStorage.getTotalKills()),
                    "<kills>", String.valueOf(playerStorage.getKills()),
                    "<deaths>", String.valueOf(playerStorage.getDeaths()),
                    "<killStreak>", String.valueOf(playerStorage.getKillStreak()),
                    "<betterKillStreak>", String.valueOf(playerStorage.getBetterKillStreak()),
                    "<world_online>", String.valueOf(player.getLevel().getPlayers().size()),
                    "<has_pvp>", Boolean.toString(!playerStorage.getAttackingName().equals("")),
                    "<fighting>", playerStorage.getAttackingName(),
                    "<time_left>", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(playerStorage.getLastAttackTime() - System.currentTimeMillis())),
                    "<online>", String.valueOf(Server.getInstance().getOnlinePlayers().size())
            );

            if (finalLine != null) {
                lines.add(finalLine);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            scoreboardBuilder.setLine(i, lines.get(i), player);
        }
    }
}