package dev.thatsmybaby.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.provider.PlayerStorage;

public class PlayerUpdateTask extends Task {

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            PlayerStorage playerStorage = PlayerStorage.of(player);

            if (playerStorage == null) {
                continue;
            }

            player.setNameTag(KitPvP.replacePlaceholders("PLAYER_NAME_TAG", "<player>", player.getName(), "<kills_rank>", playerStorage.getRank().getFormat(), "<health>", String.valueOf(player.getHealth()), "<device>", KitPvP.getDeviceAsString(player.getLoginChainData().getDeviceOS())));
        }
    }
}