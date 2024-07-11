package com.potato.ragelandscustom.Items;

import com.potato.ragelandscustom.Main;
import org.bukkit.NamespacedKey;

public class PDCKeys {
    private final Main main;

    public PDCKeys(Main main) {
        this.main = main;
    }

    public NamespacedKey getKey() {
        NamespacedKey key = new NamespacedKey(main, "customitem");
        return key;
    }
}
