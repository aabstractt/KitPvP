package dev.thatsmybaby.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public abstract class PlayerSubCommand extends SubCommand {

    public PlayerSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + "Run this command in-game");

            return;
        }

        this.execute((Player) sender, label, args);
    }

    public abstract void execute(Player sender, String label, String[] args);
}