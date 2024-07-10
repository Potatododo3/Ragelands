package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemPowerOrbFragment {

    public ItemStack getPowerOrbFragment() {
        ItemStack powerOrbFragment = new ItemStack(Material.QUARTZ);
        ItemMeta powerMeta = powerOrbFragment.getItemMeta();
        powerMeta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Power Orb Fragment");
        List<String> array = new ArrayList<>();
        array.add(ChatColor.GRAY + "Can be used to craft custom items");
        powerMeta.setLore(array);
        powerOrbFragment.setItemMeta(powerMeta);
        return powerOrbFragment;
    }

}
