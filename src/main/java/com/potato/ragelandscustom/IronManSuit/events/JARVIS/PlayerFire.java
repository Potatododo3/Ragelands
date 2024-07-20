package com.potato.ragelandscustom.IronManSuit.events.JARVIS;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.IronManSuit.Delay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerFire implements Listener {

    ArrayList<Player> fireCooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerFire(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
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
                        Delay.until(15, () -> {
                            player.setFireTicks(0);
                            Chat.msg(player, Chat.jarvis + "&6Fire suppression successful!");
                            fireCooldown.add(player);
                            Data.isOnFire.remove(player);
                        });

                        Delay.until(50, () -> fireCooldown.remove(player));
                    }
                }
            }
        }
    }
}