package dev.thatsmybaby.command.admin;

import dev.thatsmybaby.command.Command;
import dev.thatsmybaby.command.admin.subcommand.*;

public class AdminCommand extends Command {

    public AdminCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);

        addCommand(
                new SpawnNpcSubCommand("spawnnpc", "admin.command.spawnnpc"),
                new ZoneSubCommand("zone", "admin.command.zone"),
                new HubSubCommand("hub", "admin.command.hub"),
                new DespawnNpcSubCommand("despawnnpc", "admin.command.despawnnpc"),
                new AddCoinsSubCommand("addcoins", "admin.command.addcoins"),
                new RemoveCoinsSubCommand("removecoins", "admin.command.removecoins"),
                new DropSubCommand("drop", "admin.command.drop")
        );
    }
}