package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.entity.GameSelectorEntity;
import dev.thatsmybaby.entity.KitSelectorEntity;

public class SpawnNpcSubCommand extends PlayerSubCommand {

    public SpawnNpcSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player sender, String label, String[] args) {
        CompoundTag nbt = Entity.getDefaultNBT(sender.getLocation(), null).putCompound("Skin", sender.namedTag.getCompound("Skin").clone());
        EntityHuman entity = null;

        if (args[0].equalsIgnoreCase("kit")) {
            entity = (KitSelectorEntity) Entity.createEntity("KitSelectorEntity", sender.getChunk(), nbt);

            entity.setNameTag(TextFormat.YELLOW + TextFormat.BOLD.toString() + "KIT SELECTOR");
        } else if (args[0].equalsIgnoreCase("game")) {
            entity = (GameSelectorEntity) Entity.createEntity("GameSelectorEntity", sender.getChunk(), nbt);

            entity.setNameTag(TextFormat.YELLOW + TextFormat.BOLD.toString() + "GAME SELECTOR");
        }

        if (entity == null) {
            sender.sendMessage(TextFormat.RED + "Entity " + args[0] + " not found");

            return;
        }

        entity.setNameTagAlwaysVisible();
        entity.setNameTagVisible();

        entity.spawnToAll();

        sender.sendMessage(TextFormat.GREEN + "Npc spawned!");
    }
}