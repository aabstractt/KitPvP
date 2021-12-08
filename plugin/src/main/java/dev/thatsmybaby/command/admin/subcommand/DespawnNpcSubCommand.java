package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.entity.GameSelectorEntity;
import dev.thatsmybaby.entity.KitSelectorEntity;
import dev.thatsmybaby.entity.LeaderboardEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DespawnNpcSubCommand extends PlayerSubCommand {

    public DespawnNpcSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player player, String label, String[] args) {
        if (args.length < 1) {
            player.sendMessage(TextFormat.RED + "Usage: /admin despawnnpc <npc>");

            return;
        }

        Class<?> clazz = null;

        if (args[0].equalsIgnoreCase("game")) {
            clazz = GameSelectorEntity.class;
        } else if (args[0].equalsIgnoreCase("leaderboard")) {
            clazz = LeaderboardEntity.class;
        } else if (args[0].equalsIgnoreCase("kit")) {
            clazz = KitSelectorEntity.class;
        }

        if (clazz == null) {
            player.sendMessage(TextFormat.RED + "Entity type " + args[0] + " not found");

            return;
        }

        Class<?> finalClazz = clazz;
        for (Entity entity : Arrays.stream(player.getLevel().getEntities()).filter(entity -> entity.getClass().isAssignableFrom(finalClazz)).collect(Collectors.toList())) {
            entity.kill();
        }
    }
}