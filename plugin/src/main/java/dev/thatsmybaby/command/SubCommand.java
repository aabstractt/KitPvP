package dev.thatsmybaby.command;

import cn.nukkit.command.CommandSender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SubCommand {

    private String name;
    private String permission;

    public abstract void execute(CommandSender sender, String label, String[] args);
}