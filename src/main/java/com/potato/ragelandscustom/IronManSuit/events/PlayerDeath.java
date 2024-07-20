package com.potato.ragelandscustom.IronManSuit.events;

import com.google.common.collect.Maps;
import com.potato.ragelandscustom.IronManSuit.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerDeath implements Listener {

    private HashMap<UUID, Integer> deaths = Maps.newHashMap();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
            playerpdc.set(suitOn, PersistentDataType.BOOLEAN, false);
            Data.isLowHealth.remove(player);
            Data.buildingSuit.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }
    }
}
