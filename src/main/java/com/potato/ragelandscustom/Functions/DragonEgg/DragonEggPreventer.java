package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DragonEggPreventer implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        // Check if the item is a dragon egg
        if (item.getType() == Material.DRAGON_EGG) {
            // Check if the top inventory is a player inventory
            Inventory topInventory = event.getView().getTopInventory();
            List<InventoryType> inventoryTypes = Main.getAllInventoryTypes();
            if (inventoryTypes.contains(topInventory.getType())) {
                // Cancel the event if the top inventory is not a player inventory
                Chat.msg(player, "&f&lYou cannot store the dragon egg in this inventory!");
                event.setCancelled(true);
            }
        }
    }
}