package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;

import java.util.ArrayList;

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
}
