package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.room.PrivateRoom;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChatEvent(PlayerChatEvent ev) {
        Player player = ev.getPlayer();

        String message = ev.getMessage();

        PrivateRoom privateRoom = KitPvP.queueJoin.getOrDefault(player.getName().toLowerCase(), null);

        if (privateRoom == null) {
            return;
        }

        if (!privateRoom.getPassword().equals(message)) {
            player.sendMessage(KitPvP.replacePlaceholders("PASSWORD_INCORRECT", "<password>", message));
        } else {
            player.sendMessage(KitPvP.replacePlaceholders("PASSWORD_CORRECT"));

            if (privateRoom.getWorld() != null) {
                player.teleport(privateRoom.getWorld().getSpawnLocation());
            }
        }

        KitPvP.queueJoin.remove(player.getName().toLowerCase());

        ev.setCancelled();
    }
}
