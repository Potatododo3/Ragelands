package com.potato.ragelandscustom.ItemFunctions.Stinger;

import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowHitListener implements Listener {

    private final Main main;
    private final ArrowFireListener arrowFireListener;

    public ArrowHitListener(Main main, ArrowFireListener arrowFireListener) {
        this.arrowFireListener = arrowFireListener;
        this.main = main;
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        Projectile storedProjectile = arrowFireListener.getArrow(projectile.getUniqueId());

        if (storedProjectile != null) {
            if (event.getHitEntity() instanceof Player) {
                Player player = (Player) event.getHitEntity();

                new BukkitRunnable() {
                    private long timeElapsed = 0L;

                    @Override
                    public void run() {
                        timeElapsed += 5;

                        if (timeElapsed >= 120) {
                            this.cancel();
                        }

                        if (player.isGliding()) {
                            player.setGliding(false);
                            player.damage(23);
                        }

                        if (player.isInsideVehicle()) {
                            player.leaveVehicle();
                            player.damage(23);
                        }
                    }
                }.runTaskTimer(main, 0, 5L);
            }
        }
    }
}
