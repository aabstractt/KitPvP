package dev.thatsmybaby.provider;

import cn.nukkit.Player;
import cn.nukkit.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class PlayerStorage {

    public final static Map<String, PlayerStorage> players = new ConcurrentHashMap<>();

    private final String name;
    private int totalKills = 0;
    private int kills = 0;
    private int killStreak = 0;
    private int betterKillStreak = 0;
    private int deaths = 0;

    private String lastAttack = null;
    private long lastAttackTime = -1;

    public PlayerStorage(String name) {
        this.name = name;
    }

    public void increaseKills() {
        this.kills++;
        this.totalKills++;

        if (this.kills > this.killStreak) {
            this.killStreak = this.kills;
        }

        if (this.killStreak > this.betterKillStreak) {
            this.betterKillStreak = this.killStreak;
        }
    }

    public void death() {
        this.deaths++;

        this.kills = 0;
        this.killStreak = 0;
    }

    public void attack(Player attack) {
        if (attack == null) {
            this.lastAttack = null;

            return;
        }

        this.lastAttack = attack.getName();

        this.lastAttackTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
    }

    public Player getLastAttack() {
        if (System.currentTimeMillis() > this.lastAttackTime || this.lastAttack == null) {
            return null;
        }

        return Server.getInstance().getPlayerExact(this.lastAttack);
    }

    public static PlayerStorage of(Player player) {
        return of(player.getName());
    }

    public static PlayerStorage of(String name) {
        return players.get(name.toLowerCase());
    }
}