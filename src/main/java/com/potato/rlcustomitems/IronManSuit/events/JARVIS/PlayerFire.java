package com.potato.rlcustomitems.IronManSuit.events.JARVIS;

import com.potato.rlcustomitems.IronManSuit.Chat;
import com.potato.rlcustomitems.IronManSuit.Data;
import com.potato.rlcustomitems.IronManSuit.Delay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class PlayerFire implements Listener {

    ArrayList<Player> fireCooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerFire(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Data.Suit.contains(player)) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    if (!Data.isOnFire.contains(player)) {
                        if (fireCooldown.contains(player)) {
                            return;
                        }

                        Data.isOnFire.add(player);
                        Chat.msg(
                                player,
                                Chat.jarvis + "&cWarning! Fire detected!",
                                Chat.jarvis + "&6Fire suppression started!"
                        );
                        Delay.until(40, () -> {
                            player.setFireTicks(0);
                            Chat.msg(player, Chat.jarvis + "&6Fire suppression successful!");
                            fireCooldown.add(player);
                            Data.isOnFire.remove(player);
                        });

                        Delay.until(200, () -> fireCooldown.remove(player));
                    }
                }

                if (event.getCause() == EntityDamageEvent.DamageCause.POISON) {
                    if (!Data.isPoisoned.contains(player)) {
                        Chat.msg(
                                player,
                                Chat.jarvis + "&cWarning! Unknown synthetic detected!",
                                Chat.jarvis + "&6Starting scan..."
                        );
                    }
                }

                if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    if (!Data.Suit.contains(player)) {
                        Chat.msg(
                                player,
                                Chat.jarvis + ""
                        );
                    }
                }
            }
        }
    }
}