package dev.thatsmybaby.rank;

import cn.nukkit.utils.TextFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rank {

    private String format;
    private final int minKills;
    private final int maxKills;

    public String getName() {
        return TextFormat.clean(this.format);
    }
}