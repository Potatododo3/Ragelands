package com.potato.ragelandscustom.IronManSuit.events.JARVIS;

import com.potato.ragelandscustom.IronManSuit.Chat;
import com.potato.ragelandscustom.Main;
import net.kyori.adventure.text.format.NamedTextColor;
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
    private final Set<Player> glowingPlayers = new HashSet<>();
    public PlayerTracking(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onTrack(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains("Track Nearby Players")) {
            startTrackingNearbyPlayersTask(player, 13, 36);
        }
    }

    private void startTrackingNearbyPlayersTask(Player player, int durationSeconds, int radius) {
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
                                if (!main.geAPI.getGlowingEntities().containsKey(nearbyPlayer.getUniqueId())) {
                                    sendGlowingPacket(player, nearbyPlayer, NamedTextColor.WHITE);
                                    Chat.msg(player, "&4&l!! &cTracker found player " + nearbyPlayer.getDisplayName() + " with health " + nearbyPlayer.getHealth() + "&4&l!!");
                                    glowingPlayers.add(nearbyPlayer); // Add player to glowing set
                                    unglowPlayers(player, nearbyPlayer);
                                }
                            }
                        }
                    }
                }

                timeElapsed += 10; // Increment elapsed time by the period (10 ticks)
            }
        }.runTaskTimer(main, 0L, 10L); // Schedule the task to run every 10 ticks (0.5 seconds)
    }

    private void sendGlowingPacket(Player toPlayer, Player targetPlayer, NamedTextColor glowing) {
        main.geAPI.setGlowing(toPlayer, targetPlayer, glowing);
    }

    private void unglowPlayers(Player toPlayer, Player targetPlayer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player glowingPlayer : glowingPlayers) {
                    main.geAPI.unsetGlowing(toPlayer, targetPlayer);
                }
                glowingPlayers.clear(); // Clear the set of glowing players
            }
        }.runTaskLater(main, 20L * 12); // Unglow players after 12 seconds (adjust as needed)
    }
}
