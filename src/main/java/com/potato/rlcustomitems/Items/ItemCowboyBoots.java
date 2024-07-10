package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.Items.PDCKeys;
import com.potato.rlcustomitems.CC;
import com.potato.rlcustomitems.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemCowboyBoots {
    private final Main main;

    public ItemCowboyBoots(Main main) {
        this.main = main;
    }

    public ItemStack getCowboyBoots() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getKey();
        ItemStack cowboyBoots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        ItemMeta meta = cowboyBoots.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(CC.translate("&#5C3320&lC&#603D30&lo&#654740&lw&#695150&lb&#6D5B60&lo&#726570&ly &#7B7891&lB&#7F82A1&lo&#838CB1&lo&#8896C1&lt&#8CA0D1&ls"));
            PersistentDataContainer fragBombPDC = meta.getPersistentDataContainer();
            fragBombPDC.set(key, PersistentDataType.BOOLEAN, true);
        }
        cowboyBoots.setItemMeta(meta);
        return cowboyBoots;
    }
}
