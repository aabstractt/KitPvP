package dev.thatsmybaby.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rank {

    private String format;
    private final int minKills;
    private final int maxKills;
}