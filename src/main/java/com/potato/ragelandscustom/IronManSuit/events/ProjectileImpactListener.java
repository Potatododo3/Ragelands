package com.potato.ragelandscustom.IronManSuit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileImpactListener implements Listener {
    private final JavaPlugin plugin;

    public ProjectileImpactListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("ironman_projectile")) {

            event.getEntity().remove();

            // Check if it hit a player
            if (event.getHitEntity() instanceof Player) {
                Player hitPlayer = (Player) event.getHitEntity();

                // Apply glowing effect
                hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));

                // Set player on fire
                hitPlayer.setFireTicks(20 * 5); // 5 seconds of fire
            }
            if (event.getHitBlock() != null) {
                event.getHitBlock().getWorld().createExplosion(event.getHitBlock().getLocation(), 3.0F, false, false);
            }
        }
    }
}