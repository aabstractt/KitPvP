package dev.thatsmybaby;

import cn.nukkit.plugin.PluginBase;
import dev.thatsmybaby.command.KitCommand;
import dev.thatsmybaby.kit.KitFactory;
import lombok.Getter;

public class KitPvP extends PluginBase {

    @Getter
    private static KitPvP instance;

    @Override
    public void onEnable() {
        instance = this;

        KitFactory.getInstance().init();

        getServer().getCommandMap().register("kit", new KitCommand("kit"));
    }
}