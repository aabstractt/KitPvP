package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerFormRespondedListener implements Listener {

    @EventHandler
    public void onPlayerFormRespondedEvent(PlayerFormRespondedEvent ev) {
        if (ev.wasClosed()) {
            return;
        }

        Player player = ev.getPlayer();

        if (!(ev.getWindow() instanceof FormWindowSimple)) {
            return;
        }

        if (!((FormWindowSimple) ev.getWindow()).getTitle().equals(KitPvP.getInstance().replacePlaceholders("KIT_WINDOW_TITLE"))) {
            return;
        }

        try {
            Kit kit = new ArrayList<>(KitFactory.getInstance().getKits().values()).get(((FormResponseSimple) ev.getResponse()).getClickedButtonId());

            player.getInventory().clearAll();
            player.getInventory().setContents(kit.getItems());

            player.sendMessage(KitPvP.getInstance().replacePlaceholders("KIT_SELECTED", "<kit_name>", kit.getKitName()));
        } catch (Exception e) {
            player.sendMessage(TextFormat.RED + "Kit not found");
        }
    }

    @EventHandler
    @SuppressWarnings("unchecked")
    public void onPlayerFormRespondedEvent0(PlayerFormRespondedEvent ev) {
        if (ev.wasClosed()) {
            return;
        }

        Player player = ev.getPlayer();

        if (!(ev.getWindow() instanceof FormWindowSimple)) {
            return;
        }

        if (!((FormWindowSimple) ev.getWindow()).getTitle().equals(KitPvP.getInstance().replacePlaceholders("GAME_SELECTOR_TITLE"))) {
            return;
        }

        Map<String, Map<String, Object>> map = KitPvP.getInstance().getConfig().get("type", new HashMap<>());

        if (map.isEmpty()) {
            return;
        }

        Map<String, Object> typeData = new ArrayList<>(map.values()).get(((FormResponseSimple) ev.getResponse()).getClickedButtonId());
        List<String> list = (List<String>) typeData.get("device");

        if (!list.contains(KitPvP.getDeviceAsString(player.getLoginChainData().getDeviceOS())) && !list.contains(KitPvP.getInputAsString(player.getLoginChainData().getCurrentInputMode()))) {
            player.sendMessage(KitPvP.getInstance().replacePlaceholders("INVALID_DEVICE", "<device>", String.join(", ", list.toArray(new String[0]))));

            return;
        }

        String world = typeData.get("world").toString();

        if ((world == null || !Server.getInstance().isLevelLoaded(world)) && !player.hasPermission("kitpvp.forceaccess")) {
            TaskUtils.runLater(() -> player.kick(TextFormat.RED + "World not found"), 20);

            return;
        }

        KitPvP.defaultValues(player, Server.getInstance().getLevelByName(world));
    }
}