package dev.thatsmybaby.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.room.PrivateRoom;
import dev.thatsmybaby.room.PrivateRoomFactory;

public class PrivateRoomUpdateTask extends Task {

    @Override
    public void onRun(int i) {
        for (PrivateRoom privateRoom : PrivateRoomFactory.getInstance().getPrivateRoomMap().values()) {
            Player player = Server.getInstance().getPlayer(privateRoom.getOwner()).orElse(null);

            if (player == null || !player.getLevel().getFolderName().equalsIgnoreCase(privateRoom.getWorldName())) {
                privateRoom.close();

                continue;
            }

            for (Player target : privateRoom.getPlayers()) {
                ScoreboardUpdateTask.injectScoreboard(target, false);
            }
        }
    }
}