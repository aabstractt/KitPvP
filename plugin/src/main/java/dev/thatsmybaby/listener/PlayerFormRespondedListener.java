package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.room.PrivateRoom;
import dev.thatsmybaby.room.PrivateRoomFactory;

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

        if (!((FormWindowSimple) ev.getWindow()).getTitle().equals(KitPvP.replacePlaceholders("KIT_WINDOW_TITLE"))) {
            return;
        }

        try {
            Kit kit = new ArrayList<>(KitFactory.getInstance().getKits().values()).get(((FormResponseSimple) ev.getResponse()).getClickedButtonId());

            if (!PlayerStorage.of(player).getKits().contains(kit.getKitName())) {
                Server.getInstance().dispatchCommand(player, "kit buy " + kit.getKitName());

                return;
            }

            player.getInventory().clearAll();
            player.getInventory().setContents(kit.getItems());

            player.sendMessage(KitPvP.replacePlaceholders("KIT_SELECTED", "<kit_name>", kit.getKitName()));
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

        if (!((FormWindowSimple) ev.getWindow()).getTitle().equals(KitPvP.replacePlaceholders("GAME_SELECTOR_TITLE"))) {
            return;
        }

        FormResponseSimple responseSimple = (FormResponseSimple) ev.getResponse();

        if (responseSimple.getClickedButton().getText().equals(KitPvP.replacePlaceholders("GAME_BUTTON_JOIN"))) {
            player.showFormWindow(showListPrivateGames());

            return;
        }

        if (responseSimple.getClickedButton().getText().equals(KitPvP.replacePlaceholders("GAME_BUTTON_PRIVATE"))) {
            PrivateRoomFactory.getInstance().createPrivateRoom(player);

            return;
        }

        Map<String, Map<String, Object>> map = KitPvP.getInstance().getConfig().get("type", new HashMap<>());

        if (map.isEmpty()) {
            return;
        }

        Map<String, Object> typeData = new ArrayList<>(map.values()).get(responseSimple.getClickedButtonId());
        List<String> list = (List<String>) typeData.get("device");

        if (!list.contains(KitPvP.getDeviceAsString(player.getLoginChainData().getDeviceOS())) && !list.contains(KitPvP.getInputAsString(player.getLoginChainData().getCurrentInputMode()))) {
            player.sendMessage(KitPvP.replacePlaceholders("INVALID_DEVICE", "<device>", String.join(", ", list.toArray(new String[0]))));

            return;
        }

        String world = typeData.get("world").toString();

        if ((world == null || !Server.getInstance().isLevelLoaded(world)) && !player.hasPermission("kitpvp.forceaccess")) {
            TaskUtils.runLater(() -> player.kick(TextFormat.RED + "World not found"), 20);

            return;
        }

        KitPvP.defaultValues(player, Server.getInstance().getLevelByName(world));
    }

    @EventHandler
    public void onPlayerFormRespondedEvent1(PlayerFormRespondedEvent ev) {
        if (ev.wasClosed()) {
            return;
        }

        Player player = ev.getPlayer();

        if (!(ev.getWindow() instanceof FormWindowSimple)) {
            return;
        }

        if (!((FormWindowSimple) ev.getWindow()).getTitle().equals(KitPvP.replacePlaceholders("FORM_PRIVATE_GAMES_TITLE"))) {
            return;
        }

        FormResponseSimple responseSimple = (FormResponseSimple) ev.getResponse();

        try {
            PrivateRoom privateRoom = new ArrayList<>(PrivateRoomFactory.getInstance().getPrivateRoomMap().values()).get(responseSimple.getClickedButtonId());

            if (privateRoom == null || privateRoom.getWorld() == null) {
                player.sendMessage(TextFormat.RED + "Private room not found");

                return;
            }

            player.sendMessage(KitPvP.replacePlaceholders("TYPE_PASSWORD_CHAT", "<private_room>", privateRoom.getOwnerName()));

            KitPvP.queueJoin.put(player.getName().toLowerCase(), privateRoom);
        } catch (Exception ignored) {}
    }

    private FormWindowSimple showListPrivateGames() {
        FormWindowSimple formWindowSimple = new FormWindowSimple(KitPvP.replacePlaceholders("FORM_PRIVATE_GAMES_TITLE"), KitPvP.replacePlaceholders("FORM_PRIVATE_GAMES_CONTENT"));

        for (PrivateRoom privateRoom : PrivateRoomFactory.getInstance().getPrivateRoomMap().values()) {
            formWindowSimple.addButton(new ElementButton(KitPvP.replacePlaceholders("FORM_PRIVATE_GAME_BUTTON", "<name>", privateRoom.getOwnerName(), "<players_count>", String.valueOf(privateRoom.getPlayers().size()))));
        }

        return formWindowSimple;
    }
}