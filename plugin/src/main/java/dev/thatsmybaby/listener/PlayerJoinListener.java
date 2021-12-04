package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.LoginChainData;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerJoinListener implements Listener {

    private final Map<String, String> playerType = new HashMap<>();

    @EventHandler
    public void onPlayerAsyncPreLoginEvent(PlayerAsyncPreLoginEvent ev) {
        LoginChainData loginChainData = ev.getChainData();
        String type = null;

        if (Arrays.asList(1, 2, 4, 12).contains(loginChainData.getDeviceOS()) && loginChainData.getCurrentInputMode() == 2) {
            type = "Mobile";
        } else if (Arrays.asList(11, 13).contains(loginChainData.getDeviceOS()) || loginChainData.getCurrentInputMode() == 3) {
            type = "Console";
        } else if (Arrays.asList(3, 9).contains(loginChainData.getDeviceOS()) || loginChainData.getCurrentInputMode() == 1) {
            type = "PC";
        }

        if (type != null) {
            this.playerType.put(loginChainData.getUsername(), type);

            return;
        }

        ev.setKickMessage(TextFormat.RED + "Type not found");

        ev.setLoginResult(PlayerAsyncPreLoginEvent.LoginResult.KICK);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();

        String world = KitPvP.getInstance().getConfig().get("type." + this.playerType.get(player.getName()), null);

        if ((world == null || !Server.getInstance().isLevelLoaded(world)) && !player.hasPermission("kitpvp.forceaccess")) {
            TaskUtils.runLater(() -> player.kick(TextFormat.RED + "World not found"), 20);

            return;
        }

        if (world != null) {
            player.teleport(Server.getInstance().getLevelByName(world).getSpawnLocation());
        }

        player.getInventory().clearAll();

        player.extinguish();
        player.setGamemode(2);
        player.removeAllEffects();
        player.setExperience(0, 0);

        player.setHealth(player.getMaxHealth());
        player.getFoodData().reset();
        player.setFoodEnabled(false);
    }
}