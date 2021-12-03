package dev.thatsmybaby.kit;

import cn.nukkit.item.Item;
import dev.thatsmybaby.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Kit {

    private String kitName;
    private Map<Integer, Item> items;

    public Map<String, Map<Integer, String>> serialize() {
        Map<Integer, String> items = new HashMap<>();

        this.items.forEach((integer, item) -> items.put(integer, ItemUtils.itemToString(item)));

        return new HashMap<String, Map<Integer, String>>() {{
            put("items", items);
        }};
    }

    public static Kit deserialize(String kitName, Map<String, Map<Integer, String>> serialized) {
        Map<Integer, Item> items = new HashMap<>();

        serialized.get("items").forEach((slot, string) -> items.put(slot, ItemUtils.stringToItem(string)));

        return new Kit(kitName, items);
    }
}