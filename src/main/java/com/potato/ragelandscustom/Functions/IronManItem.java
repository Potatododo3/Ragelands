package com.potato.ragelandscustom.Functions;

import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class IronManItem implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null) {
                if (item.isSimilar(Main.mark34)) {
                    e.setCancelled(true);
                    Data.suitAssigned.put(player, "MK42");
                    Main.getSuitManager().apply(player);
                    int amount = item.getAmount();
                    item.setAmount(amount - 1);
                }
                if (item.isSimilar(Main.mark50)) {
                    e.setCancelled(true);
                    Data.suitAssigned.put(player, "MK1");
                    Main.getSuitManager().apply(player);
                    int amount = item.getAmount();
                    item.setAmount(amount - 1);
                }
            }
        }
    }
}
