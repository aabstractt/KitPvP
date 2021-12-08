package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.command.SubCommand;
import dev.thatsmybaby.provider.MysqlProvider;

public class DropSubCommand extends SubCommand {

    public DropSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        TaskUtils.runAsync(() -> MysqlProvider.getInstance().deleteKills());

        sender.sendMessage(TextFormat.GREEN + "Kills restarted");
    }
}