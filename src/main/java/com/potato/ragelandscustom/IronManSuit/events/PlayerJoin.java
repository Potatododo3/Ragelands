package com.potato.ragelandscustom.IronManSuit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.*;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (playerpdc.get(suitOn, PersistentDataType.BOOLEAN) == null) {
            playerpdc.set(MK50, PersistentDataType.BOOLEAN, false);
            playerpdc.set(suitOn, PersistentDataType.BOOLEAN, false);
            playerpdc.set(MK34, PersistentDataType.BOOLEAN, false);
        }
    }
}
