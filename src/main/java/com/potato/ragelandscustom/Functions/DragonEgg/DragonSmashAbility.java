package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonSmashAbility implements Listener {

    private final Main main;

    public DragonSmashAbility(Main main) {
        this.main = main;
    }

    public void triggerAbility(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setVelocity(player.getVelocity().setY(1)); // Launch the player up
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnGround()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnGround()) {
                                        // Create shockwave effect
                                        player.getWorld().createExplosion(player.getLocation(), 0, false, false);
                                        for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                                            entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5));
                                            if (entity instanceof Player) {
                                                ((Player) entity).damage(10);
                                            }
                                        }
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(main, 0, 1);
                        }
                    }
                }.runTaskLater(main, 20); // Delay to check if the player is falling
            }
        }.runTaskLater(main, 20); // Delay to check if the player continues sneaking
    }
}