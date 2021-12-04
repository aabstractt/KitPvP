package dev.thatsmybaby.command.kit;

import dev.thatsmybaby.command.Command;
import dev.thatsmybaby.command.kit.subcommand.CreateSubCommand;

public class KitCommand extends Command {

    public KitCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);

        addCommand(new CreateSubCommand("create", "kit.create"));
    }
}