package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFly implements Listener {

    private final Main main;
    private static final double MIN_SPEED = 0.1;
    private static final double MAX_SPEED = 2.0;
    private static final double SPEED_STEP = 0.2;

    public PlayerFly(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            // Check if player is in creative mode
            if (player.getGameMode() == GameMode.CREATIVE) {
                return; // Allow normal creative flight
            }

            // Allow normal elytra gliding if player doesn't have the suit
            if (!Data.Suit.contains(player)) {
                return;
            }

            // Custom suit gliding logic
            if (!player.getAllowFlight() && Data.Suit.contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        // Check if player is in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow normal creative flight
        }

        // Allow normal elytra flying if player doesn't have the suit
        if (!Data.Suit.contains(player)) {
            return;
        }

        // Custom suit movement logic
        if (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            player.setAllowFlight(true);
            player.setFlying(false);
            if (Data.Suit.contains(player)) {
                Data.isGliding.remove(player);
            }
        }
    }

    void boostPlayer(Player player) {
        if (!player.isGliding()) {
            return;
        }
        player.boostElytra(new ItemStack(Material.FIREWORK_ROCKET));
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();

        // Check if player is in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow normal creative flight
        }

        // Allow normal elytra flying if player doesn't have the suit
        if (!Data.Suit.contains(player)) {
            return;
        }

        // Custom suit flying logic
        if (e.isFlying() && Data.Suit.contains(player)) {
            player.setAllowFlight(false);
            player.setGliding(true);
            Data.isGliding.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Data.Suit.contains(player) && Data.isGliding.contains(player)) {
                        boostPlayer(player);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(main, 0L, 12L); // Schedule the task to run every 10 ticks (0.5 seconds)
        } else {
            e.setCancelled(true);
        }
    }
}