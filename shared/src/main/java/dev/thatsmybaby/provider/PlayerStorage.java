package dev.thatsmybaby.provider;

import cn.nukkit.Player;
import cn.nukkit.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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
    private int coins = 0;
    @Setter private String rankName;

    private List<String> kits = new ArrayList<>();

    private String lastAttack = null;
    private long lastAttackTime = -1;

    public PlayerStorage(String name, String rankName) {
        this.name = name;

        this.rankName = rankName;
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

    public void increaseCoins(int increase) {
        this.coins += increase;
    }

    public void decreaseCoins(int decrease) {
        if (decrease == 0) {
            return;
        }

        this.coins -= decrease;
    }

    public void death() {
        this.deaths++;

        this.kills = 0;
        this.killStreak = 0;
    }

    public boolean attack(Player attack) {
        if (attack == null) {
            this.lastAttack = null;

            return false;
        }

        boolean updated = !attack.getName().equals(this.lastAttack);

        this.lastAttack = attack.getName();

        this.lastAttackTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);

        return updated;
    }

    public Player getLastAttack() {
        if (System.currentTimeMillis() > this.lastAttackTime || this.lastAttack == null) {
            return null;
        }

        return Server.getInstance().getPlayerExact(this.lastAttack);
    }

    public String getAttackingName() {
        Player target = getLastAttack();

        return target == null ? "" : target.getName();
    }

    public static PlayerStorage of(Player player) {
        return of(player.getName());
    }

    public static PlayerStorage of(String name) {
        return players.get(name.toLowerCase());
    }
}