package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.IronManSuit.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DragonEggPreventer implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        if (event.getView().getTopInventory().getType()== InventoryType.SHULKER_BOX || event.getView().getTopInventory().getType() == InventoryType.ENDER_CHEST) {
            if (item.getType() == Material.DRAGON_EGG) {
                Chat.msg(player, "&f&lYou can not store the dragon egg in an ender chest or a shulker box!");
                event.setCancelled(true);
            }
        }
    }

}
