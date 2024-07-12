package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Data.Suit.remove(player);
        Data.buildingSuit.remove(player);
        Data.isGliding.remove(player);
        Data.isLowHealth.remove(player);
        Data.isOnFire.remove(player);
        Data.isPoisoned.remove(player);
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
