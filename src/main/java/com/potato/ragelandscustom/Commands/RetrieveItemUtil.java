package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class RetrieveItemUtil {
    private final Main main;

    public RetrieveItemUtil(Main main) {
        this.main = main;
    }

    public ItemStack getSavedItem() {
        FileConfiguration customConfig = main.getCustomConfig();
        return customConfig.getItemStack("saved-item");
    }
}
