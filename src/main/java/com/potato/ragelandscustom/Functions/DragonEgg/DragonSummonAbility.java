package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonSummonAbility implements Listener {

    private final Main main;
    private Long timeElapse = 0L;
    public DragonSummonAbility(Main main) {
        this.main = main;
    }

    public void triggerAbility(Player player) {
        // Summon the Vex
        Vex dragonPet = (Vex) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLAZE);
        dragonPet.setCustomName("Dragon Pet");
        dragonPet.setCustomNameVisible(true);
        dragonPet.setSilent(true); // Mute the Vex

        new BukkitRunnable() {
            @Override
            public void run() {
                if (timeElapse >= 300) {
                    dragonPet.remove();
                    this.cancel();
                    return;
                }

                if (!dragonPet.isValid() || !player.isOnline()) {
                    dragonPet.remove();
                    cancel();
                    return;
                }

                // Follow the player
                dragonPet.teleport(player.getLocation());

                // Find nearby hostile mobs and set the Vex's target
                for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity instanceof Player) {
                            dragonPet.setTarget(livingEntity);
                            break;
                        }
                    }
                }
                timeElapse = timeElapse + 40;
                // Ensure the Vex doesn't attack the player
                if (dragonPet.getTarget() == player) {
                    dragonPet.setTarget(null);
                }
            }
        }.runTaskTimer(main, 0, 40L);
    }
}
