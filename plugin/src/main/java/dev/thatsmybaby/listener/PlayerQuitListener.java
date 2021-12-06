package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.room.PrivateRoomFactory;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();

        PlayerStorage playerStorage = PlayerStorage.of(player);

        if (playerStorage == null) {
            return;
        }

        PrivateRoomFactory.getInstance().closePrivateRoom(player);
        KitPvP.queueJoin.remove(player.getName().toLowerCase());

        TaskUtils.runAsync(() -> {
            MysqlProvider.getInstance().savePlayerStorage(playerStorage);

            PlayerStorage.players.remove(playerStorage.getName().toLowerCase());
        });
    }
}