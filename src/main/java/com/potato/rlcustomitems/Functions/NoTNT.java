package com.potato.rlcustomitems.Functions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class NoTNT implements Listener {

    @EventHandler
    public void onTNTExplode (EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof TNTPrimed || entity.getType() == EntityType .MINECART_TNT) {
            e.blockList().clear();
        }

    }

}
