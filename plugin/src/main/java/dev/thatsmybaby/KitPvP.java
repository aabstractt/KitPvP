package dev.thatsmybaby;

import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.kit.KitCommand;
import dev.thatsmybaby.command.admin.AdminCommand;
import dev.thatsmybaby.kit.KitFactory;
import dev.thatsmybaby.listener.PlayerFormRespondedListener;
import dev.thatsmybaby.listener.PlayerJoinListener;
import dev.thatsmybaby.entity.KitSelectorEntity;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KitPvP extends PluginBase {

    @Getter
    private static KitPvP instance;

    private final Map<String, String> messages = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.saveResource("messages.yml");

        Entity.registerEntity("KitSelectorEntity", KitSelectorEntity.class);

        for (Map.Entry<String, Object> entry : (new Config(new File(getDataFolder(), "messages.yml"), Config.YAML)).getAll().entrySet()) {
            this.messages.put(entry.getKey(), entry.getValue().toString());
        }

        KitFactory.getInstance().init();

        getServer().getCommandMap().register("admin", new AdminCommand("admin", "Admin commands", "", new String[0]));
        getServer().getCommandMap().register("kit", new KitCommand("kit", "Kit command", "", new String[0]));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFormRespondedListener(), this);
    }

    public String replacePlaceholders(String key, String... values) {
        String message = this.messages.getOrDefault(key, key);

        if (values.length >= 2) {
            for (int i = 0; i < values.length; i += 2) {
                try {
                    String k = values[i];
                    String v = values[i + 1];

                    message = message.replaceAll(k, v);
                } catch (Exception e) {
                    return "";
                }
            }
        }

        return TextFormat.colorize(message);
    }
}