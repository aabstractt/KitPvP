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

import java.util.HashMap;
import java.util.Map;

public class GameSelectorEntity extends EntityHuman {

    public GameSelectorEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        source.setCancelled();

        if (source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
            Player target = (Player) ((EntityDamageByEntityEvent) source).getDamager();

            KitPvP instance = KitPvP.getInstance();

            Map<String, Map<String, Object>> map = instance.getConfig().get("type", new HashMap<>());

            if (map.isEmpty()) {
                return false;
            }

            FormWindowSimple formWindowSimple = new FormWindowSimple(instance.replacePlaceholders("GAME_SELECTOR_TITLE"), instance.replacePlaceholders("GAME_SELECTOR_CONTENT"));

            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Level level = Server.getInstance().getLevelByName(entry.getValue().get("world").toString());

                if (level == null) {
                    continue;
                }

                formWindowSimple.addButton(new ElementButton(instance.replacePlaceholders("GAME_BUTTON_" + entry.getKey().toUpperCase(), "<players_count>", String.valueOf(level.getPlayers().size()))));
            }

            formWindowSimple.addButton(new ElementButton(instance.replacePlaceholders("GAME_BUTTON_JOIN")));
            formWindowSimple.addButton(new ElementButton(instance.replacePlaceholders("GAME_BUTTON_PRIVATE")));

            target.showFormWindow(formWindowSimple);
        }

        return super.attack(source);
    }
}