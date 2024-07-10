package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.Main;
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
