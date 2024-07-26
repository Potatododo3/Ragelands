package com.potato.ragelandscustom.IronManSuit.events;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.ItemFunctions.CooldownManager;
import com.potato.ragelandscustom.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class FireAbility implements Listener {
    private final CooldownManager cooldownManager;
    private final Main main;
    public FireAbility(Main main) {
        this.main = main;
        this.cooldownManager = new CooldownManager();
        radius = main.getConfig().getDouble("LaserRadius");
    }
    static HashMap<UUID, Projectile> laserArrowMap;
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (item.getType() == Material.BLAZE_POWDER && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', "&fShoot explosive arrows"))) {
                if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
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
                    projectile.setMetadata("ironman_projectile", new FixedMetadataValue(main, true));
                    laserArrowMap.put(projectile.getUniqueId(), projectile); // Store the arrow's UUID

                    // Schedule despawning task after 10 seconds if not hit
                    main.getServer().getScheduler().runTaskLater(main, () -> {
                        if (!projectile.isDead()) {
                            projectile.remove();
                            laserArrowMap.remove(projectile.getUniqueId());
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
                    }.runTaskTimer(main, 0L, 1L); // Run every tick (20 times per second)
                }
                else {
                    Chat.msg(player, "You need to build the IronMan MK42 Suit to use this!");
                }
        }
    }
    public Projectile getArrow(UUID uuid) {
        return laserArrowMap.get(uuid); // Retrieve the arrow by UUID
    }
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        try {
            Projectile storedProjectile = getArrow(projectile.getUniqueId());
            if (storedProjectile != null) {
                if (event.getHitEntity() instanceof Player) {
                    Player player = (Player) event.getHitEntity();
                    player.damage(30);
                }
            }
        }
        catch (NullPointerException e) {
            return;
        }
    }
    double radius;
    private Player findNearestPlayer(Projectile projectile) {
        double minDistanceSquared = radius;
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
                if (!shooterFPlayerFaction.isWilderness() || !nearbyPlayerFaction.isWilderness()) {
                    double distanceSquared = player.getLocation().distanceSquared(projectile.getLocation());
                    if (distanceSquared < minDistanceSquared) {
                        minDistanceSquared = distanceSquared;
                        nearestPlayer = player;
                    }
                }
            }
        }
        return nearestPlayer;
    }

}
