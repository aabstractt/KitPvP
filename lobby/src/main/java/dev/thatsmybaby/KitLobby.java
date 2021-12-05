package dev.thatsmybaby;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;

public class KitLobby extends PluginBase {

    @Getter
    private static KitLobby instance;

    @Override
    public void onEnable() {
        instance = this;
    }
}