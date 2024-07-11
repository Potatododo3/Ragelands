package com.potato.ragelandscustom.IronManSuit.events;

import com.google.common.collect.Maps;
import com.potato.ragelandscustom.IronManSuit.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeath implements Listener {

    private HashMap<UUID, Integer> deaths = Maps.newHashMap();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (Data.Suit.contains(player)) {
            Data.Suit.remove(player);
            Data.isLowHealth.remove(player);
            Data.buildingSuit.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
