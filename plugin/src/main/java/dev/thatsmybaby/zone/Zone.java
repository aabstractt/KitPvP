package dev.thatsmybaby.zone;

import cn.nukkit.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Zone {

    private final Vector3 min;
    private final Vector3 max;
}
