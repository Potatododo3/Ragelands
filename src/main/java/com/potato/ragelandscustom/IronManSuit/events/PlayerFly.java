package com.potato.ragelandscustom.IronManSuit.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerFly implements Listener {
    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        //Bukkit.broadcastMessage(e.isGliding() + " ");
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!player.getAllowFlight()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(false);
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        if (e.isFlying()) {
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setGliding(true);
        }
    }
}
