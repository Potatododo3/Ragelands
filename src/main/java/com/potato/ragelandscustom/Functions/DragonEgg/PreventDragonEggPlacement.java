package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PreventDragonEggPlacement implements Listener {

    private final Main main;

    public PreventDragonEggPlacement(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DRAGON_EGG && !event.getPlayer().isSneaking()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You must be sneaking to place the dragon egg!");
        }
    }
}