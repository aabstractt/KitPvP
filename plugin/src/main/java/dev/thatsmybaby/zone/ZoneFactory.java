package dev.thatsmybaby.zone;

import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import dev.thatsmybaby.KitPvP;
import lombok.Getter;

import java.io.File;
import java.util.*;

public class ZoneFactory {

    @Getter
    private final static ZoneFactory instance = new ZoneFactory();

    private final List<Zone> zoneList = new ArrayList<>();

    public void init() {
        for (String worldName : KitPvP.getInstance().getConfig().getStringList("worlds")) {
            Server.getInstance().loadLevel(worldName);
        }

        File file = new File(KitPvP.getInstance().getDataFolder(), "zones.yml");

        if (!file.exists()) {
            return;
        }

        Config config = new Config(file, Config.YAML);

        for (String k : config.getKeys(false)) {
            this.zoneList.add(new Zone(mapToVector(config.get(k + ".min", new HashMap<>())), mapToVector(config.get(k + ".max", new HashMap<>()))));
        }
    }

    public boolean inZone(Vector3 currentVector) {
        for (Zone zone : this.zoneList) {
            if (zone.getMin() == null || zone.getMax() == null) {
                continue;
            }

            double maxX = Math.max(zone.getMax().x, zone.getMin().x);
            double maxY = Math.max(zone.getMax().y, zone.getMin().y);
            double maxZ = Math.max(zone.getMax().z, zone.getMin().z);

            double minX = Math.min(zone.getMax().x, zone.getMin().x);
            double minY = Math.min(zone.getMax().y, zone.getMin().y);
            double minZ = Math.min(zone.getMax().z, zone.getMin().z);

            if (
                    currentVector.getX() <= maxX
                    && currentVector.getX() >= minX
                    && currentVector.getY() <= maxY
                    && currentVector.getY() >= minY
                    && currentVector.getZ() <= maxZ
                    && currentVector.getZ() >= minZ
            ) {
                return true;
            }
        }

        return false;
    }

    public void addZone(Vector3 min, Vector3 max) {
        this.zoneList.add(new Zone(min, max));

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        int i = 0;

        for (Zone zone : this.zoneList) {
            map.put(String.valueOf(i), new HashMap<String, Map<String, Integer>>() {{
                put("min", vectorToMap(zone.getMin()));
                put("max", vectorToMap(zone.getMax()));
            }});
        }

        Config config = new Config(new File(KitPvP.getInstance().getDataFolder(), "zones.yml"), Config.YAML);

        config.setAll(map);
        config.save();
    }

    private Vector3 mapToVector(Map<String, Integer> map) {
        return new Vector3(map.get("x"), map.get("y"), map.get("z"));
    }

    private Map<String, Integer> vectorToMap(Vector3 vector3) {
        return new HashMap<String, Integer>() {{
            put("x", vector3.getFloorX());
            put("y", vector3.getFloorY());
            put("z", vector3.getFloorZ());
        }};
    }
}