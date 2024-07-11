package com.potato.ragelandscustom.IronManSuit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class PlayerFly implements Listener {

    @EventHandler
    public void onGlide (EntityToggleGlideEvent e ) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.isGliding()) {
                e.setCancelled(true);
            }
        }
    }

}
