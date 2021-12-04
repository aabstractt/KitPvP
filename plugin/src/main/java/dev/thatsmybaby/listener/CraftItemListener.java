package dev.thatsmybaby.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.CraftItemEvent;

public class CraftItemListener implements Listener {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent ev) {
        ev.setCancelled();
    }
}
