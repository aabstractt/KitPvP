package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.level.Level;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.task.ScoreboardUpdateTask;

import java.util.List;

public class EntityLevelChangeListener implements Listener {

    @EventHandler
    public void onEntityLevelChangeEvent(EntityLevelChangeEvent ev) {
        if (!(ev.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) ev.getEntity();

        Level level = ev.getOrigin();
        Level target = ev.getTarget();

        List<String> worlds = KitPvP.getInstance().getConfig().getStringList("worlds");

        if (worlds.contains(level.getFolderName())) {
            ScoreboardUpdateTask.scoreboardBuilder.removePlayer(player);
        }

        if (worlds.contains(target.getFolderName())) {
            ScoreboardUpdateTask.injectScoreboard(player, true);
        }

        System.out.println("Repeating");
    }
}