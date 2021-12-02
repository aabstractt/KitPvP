package dev.thatsmybaby.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;

public class KitCommand extends Command {

    public KitCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(TextFormat.RED + "Run this command in-game");

            return false;
        }

        Player player = (Player) commandSender;

        if (args.length < 1) {
            player.sendMessage(TextFormat.RED + "Use /kit help");

            return false;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission("kit.command.create")) {
                player.sendMessage(TextFormat.RED + "You don't have permissions to use this command.");

                return false;
            }

            if (args.length < 2) {
                player.sendMessage(TextFormat.RED + "Usage: /kit create <kitName>");

                return false;
            }

            if (KitFactory.getInstance().getKit(args[1]) != null) {
                player.sendMessage(TextFormat.RED + "This kit already exists.");

                return false;
            }

            KitFactory.getInstance().addKit(new Kit(args[1], player.getInventory().getArmorContents(), player.getInventory().getContents().values().toArray(new Item[0])), true);

            player.sendMessage(TextFormat.GREEN + "Kit " + args[1] + " created!");

            return false;
        }

        if (args[0].equalsIgnoreCase("list")) {
            for (Kit kit : KitFactory.getInstance().getKits().values()) {
                player.sendMessage(TextFormat.GREEN + kit.getKitName());
            }
        }

        return false;
    }
}