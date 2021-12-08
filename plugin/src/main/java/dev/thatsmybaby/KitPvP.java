package dev.thatsmybaby;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.command.admin.AdminCommand;
import dev.thatsmybaby.command.kit.KitCommand;
import dev.thatsmybaby.entity.GameSelectorEntity;
import dev.thatsmybaby.entity.KitSelectorEntity;
import dev.thatsmybaby.entity.LeaderboardEntity;
import dev.thatsmybaby.kit.KitFactory;
import dev.thatsmybaby.listener.*;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.rank.RankFactory;
import dev.thatsmybaby.room.PrivateRoom;
import dev.thatsmybaby.room.PrivateRoomFactory;
import dev.thatsmybaby.task.LeaderboardUpdateTask;
import dev.thatsmybaby.task.PlayerUpdateTask;
import dev.thatsmybaby.task.PrivateRoomUpdateTask;
import dev.thatsmybaby.task.ScoreboardUpdateTask;
import dev.thatsmybaby.zone.ZoneFactory;
import lombok.Getter;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KitPvP extends PluginBase {

    @Getter
    private static KitPvP instance;

    private static final Map<String, String> messages = new HashMap<>();

    public static final Map<String, PrivateRoom> queueJoin = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        instance = this;
        TaskUtils.plugin = this;

        this.saveDefaultConfig();
        this.saveResource("messages.yml");

        try {
            MysqlProvider.getInstance().init(getConfig().get("mysql", new HashMap<>()));

            Entity.registerEntity("KitSelectorEntity", KitSelectorEntity.class);
            Entity.registerEntity("GameSelectorEntity", GameSelectorEntity.class);
            Entity.registerEntity("LeaderboardEntity", LeaderboardEntity.class);

            for (Map.Entry<String, Object> entry : (new Config(new File(getDataFolder(), "messages.yml"), Config.YAML)).getAll().entrySet()) {
                messages.put(entry.getKey(), entry.getValue().toString());
            }

            KitFactory.getInstance().init();
            ZoneFactory.getInstance().init();
            RankFactory.getInstance().init(getConfig().getList("ranks"));

            getServer().getCommandMap().register("admin", new AdminCommand("admin", "Admin commands", "", new String[0]));
            getServer().getCommandMap().register("kit", new KitCommand("kit", "Kit command", "", new String[0]));

            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
            getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
            getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
            getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
            getServer().getPluginManager().registerEvents(new LevelBlockListener(), this);
            getServer().getPluginManager().registerEvents(new EntityLevelChangeListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerFormRespondedListener(), this);

            getServer().getScheduler().scheduleRepeatingTask(new LeaderboardUpdateTask(), 60);
            getServer().getScheduler().scheduleRepeatingTask(new ScoreboardUpdateTask(this.getConfig().getStringList("worlds")), 20);
            getServer().getScheduler().scheduleRepeatingTask(new PlayerUpdateTask(), 20);
            getServer().getScheduler().scheduleRepeatingTask(new PrivateRoomUpdateTask(), 20);
        } catch (SQLException e) {
            e.printStackTrace();

            this.getServer().getPluginManager().disablePlugin(this);

            this.getPluginLoader().disablePlugin(this);

            this.getServer().shutdown();
        }
    }

    @Override
    public void onDisable() {
        for (PrivateRoom privateRoom : PrivateRoomFactory.getInstance().getPrivateRoomMap().values()) {
            privateRoom.close(false);
        }
    }

    public static void defaultValues(Player player, Level level) {
        if (level == null) {
            level = player.getLevel();
        }

        if (level != null) {
            player.teleport(level.getSpawnLocation());
        }

        player.getInventory().clearAll();

        player.extinguish();
        player.setGamemode(2);
        player.removeAllEffects();
        player.setExperience(0, 0);

        player.setHealth(player.getMaxHealth());
        player.getFoodData().reset();
        player.setFoodEnabled(false);
    }

    public static String replacePlaceholders(String key, String... values) {
        String message = messages.getOrDefault(key, key);

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

        return shouldDisplay(TextFormat.colorize(message));
    }

    private static String shouldDisplay(String text) {
        if (!text.contains("<display=")) {
            return text;
        }

        String clearText = text.split("<display=")[1];

        return clearText.equalsIgnoreCase("!true")
                || clearText.equalsIgnoreCase("false")
                || clearText.equalsIgnoreCase("true&&false")
                || clearText.equalsIgnoreCase("false&&true")
                || clearText.equalsIgnoreCase("true&&!true")
                || clearText.equalsIgnoreCase("!true&&true")
                || clearText.equalsIgnoreCase("!true&&!true")
                || clearText.equalsIgnoreCase("false&&!true")
                || clearText.equalsIgnoreCase("!false&&!false")
                || clearText.equalsIgnoreCase("!false&&false")
                || clearText.equalsIgnoreCase("false&&!false")
                || clearText.equalsIgnoreCase("false&&false")
                || clearText.equalsIgnoreCase("!true&&false") ?null : text.split("<display=")[0];
    }

    public static String getDeviceAsString(int os) {
        switch (os) {
            case 1:
                return "Android";
            case 2:
                return "iOS";
            case 3:
                return "macOS";
            case 4:
                return "FireOS";
            case 5:
            case 6:
                return "GearVR";
            case 7:
            case 8:
                return "Windows";
            case 10:
                return "TvOS";
            case 11:
                return "PS4";
            case 12:
                return "Switch";
            case 13:
                return "Xbox";
            default:
                return "Unknown";
        }
    }

    public static String getInputAsString(int input) {
        switch (input) {
            case 1:
                return "Mouse";
            case 2:
                return "Touch";
            case 3:
                return "Controller";

            default:
                return "Unkown";
        }
    }
}