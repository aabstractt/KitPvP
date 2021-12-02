package dev.thatsmybaby.kit;

import cn.nukkit.utils.Config;
import com.google.gson.Gson;
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

    public void init() {
        File file = new File(KitPvP.getInstance().getDataFolder(), "kits.json");

        if (!file.exists()) {
            return;
        }

        for (Object kitObject : (new Config(file)).getAll().values()) {
            this.addKit(new Gson().fromJson(kitObject.toString(), Kit.class), false);
        }
    }

    public void addKit(Kit kit, boolean save) {
        this.kits.put(kit.getKitName(), kit);

        if (save) {
            Config config = new Config(new File(KitPvP.getInstance().getDataFolder(), "kits.json"));

            for (Kit kits : this.kits.values()) {
                config.set(kits.getKitName(), new Gson().toJson(kit));
            }

            config.save();
        }
    }

    public Kit getKit(String kitName) {
        return this.kits.get(kitName);
    }
}