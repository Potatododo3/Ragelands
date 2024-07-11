package com.potato.ragelandscustom.IronManSuit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CloseAction {

    void close(Player player, InventoryCloseEvent e);

}
