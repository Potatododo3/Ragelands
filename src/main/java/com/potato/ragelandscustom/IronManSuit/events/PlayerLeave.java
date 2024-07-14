package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.IronManSuit.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        if (Data.Suit.contains(player)) {
            ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                    .setName("&c&lLaser hands")
                    .addLore("&fShoot explosive arrows")
                    .build();
            ItemStack Tracker = new ItemStackBuilder(Material.COMPASS)
                    .setName("&d&lTracker")
                    .addLore("&6Track Nearby Players")
                    .build();
            if (player.getInventory().contains(Laserhands)) {
                player.getInventory().removeItem(Laserhands);
            }
            if (player.getInventory().contains(Tracker)) {
                player.getInventory().removeItem(Tracker);
            }
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
        Data.Suit.remove(player);
        Data.buildingSuit.remove(player);
        Data.isGliding.remove(player);
        Data.isLowHealth.remove(player);
        Data.isOnFire.remove(player);
        Data.isPoisoned.remove(player);

    }
}
