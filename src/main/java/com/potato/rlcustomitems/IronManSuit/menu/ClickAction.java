package com.potato.rlcustomitems.IronManSuit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickAction {
    void click(Player clicker, InventoryClickEvent event);

}
