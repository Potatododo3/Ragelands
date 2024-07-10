package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemFreezeClock {

    public ItemStack getFreezeClock() {
        ItemStack freezeClock = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = freezeClock.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(CC.translate("&#D5A24B&lF&#C8A557&lr&#BBA862&le&#ADAA6E&le&#A0AD7A&lz&#93B086&le &#79B69D&lC&#6CB9A9&ll&#5EBBB5&lo&#51BEC0&lc&#44C1CC&lk"));
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Custom Item"
            ));
            freezeClock.setItemMeta(meta);
        }
        return freezeClock;
    }

}
