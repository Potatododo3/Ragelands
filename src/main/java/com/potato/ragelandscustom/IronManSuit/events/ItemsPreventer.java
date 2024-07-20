package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.IronManSuit.Delay;
import com.potato.ragelandscustom.IronManSuit.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class ItemsPreventer implements Listener {

    ArrayList<Player> SpamCheck = new ArrayList<Player>();

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                .setName("&c&lLaser hands")
                .addLore("&fShoot explosive arrows")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        ItemStack Tracker = new ItemStackBuilder(Material.AMETHYST_SHARD)
                .setName("&d&lTracker")
                .addLore("&6Track Nearby Players")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        if (item.isSimilar(Laserhands) || item.isSimilar(Tracker)) {
            Chat.msg(player, Chat.prefix + "&7You can not move your abilities!");
            event.setCancelled(true);
        }

    }
    @EventHandler
    public void playerDropAbilityEvent (PlayerDropItemEvent e) {
        ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                .setName("&c&lLaser hands")
                .addLore("&fShoot explosive arrows")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        ItemStack Tracker = new ItemStackBuilder(Material.AMETHYST_SHARD)
                .setName("&d&lTracker")
                .addLore("&6Track Nearby Players")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        Player player = e.getPlayer();
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN)) || (Data.buildingSuit.contains(player))) {
            if (e.getItemDrop().getItemStack().isSimilar(Laserhands) || e.getItemDrop().getItemStack().isSimilar(Tracker)) {
                e.setCancelled(true);

                if (SpamCheck.contains(player)) {
                    return;
                }

                Chat.msg(player, Chat.prefix + "&7You can not move your abilities!");
                SpamCheck.add(player);

                Delay.until(200, () -> SpamCheck.remove(player));
            }
        }
    }
}
