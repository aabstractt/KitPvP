package dev.thatsmybaby.command.admin.subcommand;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.zone.ZoneFactory;

import java.util.ArrayList;
import java.util.List;

public class ZoneSubCommand extends PlayerSubCommand {

    private List<Vector3> positions = new ArrayList<>();

    public ZoneSubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player sender, String label, String[] args) {
        this.positions.add(sender.getPosition());

        if (this.positions.size() == 2) {
            ZoneFactory.getInstance().addZone(this.positions.get(0), this.positions.get(1));

            sender.sendMessage(TextFormat.GREEN + "Zone was saved!");

            this.positions.clear();
            this.positions = new ArrayList<>();
        } else if (this.positions.size() > 2) {
            this.positions.clear();

            this.positions = new ArrayList<>();
        } else {
            sender.sendMessage(TextFormat.GOLD + "First position registered!");
        }
    }
}