package com.potato.ragelandscustom.IronManSuit.events.JARVIS;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.ItemFunctions.CooldownManager;
import com.potato.ragelandscustom.Main;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class PlayerTracking implements Listener {
    private final Main main;
    private final CooldownManager cooldownManager;
    public static Set<Player> glowingPlayers = new HashSet<>();
    public PlayerTracking(Main main) {
        this.main = main;
        this.cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void onTrack(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', "&6Track Nearby Players"))) {
            startTrackingNearbyPlayersTask(player, 12, 36);
        }
    }

    private void startTrackingNearbyPlayersTask(Player player, int durationSeconds, int radius) {
        if (Data.Suit.contains(player) && Data.suitAssigned.get(player).equals("MK42")) {
            String playerName = player.getName();
            if (cooldownManager.isOnTrackerCooldown(playerName)) {
                long remainingMillis = cooldownManager.getRemainingTrackerCooldown(playerName);
                float remainingTime = remainingMillis / 1000.0f;  // Convert milliseconds to seconds
                player.sendMessage("Tracking has not completed! " + remainingTime + " seconds remaining.");
                return;
            }
            cooldownManager.setTrackerCooldown(playerName, 12);
            new BukkitRunnable() {
                int timeElapsed = 0;

                @Override
                public void run() {
                    if (timeElapsed >= durationSeconds * 20) { // Convert seconds to ticks
                        this.cancel();
                        Chat.msg(player, "Tracking completed after " + durationSeconds + " seconds.");
                        return;
                    }

                    // Check if the player is online
                    if (player != null && player.isOnline()) {
                        Location location = player.getLocation();
                        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                            if (entity instanceof Player) {
                                if (entity != player) {
                                    Player nearbyPlayer = (Player) entity;
                                    if (!glowingPlayers.contains(nearbyPlayer)) {
                                        sendGlowingPacket(player, nearbyPlayer, NamedTextColor.WHITE);
                                        Chat.msg(player, "&4&l!! &cTracker found player " + "&d&l" + nearbyPlayer.getDisplayName() + " &cwith health " + roundToNearestHalf(nearbyPlayer.getHealth() / 2) + "&4♥ " + "&4&l !!");
                                        glowingPlayers.add(nearbyPlayer); // Add player to glowing set
                                        unglowPlayers(player, nearbyPlayer);
                                    }
                                }
                            }
                        }
                    }

                    timeElapsed += 5; // Increment elapsed time by the period (10 ticks)
                }
            }.runTaskTimer(main, 0L, 5L); // Schedule the task to run every 10 ticks (0.5 seconds)
        }
        else {
            Chat.msg(player, "&cYou need to build the IronMan MK42 Suit to use this!");
        }
    }

    private void sendGlowingPacket(Player toPlayer, Player targetPlayer, NamedTextColor glowing) {
        main.geAPI.setGlowing(toPlayer, targetPlayer, glowing);
    }
    public double roundToNearestHalf(double number) {
        double increment = 0.5;
        return Math.round(number / increment) * increment;
    }
    private void unglowPlayers(Player toPlayer, Player targetPlayer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player glowingPlayer : glowingPlayers) {
                    if (!targetPlayer.isDead() || !toPlayer.isDead()) {
                        main.geAPI.unsetGlowing(toPlayer, targetPlayer);
                    }
                    glowingPlayers.remove(targetPlayer);
                }
                 // Clear the set of glowing players
            }
        }.runTaskLater(main, 20L * 12); // Unglow players after 12 seconds (adjust as needed)
    }
}
