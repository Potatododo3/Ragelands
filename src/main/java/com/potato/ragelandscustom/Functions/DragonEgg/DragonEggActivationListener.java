package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DragonEggActivationListener implements Listener {

    private final Main main;

    public DragonEggActivationListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null && event.getItem().getType() == Material.DRAGON_EGG) {
                // Open ability selection GUI
                new AbilitySelectionGUI(main).openAbilitySelectionGUI(event.getPlayer());
            }
        }
    }
}