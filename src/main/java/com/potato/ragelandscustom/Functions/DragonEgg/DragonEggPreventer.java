package com.potato.ragelandscustom.Functions.DragonEgg;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DragonEggPreventer implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the inventory is an Ender Chest
        Inventory inventory = event.getInventory();
        if (inventory.getType() == InventoryType.ENDER_CHEST) {
            // Check if the clicked item is a dragon egg
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.DRAGON_EGG) {
                // Cancel the event to prevent the dragon egg from being placed in the Ender Chest
                event.setCancelled(true);
                // Optionally, send a message to the player
                event.getWhoClicked().sendMessage("You cannot place dragon eggs in your Ender Chest!");
            }
        }
    }

}
