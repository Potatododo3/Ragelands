package com.potato.ragelandscustom.IronManSuit.events;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.ItemFunctions.CooldownManager;
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
    private final CooldownManager cooldownManager;
    private final JavaPlugin plugin;

    public FireAbility(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType() == Material.BLAZE_POWDER && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', "&fShoot explosive arrows"))) {
                if (Data.Suit.contains(player)) {
                    String playerName = player.getName();
                    if (cooldownManager.isOnFireAbilityCooldown(playerName)) {
                        return;
                    }
                    cooldownManager.setFireAbilityCooldown(playerName, 1);
                    Projectile projectile = player.launchProjectile(org.bukkit.entity.Arrow.class);
                    projectile.setGravity(false);
                    projectile.setVisualFire(true);
                    projectile.setCustomNameVisible(true);
                    projectile.setCustomName("LASER");
                    projectile.setVelocity(player.getLocation().getDirection().multiply(3.33));
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
    }

    private Player findNearestPlayer(Projectile projectile) {
        double minDistanceSquared = 64; // 8 blocks squared
        Player nearestPlayer = null;
        Collection<Player> nearbyPlayers = projectile.getWorld().getPlayers();

        for (Player player : nearbyPlayers) {
            Player shooter = (Player) projectile.getShooter();
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(shooter);
            FPlayer nearbyPlayer = FPlayers.getInstance().getByPlayer(player);
            Faction shooterFPlayerFaction = fPlayer.getFaction();
            Faction nearbyPlayerFaction = nearbyPlayer.getFaction();
            // Exclude the shooter (usually the player)
            if (!player.equals(projectile.getShooter()) && shooterFPlayerFaction != nearbyPlayerFaction) {
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
