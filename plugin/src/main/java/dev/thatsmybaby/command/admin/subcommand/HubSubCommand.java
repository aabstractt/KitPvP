package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import dev.thatsmybaby.command.PlayerSubCommand;

public class HubSubCommand extends PlayerSubCommand {

    public HubSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player sender, String label, String[] args) {
        sender.teleport(Server.getInstance().getDefaultLevel().getSpawnLocation());
    }
}