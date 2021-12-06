package dev.thatsmybaby.kit;

import cn.nukkit.utils.Config;
import dev.thatsmybaby.KitPvP;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KitFactory {

    @Getter
    private final static KitFactory instance = new KitFactory();
    @Getter
    private final Map<String, Kit> kits = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void init() {
        File file = new File(KitPvP.getInstance().getDataFolder(), "kits.yml");

        if (!file.exists()) {
            return;
        }

        for (Map.Entry<String, Object> entry : (new Config(file, Config.YAML)).getAll().entrySet()) {
            KitPvP.getInstance().getLogger().info("Loading " + entry.getKey() + " kit...");

            this.addKit(Kit.deserialize(entry.getKey(), (Map<String, Map<Integer, String>>) entry.getValue()), false);
        }
    }

    public void addKit(Kit kit, boolean save) {
        this.kits.put(kit.getKitName(), kit);

        if (save) {
            Config config = new Config(new File(KitPvP.getInstance().getDataFolder(), "kits.yml"), Config.YAML);

            config.set(kit.getKitName(), kit.serialize());

            config.save();
        }
    }

    public Kit getKit(String kitName) {
        return this.kits.get(kitName);
    }
}