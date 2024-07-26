package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.IronManSuit.Delay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerMoveArmourListener implements Listener {

    ArrayList<Player> SpamCheck = new ArrayList<Player>();

    @EventHandler
    public void playerMoveArmourEvent (InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN)) || (Data.buildingSuit.contains(player))) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);

                if (SpamCheck.contains(player)) {
                    player.closeInventory();
                    return;
                }

                player.closeInventory();
                Chat.msg(player, Chat.prefix + "&7To remove your armour type /suits eject");
                SpamCheck.add(player);

                Delay.until(200, () -> SpamCheck.remove(player));
            }
        }
    }
}