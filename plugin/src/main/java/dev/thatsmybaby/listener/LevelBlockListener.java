package dev.thatsmybaby.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.*;
import cn.nukkit.event.level.ThunderChangeEvent;
import cn.nukkit.event.level.WeatherChangeEvent;

public class LevelBlockListener implements Listener {

    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockUpdateEvent(BlockUpdateEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockFallEvent(BlockFallEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onThunderChangeEvent(ThunderChangeEvent event) {
        event.setCancelled(true);
    }
}