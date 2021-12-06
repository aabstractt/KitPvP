package dev.thatsmybaby.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import dev.thatsmybaby.room.PrivateRoom;
import dev.thatsmybaby.room.PrivateRoomFactory;

public class PrivateRoomUpdateTask extends Task {

    @Override
    public void onRun(int i) {
        for (PrivateRoom privateRoom : PrivateRoomFactory.getInstance().getPrivateRoomMap().values()) {
            for (Player target : privateRoom.getPlayers()) {
                ScoreboardUpdateTask.injectScoreboard(target, false);
            }

            long timeWaiting = privateRoom.getWaitingTime();

            if (timeWaiting == -1 && !privateRoom.ownerInside()) {
                privateRoom.startWaitingTime();

                return;
            }

            if (timeWaiting == -1) {
                return;
            }

            if (privateRoom.ownerInside()) {
                privateRoom.startWaitingTime(-1);

                return;
            }

            if (System.currentTimeMillis() > privateRoom.getWaitingTime()) {
                privateRoom.close(true);
            }

            /*if (player == null || !player.getLevel().getFolderName().equalsIgnoreCase(privateRoom.getWorldName())) {
                privateRoom.close();

                continue;
            }*/
        }
    }
}