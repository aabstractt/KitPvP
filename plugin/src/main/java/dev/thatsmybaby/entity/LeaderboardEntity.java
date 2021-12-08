package dev.thatsmybaby.entity;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class LeaderboardEntity extends EntityHuman {

    public LeaderboardEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
}
