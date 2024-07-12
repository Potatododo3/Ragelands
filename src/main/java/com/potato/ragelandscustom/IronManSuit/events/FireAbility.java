package com.potato.ragelandscustom.IronManSuit.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class FireAbility implements Listener {

    private final JavaPlugin plugin;

    public FireAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType() == Material.BLAZE_POWDER && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', "&fShoot explosive arrows"))) {
            Projectile projectile = player.launchProjectile(org.bukkit.entity.Arrow.class);
            projectile.setVelocity(player.getLocation().getDirection().multiply(2));
            projectile.setMetadata("ironman_projectile", new FixedMetadataValue(plugin, true));

            // Schedule despawning task after 10 seconds if not hit
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!projectile.isDead()) {
                    projectile.remove();
                }
            }, 200L); // 200 ticks = 10 seconds

            // Check for nearby players and update projectile velocity
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!projectile.isDead()) {
                        Player nearestPlayer = findNearestPlayer(projectile);
                        if (nearestPlayer != null) {
                            Vector direction = nearestPlayer.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize();
                            projectile.setVelocity(direction.multiply(2)); // Adjust speed as needed
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L); // Run every tick (20 times per second)
        }
    }

    private Player findNearestPlayer(Projectile projectile) {
        double minDistanceSquared = 16; // 4 blocks squared
        Player nearestPlayer = null;
        Collection<Player> nearbyPlayers = projectile.getWorld().getPlayers();

        for (Player player : nearbyPlayers) {
            if (!player.equals(projectile.getShooter())) { // Exclude the shooter (usually the player)
                double distanceSquared = player.getLocation().distanceSquared(projectile.getLocation());
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    nearestPlayer = player;
                }
            }
        }

        return nearestPlayer;
    }

}
