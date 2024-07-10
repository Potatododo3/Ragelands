package com.potato.rlcustomitems.IronManSuit.events.JARVIS;

import com.potato.rlcustomitems.IronManSuit.Chat;
import com.potato.rlcustomitems.IronManSuit.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerHeal implements Listener {

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Data.Suit.contains(player)) {
                if (player.getHealth() >= 11 && player.getHealth() < 12) {
                    Data.isLowHealth.remove(player);
                    player.removePotionEffect(PotionEffectType.REGENERATION);
                    Chat.msg(player, Chat.jarvis + "&6Vital signs normalizing.");
                }
            }
        }
    }
}