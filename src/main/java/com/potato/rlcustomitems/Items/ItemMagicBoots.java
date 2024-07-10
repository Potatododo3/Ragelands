package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.Items.PDCKeys;
import com.potato.rlcustomitems.CC;
import com.potato.rlcustomitems.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ItemMagicBoots {
    private final Main main;

    public ItemMagicBoots(Main main) {
        this.main = main;
    }

    public ItemStack getMagicBoots() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getKey();
        ItemStack itemStack = new ItemStack(Material.IRON_BOOTS, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(CC.translate("&#6BC08D&lP&#6AB586&lh&#69A97F&la&#689E77&ln&#679270&lt&#668769&lo&#667C62&lm &#646553&lb&#63594C&lo&#624E45&lo&#61423D&lt&#603736&ls"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setLore(Arrays.asList(
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Sneak to gain invisibility.",
                ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Gives slowness when sneaking."
        ));
        PersistentDataContainer magicBootsPdc = meta.getPersistentDataContainer();
        magicBootsPdc.set(key, PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
