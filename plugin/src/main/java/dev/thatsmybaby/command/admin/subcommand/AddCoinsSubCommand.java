package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.SubCommand;
import dev.thatsmybaby.provider.PlayerStorage;

public class AddCoinsSubCommand extends SubCommand {

    public AddCoinsSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextFormat.RED + "Usage: /admin addcoins <player> <coins>");

            return;
        }

        Player target = Server.getInstance().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(TextFormat.RED + "Player not found");

            return;
        }

        PlayerStorage playerStorage = PlayerStorage.of(target);

        if (playerStorage == null) {
            sender.sendMessage(TextFormat.RED + "Player not found");

            return;
        }

        playerStorage.increaseCoins(Integer.parseInt(args[1]));

        sender.sendMessage(TextFormat.GREEN + "You added " + args[1] + " coins to " + playerStorage.getName());
    }
}