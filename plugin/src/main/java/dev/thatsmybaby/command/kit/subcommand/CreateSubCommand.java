package dev.thatsmybaby.command.kit.subcommand;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;

public class CreateSubCommand extends PlayerSubCommand {

    public CreateSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player player, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(TextFormat.RED + "Usage: /kit create <kitName> <price>");

            return;
        }

        if (KitFactory.getInstance().getKit(args[0]) != null) {
            player.sendMessage(TextFormat.RED + "This kit already exists.");

            return;
        }

        KitFactory.getInstance().addKit(new Kit(args[0], player.getInventory().getContents(), Integer.parseInt(args[1])), true);

        player.sendMessage(TextFormat.GREEN + "Kit " + args[0] + " created!");
    }
}