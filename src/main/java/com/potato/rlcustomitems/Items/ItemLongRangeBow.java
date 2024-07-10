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

public class ItemLongRangeBow {
    private final Main main;

    public ItemLongRangeBow(Main main) {
        this.main = main;
    }

    public ItemStack getLongRangeBow() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getKey();
        ItemStack longRangeBow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = longRangeBow.getItemMeta();
        meta.setDisplayName(CC.translate("&#54050E&lL&#4E0E1E&lo&#48182F&ln&#43213F&lg &#373460&lR&#313D71&la&#2C4681&ln&#264F92&lg&#2059A2&le &#156BC3&lB&#0F75D4&lo&#097EE4&lw"));
        PersistentDataContainer longRangeBowPDC = meta.getPersistentDataContainer();
        longRangeBowPDC.set(key, PersistentDataType.BOOLEAN, true);
        longRangeBow.setItemMeta(meta);
        return longRangeBow;
    }
}
