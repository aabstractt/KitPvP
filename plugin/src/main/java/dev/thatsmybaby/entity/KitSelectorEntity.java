package dev.thatsmybaby.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;

import java.util.Map;

public class KitSelectorEntity extends EntityHuman {

    public KitSelectorEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        source.setCancelled();

        if (source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
            Player target = (Player) ((EntityDamageByEntityEvent) source).getDamager();

            Map<String, Kit> kits = KitFactory.getInstance().getKits();

            if (kits.isEmpty()) {
                target.sendMessage(KitPvP.replacePlaceholders("KITS_NOT_FOUND"));

                return false;
            }

            FormWindowSimple formWindowSimple = new FormWindowSimple(KitPvP.replacePlaceholders("KIT_WINDOW_TITLE"), KitPvP.replacePlaceholders("KIT_WINDOW_CONTENT"));

            for (Kit kit : kits.values()) {
                formWindowSimple.addButton(
                        new ElementButton(KitPvP.replacePlaceholders("KIT_BUTTON", "<kit_name>", kit.getKitName()),
                        new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, KitPvP.getInstance().getConfig().get("kit_image." + kit.getKitName(), "apple")))
                );
            }

            target.showFormWindow(formWindowSimple);
        }

        return super.attack(source);
    }
}