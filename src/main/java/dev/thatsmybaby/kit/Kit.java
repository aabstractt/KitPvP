package dev.thatsmybaby.kit;

import cn.nukkit.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kit {

    private String kitName;
    private Item[] armor;
    private Item[] items;
}