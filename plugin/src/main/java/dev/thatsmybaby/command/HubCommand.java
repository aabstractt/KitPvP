package dev.thatsmybaby.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class HubCommand extends Command {

    public HubCommand(String name) {
        super(name, "", "", new String[]{"lobby"});
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(TextFormat.RED + "Run this command in-game");

            return false;
        }

        ((Player) commandSender).teleport(Server.getInstance().getDefaultLevel().getSpawnLocation());

        return false;
    }
}
