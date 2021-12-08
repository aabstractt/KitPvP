package dev.thatsmybaby.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.task.ScoreboardUpdateTask;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameSelectorEntity extends EntityHuman {

    public GameSelectorEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        source.setCancelled();

        if (source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
            Player target = (Player) ((EntityDamageByEntityEvent) source).getDamager();

            Map<String, Map<String, Object>> map = KitPvP.getInstance().getConfig().get("type", new HashMap<>());

            if (map.isEmpty()) {
                return false;
            }

            FormWindowSimple formWindowSimple = new FormWindowSimple(KitPvP.replacePlaceholders("GAME_SELECTOR_TITLE"), KitPvP.replacePlaceholders("GAME_SELECTOR_CONTENT"));

            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Level level = Server.getInstance().getLevelByName(entry.getValue().get("world").toString());

                if (level == null) {
                    continue;
                }

                formWindowSimple.addButton(new ElementButton(KitPvP.replacePlaceholders("GAME_BUTTON_" + entry.getKey().toUpperCase(), "<players_count>", String.valueOf(level.getPlayers().size()))));
            }

            formWindowSimple.addButton(new ElementButton(KitPvP.replacePlaceholders("GAME_BUTTON_JOIN")));
            formWindowSimple.addButton(new ElementButton(KitPvP.replacePlaceholders("GAME_BUTTON_PRIVATE")));

            target.showFormWindow(formWindowSimple);
        }

        return super.attack(source);
    }

    public void updateNameTag() {
        int playersCount = 0;

        for (Level level : Server.getInstance().getLevels().values().stream().filter(level -> ScoreboardUpdateTask.worlds.contains(level.getFolderName())).collect(Collectors.toList())) {
            playersCount += level.getPlayers().size();
        }

        this.setNameTag(KitPvP.replacePlaceholders("GAME_SELECTOR_ENTITY", "<players_count>", String.valueOf(playersCount)));
    }

    @Override
    public boolean onUpdate(int currentTick) {
        this.updateNameTag();

        return super.onUpdate(currentTick);
    }
}