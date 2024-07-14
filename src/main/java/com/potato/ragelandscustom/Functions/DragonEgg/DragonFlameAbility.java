package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class DragonFlameAbility implements Listener {

    private final Main main;

    public DragonFlameAbility(Main main) {
        this.main = main;
    }

    public void triggerAbility(Player player) {
        Vector direction = player.getLocation().getDirection();
        for (int i = 0; i < 10; i++) {
            Vector particleLocation = player.getLocation().toVector().add(direction.clone().multiply(i * 0.5));
            player.getWorld().spawnParticle(Particle.FLAME, particleLocation.toLocation(player.getWorld()), 5, 0.1, 0.1, 0.1, 0.01);
            player.getWorld().getNearbyEntities(particleLocation.toLocation(player.getWorld()), 0.5, 0.5, 0.5).forEach(entity -> {
                if (entity != player) {
                    entity.setFireTicks(100);
                }
            });
        }
    }
}
