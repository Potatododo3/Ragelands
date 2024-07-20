package com.potato.ragelandscustom.IronManSuit.events.JARVIS;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerHeal implements Listener {

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                if (player.getHealth() >= 11 && player.getHealth() < 12) {
                    Data.isLowHealth.remove(player);
                    player.removePotionEffect(PotionEffectType.REGENERATION);
                    Chat.msg(player, Chat.jarvis + "&6Vital signs normalizing.");
                }
            }
        }
    }
}