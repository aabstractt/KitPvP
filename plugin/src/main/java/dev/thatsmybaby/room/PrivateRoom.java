package dev.thatsmybaby.room;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class PrivateRoom {

    private final UUID owner;
    private final String ownerName;
    private final String password;

    private long waitingTime;

    public String getWorldName() {
        return this.password;
    }

    public Level getWorld() {
        return Server.getInstance().getLevelByName(this.password);
    }

    public Set<Player> getPlayers() {
        if (this.getWorld() == null) {
            return new HashSet<>();
        }

        return new HashSet<>(this.getWorld().getPlayers().values());
    }

    public boolean ownerInside() {
        Player player = Server.getInstance().getPlayer(this.owner).orElse(null);

        return player != null && player.getLevel().getFolderName().equals(this.getWorldName());
    }

    public void startWaitingTime() {
        this.startWaitingTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
    }

    public void startWaitingTime(long time) {
        this.waitingTime = time;
    }

    public void close(boolean async) {
        for (Player target : this.getPlayers()) {
            if (target.getName().equalsIgnoreCase(this.getOwnerName())) {
                continue;
            }

            target.sendMessage(KitPvP.replacePlaceholders("PRIVATE_ROOM_CLOSED", "<owner>", this.getOwnerName()));

            target.teleport(Server.getInstance().getDefaultLevel().getSpawnLocation());
        }

        PrivateRoomFactory.getInstance().getPrivateRoomMap().remove(this.getOwner());

        try {
            if (this.getWorld() != null) {
                Server.getInstance().unloadLevel(getWorld());
            }
        } catch (Exception ignored) {}

        File file = new File(Server.getInstance().getDataPath() + "/worlds/" + this.getWorldName());

        if (!file.isDirectory()) {
            return;
        }

        if (!async) {
            deleteDirectory(file);

            return;
        }

        TaskUtils.runAsync(() -> deleteDirectory(file));
    }

    private void deleteDirectory(File file) {
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}