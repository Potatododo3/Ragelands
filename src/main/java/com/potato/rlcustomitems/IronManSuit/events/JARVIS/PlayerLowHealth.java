package com.potato.rlcustomitems.IronManSuit.events.JARVIS;

import com.potato.rlcustomitems.IronManSuit.Chat;
import com.potato.rlcustomitems.IronManSuit.Data;
import com.potato.rlcustomitems.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerLowHealth implements Listener {

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Data.Suit.contains(player)) {
                if (player.getHealth() < 10 && !Data.isLowHealth.contains(player)) {
                    Data.isLowHealth.add(player);
                    Chat.msg(
                            player,
                            Chat.jarvis + "&cWarning! High damage sustained!",
                            Chat.jarvis + "&6Adrenaline has been administered."
                    );

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
                        }
                    }.runTaskLater(Main.getPlugin(), 20);
                }
            }
        }
    }
}