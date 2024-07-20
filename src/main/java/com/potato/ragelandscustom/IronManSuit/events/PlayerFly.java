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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class PlayerFly implements Listener {

    private final Main main;

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
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            // Allow normal elytra gliding if player doesn't have the suit
            if (Boolean.FALSE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                return;
            }

            // Custom suit gliding logic
            if (!player.getAllowFlight() && Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
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
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        // Allow normal elytra flying if player doesn't have the suit
        if (Boolean.FALSE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
            return;
        }

        // Custom suit movement logic
        if (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                player.setAllowFlight(true);
                player.setFlying(false);
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
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        // Allow normal elytra flying if player doesn't have the suit
        if (Boolean.FALSE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
            return;
        }

        // Custom suit flying logic
        if (e.isFlying() && Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
            player.setAllowFlight(false);
            player.setGliding(true);
            Data.isGliding.add(player);
            long ticks = 0;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN)) && player.isGliding()) {
                        boostPlayer(player);
                    }
                    else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(main, 0L, 12L); // Schedule the task to run every 10 ticks (0.5 seconds)
        } else {
            e.setCancelled(true);
        }
    }
}