package com.potato.ragelandscustom.Functions;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import static com.potato.ragelandscustom.Commands.NoTNTCommand.getNoTNT;

public class NoTNT implements Listener {

    @EventHandler
    public void onTNTExplode (EntityExplodeEvent e) {
        if (!getNoTNT()) {
            return;
        }
        Entity entity = e.getEntity();
        if (entity instanceof TNTPrimed || entity.getType() == EntityType .MINECART_TNT || entity instanceof Creeper) {
            e.blockList().clear();
        }
    }

}
