package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.entity.KitSelectorEntity;

public class SpawnNpcSubCommand extends PlayerSubCommand {

    public SpawnNpcSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player sender, String label, String[] args) {
        KitSelectorEntity entity = (KitSelectorEntity) Entity.createEntity("KitSelectorEntity", sender.getChunk(), Entity.getDefaultNBT(sender.getLocation(), null).putCompound("Skin", sender.namedTag.getCompound("Skin").clone()));

        entity.setNameTag(TextFormat.YELLOW + TextFormat.BOLD.toString() + "KIT SELECTOR");
        entity.setNameTagAlwaysVisible();
        entity.setNameTagVisible();

        entity.spawnToAll();

        sender.sendMessage(TextFormat.GREEN + "Npc spawned!");
    }
}