package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemsPreventer implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClickEvent(InventoryMoveItemEvent event) {
        ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                .setName("&c&lLaser hands")
                .addLore("&fShoot explosive arrows")
                .build();
        ItemStack Tracker = new ItemStackBuilder(Material.COMPASS)
                .setName("&d&lTracker")
                .addLore("&6Track Nearby Players")
                .build();
        Inventory destination = event.getDestination();
        ItemStack item = event.getItem();
        if (item.isSimilar(Laserhands) || item.isSimilar(Tracker)) {
            if (destination.getType() != InventoryType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }
}
