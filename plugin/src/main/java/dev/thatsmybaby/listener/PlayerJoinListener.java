package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.rank.RankFactory;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();

        TaskUtils.runAsync(() -> {
            PlayerStorage playerStorage = MysqlProvider.getInstance().getPlayerStorage(player.getName());

            if (playerStorage == null) {
                playerStorage = new PlayerStorage(player.getName(), KitPvP.getInstance().getConfig().getString("default-rank"));
            }

            if (RankFactory.getInstance().tryUpdateRank(playerStorage)) {
                player.sendMessage(KitPvP.replacePlaceholders("NEW_RANK", "<new_rank>", playerStorage.getRankName()));
            }

            PlayerStorage.players.put(player.getName().toLowerCase(), playerStorage);

            TaskUtils.runLater(() -> player.teleport(Server.getInstance().getDefaultLevel().getSpawnLocation()), 10);
        });
    }
}