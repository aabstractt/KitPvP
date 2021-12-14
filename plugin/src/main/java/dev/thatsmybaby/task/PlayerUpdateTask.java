package dev.thatsmybaby.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.rank.Rank;

public class PlayerUpdateTask extends Task {

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            PlayerStorage playerStorage = PlayerStorage.of(player);

            if (playerStorage == null) {
                continue;
            }

            Rank rank = playerStorage.getRank();

            if (rank == null) {
                continue;
            }

            player.setNameTag(KitPvP.replacePlaceholders("PLAYER_NAME_TAG", "<player>", player.getName(), "<kills_rank>", rank.getFormat(), "<health>", String.valueOf(player.getHealth()), "<device>", KitPvP.getDeviceAsString(player.getLoginChainData().getDeviceOS()), "<input>", KitPvP.getInputAsString(player.getLoginChainData().getCurrentInputMode())));
        }
    }
}