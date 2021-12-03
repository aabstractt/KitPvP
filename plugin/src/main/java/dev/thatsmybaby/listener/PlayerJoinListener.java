package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();


    }
}
