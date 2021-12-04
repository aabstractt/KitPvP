package dev.thatsmybaby;

import cn.nukkit.Server;

public class TaskUtils {

    public static void runLater(Runnable runnable, int delay) {
        Server.getInstance().getScheduler().scheduleDelayedTask(KitPvP.getInstance(), runnable, delay);
    }
}