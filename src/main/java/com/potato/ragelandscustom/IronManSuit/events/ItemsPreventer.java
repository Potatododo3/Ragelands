package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.IronManSuit.Delay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class ItemsPreventer implements Listener {

    ArrayList<Player> SpamCheck = new ArrayList<Player>();

    @EventHandler
    public void playerAbilityMoveEvent (InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (Data.Suit.contains(player) || (Data.buildingSuit.contains(player))) {
            if (event.getSlot() == 0 || event.getSlot() == 1) {
                event.setCancelled(true);

                if (SpamCheck.contains(player)) {
                    player.closeInventory();
                    return;
                }

                player.closeInventory();
                Chat.msg(player, Chat.prefix + "&7You can not move your abilities!");
                SpamCheck.add(player);

                Delay.until(200, () -> SpamCheck.remove(player));
            }
        }
    }
}
