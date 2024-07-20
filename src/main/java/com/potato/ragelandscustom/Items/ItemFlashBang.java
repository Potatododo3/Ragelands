package com.potato.ragelandscustom.Items;

import com.potato.ragelandscustom.CC;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemFlashBang {

    private final Main main;

    public ItemFlashBang(Main main) {
        this.main = main;
    }

    public ItemStack getFlashBang() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getCustomItemKey();
        ItemStack flashBang = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = flashBang.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(CC.translate("&#1A9731&lF&#31A140&ll&#48AA4E&la&#5EB45D&ls&#75BE6C&lh &#A3D189&lb&#B9DB98&la&#D0E4A6&ln&#E7EEB5&lg"));
            PersistentDataContainer fragBombPDC = meta.getPersistentDataContainer();
            fragBombPDC.set(key, PersistentDataType.BOOLEAN, true);
        }
        flashBang.setItemMeta(meta);
        return flashBang;
    }

}
