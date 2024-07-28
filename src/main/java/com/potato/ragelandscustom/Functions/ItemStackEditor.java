package com.potato.ragelandscustom.Functions;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackEditor {

    public static ItemStack addLore (ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(Chat.color(name));
        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
    public static ItemStack setDisplayNameItem(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Chat.color(name));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
