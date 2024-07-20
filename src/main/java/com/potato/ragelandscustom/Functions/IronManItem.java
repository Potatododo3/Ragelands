package com.potato.ragelandscustom.Functions;

import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.MK34;
import static com.potato.ragelandscustom.IronManSuit.SuitManager.MK50;

public class IronManItem implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null) {
                if (item.isSimilar(Main.mark34)) {
                    e.setCancelled(true);
                    PersistentDataContainer playerpdc = player.getPersistentDataContainer();
                    playerpdc.set(MK34, PersistentDataType.BOOLEAN, true);
                    Main.getSuitManager().apply(player);
                    int amount = item.getAmount();
                    item.setAmount(amount - 1);
                }
                if (item.isSimilar(Main.mark50)) {
                    e.setCancelled(true);
                    PersistentDataContainer playerpdc = player.getPersistentDataContainer();
                    playerpdc.set(MK50, PersistentDataType.BOOLEAN, true);
                    Main.getSuitManager().apply(player);
                    int amount = item.getAmount();
                    item.setAmount(amount - 1);
                }
            }
        }
    }
}
