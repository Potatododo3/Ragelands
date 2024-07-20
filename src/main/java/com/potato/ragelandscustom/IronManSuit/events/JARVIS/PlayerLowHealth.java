package com.potato.ragelandscustom.IronManSuit.events.JARVIS;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerLowHealth implements Listener {

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                if (player.getHealth() < 10 && !Data.isLowHealth.contains(player) && !player.isDead()) {
                    Data.isLowHealth.add(player);
                    Chat.msg(
                            player,
                            Chat.jarvis + "&cWarning! High damage sustained!",
                            Chat.jarvis + "&6Adrenaline has been administered."
                    );

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
                        }
                    }.runTaskLater(Main.getPlugin(), 20);
                }
            }
        }
    }
}