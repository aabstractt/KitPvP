package dev.thatsmybaby.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent ev) {
        PlayerStorage playerStorage = PlayerStorage.of(ev.getPlayer());

        if (playerStorage == null) {
            return;
        }

        TaskUtils.runAsync(() -> {
            MysqlProvider.getInstance().savePlayerStorage(playerStorage);

            PlayerStorage.players.remove(playerStorage.getName().toLowerCase());
        });
    }
}