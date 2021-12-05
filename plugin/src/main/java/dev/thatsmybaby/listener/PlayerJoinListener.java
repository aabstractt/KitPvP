package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();

        TaskUtils.runAsync(() -> {
            PlayerStorage playerStorage = MysqlProvider.getInstance().getPlayerStorage(player.getName());

            if (playerStorage == null) {
                playerStorage = new PlayerStorage(player.getName());
            }

            PlayerStorage.players.put(player.getName().toLowerCase(), playerStorage);
        });
    }
}