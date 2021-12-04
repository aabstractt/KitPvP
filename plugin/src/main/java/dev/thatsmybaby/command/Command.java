package dev.thatsmybaby.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Command extends cn.nukkit.command.Command {

    private final Map<String, SubCommand> commandMap = new HashMap<>();

    public Command(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
    }

    protected void addCommand(SubCommand... commands) {
        for (SubCommand command : commands) {
            this.commandMap.put(command.getName().toLowerCase(), command);
        }
    }

    protected SubCommand getCommand(String label) {
        return this.commandMap.get(label.toLowerCase());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (this.commandMap.size() != 0 && args.length < 1) {
            sender.sendMessage(TextFormat.RED + "Use /" + label + " help");

            return false;
        }

        SubCommand command = getCommand(args[0]);

        if (command == null) {
            sender.sendMessage(TextFormat.RED + "Use / " + label + " help");

            return false;
        }

        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            sender.sendMessage(TextFormat.RED + "You don't have permissions to use this command.");

            return false;
        }

        command.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));

        return false;
    }
}