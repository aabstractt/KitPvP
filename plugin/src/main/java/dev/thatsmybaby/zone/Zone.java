package dev.thatsmybaby.zone;

import cn.nukkit.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Zone {

    private final Vector3 min;
    private final Vector3 max;
}
