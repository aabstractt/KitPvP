package dev.thatsmybaby.scoreboard;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.ImmutableMap;
import dev.thatsmybaby.scoreboard.packets.RemoveObjectivePacket;
import dev.thatsmybaby.scoreboard.packets.SetDisplayObjectivePacket;
import dev.thatsmybaby.scoreboard.packets.SetScorePacket;
import dev.thatsmybaby.scoreboard.packets.entry.ScorePacketEntry;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ScoreboardBuilder {

    public static String LIST = "list";
    public static String SIDEBAR = "sidebar";

    public static int ASCENDING = 0;
    public static int DESCENDING = 1;

    private String displayName;

    private String objectiveName;

    private String displaySlot;

    private int sortOrder;

    public void addPlayer(Player... players) {
        SetDisplayObjectivePacket pk = new SetDisplayObjectivePacket();

        pk.displaySlot = this.displaySlot;

        pk.objectiveName = this.objectiveName;
        pk.displayName = this.displayName;

        pk.criteriaName = "dummy";

        pk.sortOrder = this.sortOrder;

        for (Player player : players) {
            player.dataPacket(pk);
        }
    }

    public void removePlayer(Player... players) {
        RemoveObjectivePacket pk = new RemoveObjectivePacket();

        pk.objectiveName = this.objectiveName;

        for (Player player : players) {
            player.dataPacket(pk);
        }
    }

    public void setLine(int line, String text, Player... players) {
        this.setLines(ImmutableMap.of(line, text), players);
    }

    public void setLines(Map<Integer, String> lines, Player... players) {
        for (Player player : players) {
            player.dataPacket(getPackets(lines, SetScorePacket.TYPE_REMOVE));

            player.dataPacket(getPackets(lines, SetScorePacket.TYPE_CHANGE));
        }
    }

    public SetScorePacket getPackets(Map<Integer, String> lines, byte type) {
        SetScorePacket pk = new SetScorePacket();

        pk.type = type;

        List<ScorePacketEntry> entries = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : lines.entrySet()) {
            ScorePacketEntry entry0 = new ScorePacketEntry();

            entry0.objectiveName = this.objectiveName;

            entry0.score = entry.getKey();

            entry0.scoreboardId = entry.getKey();

            if (type == SetScorePacket.TYPE_CHANGE) {
                entry0.type = ScorePacketEntry.TYPE_FAKE_PLAYER;

                entry0.customName = TextFormat.colorize(entry.getValue());
            }

            entries.add(entry0);
        }

        pk.entries = entries.toArray(new ScorePacketEntry[0]);

        return pk;
    }
}