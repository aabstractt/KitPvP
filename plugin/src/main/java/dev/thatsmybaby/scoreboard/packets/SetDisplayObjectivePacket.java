package dev.thatsmybaby.scoreboard.packets;

import cn.nukkit.network.protocol.DataPacket;

public class SetDisplayObjectivePacket extends DataPacket {

    public String displaySlot;
    public String objectiveName;
    public String displayName;
    public String criteriaName;
    public int sortOrder;

    @Override
    public byte pid() {
        return (byte) 0x6b;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset(); // WTF

        this.putString(this.displaySlot);
        this.putString(this.objectiveName);
        this.putString(this.displayName);
        this.putString(this.criteriaName);
        this.putVarInt(this.sortOrder);
    }
}
