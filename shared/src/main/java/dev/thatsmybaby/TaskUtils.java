package dev.thatsmybaby;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

public class TaskUtils {

    public static PluginBase plugin;

    public static void runAsync(Runnable runnable) {
        if (Server.getInstance().isPrimaryThread()) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }
    }

    public static void runLater(Runnable runnable, int delay) {
        Server.getInstance().getScheduler().scheduleDelayedTask(plugin, runnable, delay);
    }
}