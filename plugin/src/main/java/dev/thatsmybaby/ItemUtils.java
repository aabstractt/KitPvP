package dev.thatsmybaby;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

public class ItemUtils {

    public static String itemToString(Item item) {
        String string = item.getId() + ":" + item.getDamage() + ":" + item.getCount();

        for (Enchantment enchantment : item.getEnchantments()) {
            string += ":" + enchantment.getId() + ";" + enchantment.getLevel();
        }

        return string;
    }

    public static Item stringToItem(String string) {
        String[] split = string.split(":");

        Item item = Item.get(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

        for (int i = 3; i < split.length; i++) {
            String[] enchant = split[i].split(";");

            item.addEnchantment(Enchantment.get(Integer.parseInt(enchant[0])).setLevel(Integer.parseInt(enchant[1])));
        }

        return item;
    }
}