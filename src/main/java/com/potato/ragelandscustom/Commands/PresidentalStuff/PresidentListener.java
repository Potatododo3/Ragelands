package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PresidentListener implements Listener {
    private final Main main;

    public PresidentListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String presidentName = main.getVotingConfig().getString("president");

        if (presidentName != null && presidentName.equals(player.getName())) {
            // Apply effects to the president when they join
            Chat.broadcastMessage(Chat.prefix + " &dThe president " + presidentName + " &dhas joined!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0, true, false));
        }
    }

    @EventHandler
    public void onPresidentDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String presidentName = main.getVotingConfig().getString("president");

        if (presidentName != null && ChatColor.stripColor(presidentName).equals(player.getName())) {
            Player killer = player.getKiller();

            if (killer != null) {
                String newPresidentName = killer.getName();
                main.setPresident(newPresidentName);
                Chat.broadcastMessage("The President, " + presidentName + ", has been assassinated by " + newPresidentName + "!");
            } else {
                Chat.broadcastMessage("The President, " + presidentName + ", has died!");
                main.removePresident();
            }
        }
    }
}