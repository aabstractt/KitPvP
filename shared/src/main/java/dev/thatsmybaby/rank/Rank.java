package dev.thatsmybaby.rank;

import cn.nukkit.utils.TextFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Rank {

    private final String format;
    private final int minKills;
    private final int maxKills;

    public Rank(String format, int minKills, int maxKills) {
        this.format = TextFormat.colorize(format);

        this.minKills = minKills;

        this.maxKills = maxKills;
    }

    public String getName() {
        return TextFormat.clean(this.format);
    }

    @Override
    public String toString() {
        return "Rank{" + "format='" + format + '\'' + ", minKills=" + minKills + ", maxKills=" + maxKills + '}';
    }
}