package dev.thatsmybaby.command.admin;

import dev.thatsmybaby.command.Command;
import dev.thatsmybaby.command.admin.subcommand.HubSubCommand;
import dev.thatsmybaby.command.admin.subcommand.SpawnNpcSubCommand;
import dev.thatsmybaby.command.admin.subcommand.ZoneSubCommand;

public class AdminCommand extends Command {

    public AdminCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);

        addCommand(
                new SpawnNpcSubCommand("spawnnpc", "admin.command.spawnnpc"),
                new ZoneSubCommand("zone", "admin.command.zone"),
                new HubSubCommand("hub", "admin.command.hub")
        );
    }
}