package com.potato.ragelandscustom.Items;

import com.potato.ragelandscustom.CC;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemShieldDeflect {
    private final Main main;

    public ItemShieldDeflect(Main main) {
        this.main = main;
    }

    public ItemStack getDeflectingShield() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getCustomItemKey();
        ItemStack deflectingShield = new ItemStack(Material.SHIELD, 1);
        ItemMeta meta = deflectingShield.getItemMeta();
        meta.setDisplayName(CC.translate("&#FFE259&lD&#F7D956&le&#EECF52&lf&#E6C64F&ll&#DDBD4B&le&#D5B348&lc&#CCAA44&lt&#C4A041&li&#BB973E&ln&#B38E3A&lg &#A27B33&lS&#997230&lh&#91682C&li&#885F29&le&#805525&ll&#774C22&ld"));
        PersistentDataContainer deflectingShieldPDC = meta.getPersistentDataContainer();
        deflectingShieldPDC.set(key, PersistentDataType.BOOLEAN, true);
        deflectingShield.setItemMeta(meta);
        return deflectingShield;
    }
}
