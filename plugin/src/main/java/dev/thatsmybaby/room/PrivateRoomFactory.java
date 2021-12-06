package dev.thatsmybaby.room;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PrivateRoomFactory {

    @Getter
    private final static PrivateRoomFactory instance = new PrivateRoomFactory();

    @Getter
    private final Map<UUID, PrivateRoom> privateRoomMap = new HashMap<>();

    public void createPrivateRoom(Player player) {
        if (this.privateRoomMap.containsKey(player.getUniqueId())) {
            player.sendMessage(TextFormat.RED + "You already have a private room");

            return;
        }

        String worldName = KitPvP.getInstance().getConfig().getString("world-private-room", null);

        if (worldName == null) {
            player.sendMessage(TextFormat.RED + "World not found");

            return;
        }

        File file = new File(KitPvP.getInstance().getDataFolder(), worldName);

        if (!file.isDirectory()) {
            player.sendMessage(TextFormat.RED + "World directory not found");

            return;
        }

        String password = String.valueOf(new Random().nextInt());
        this.privateRoomMap.put(player.getUniqueId(), new PrivateRoom(player.getUniqueId(), player.getName(), password));

        player.sendMessage(TextFormat.GOLD + "Creando sala privada...");

        TaskUtils.runAsync(() -> {
            try {
                FileUtils.copyDirectory(file, new File(Server.getInstance().getDataPath() + "/worlds/" + password));

                Server.getInstance().loadLevel(password);

                player.teleport(Server.getInstance().getLevelByName(password).getSpawnLocation());
                player.sendMessage(TextFormat.GREEN + "Sala privada creada usando la contraseña " + TextFormat.AQUA + password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // TODO: Create world backup
    }

    public void closePrivateRoom(Player player) {
        PrivateRoom privateRoom = this.privateRoomMap.get(player.getUniqueId());

        if (privateRoom == null) {
            return;
        }

        privateRoom.close();
    }
}