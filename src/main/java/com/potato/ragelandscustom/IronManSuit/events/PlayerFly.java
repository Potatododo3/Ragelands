package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFly implements Listener {

    private final Main main;
    private static final float MIN_SPEED = 1.0f;
    private static final float MAX_SPEED = 5.0f;
    private static final float SPEED_STEP = 0.25f;

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

        float speed = Data.playerSpeed.getOrDefault(player, MIN_SPEED);
        // Apply the speed directly to the player's velocity
        player.setVelocity(player.getLocation().getDirection().multiply(speed / MAX_SPEED));
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
            }.runTaskTimer(main, 0L, 10L); // Schedule the task to run every 10 ticks (0.5 seconds)
        } else {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        if (item.isSimilar(Main.speedIncreaseItem)) {
            increaseSpeed(player);
        } else if (item.isSimilar(Main.speedDecreaseItem)) {
            decreaseSpeed(player);
        } else if (item.isSimilar(Main.hoverItem)) {
            toggleHover(player);
        }
    }

    private void increaseSpeed(Player player) {
        float currentSpeed = Data.playerSpeed.getOrDefault(player, MIN_SPEED);
        float newSpeed = Math.min(currentSpeed + SPEED_STEP, MAX_SPEED);
        Data.playerSpeed.put(player, newSpeed);
        player.setFlySpeed(newSpeed); // Update the player's fly speed
        player.setFlying(false);
        player.sendMessage("Speed increased to: " + newSpeed);
    }

    private void decreaseSpeed(Player player) {
        float currentSpeed = Data.playerSpeed.getOrDefault(player, MIN_SPEED);
        float newSpeed = Math.max(currentSpeed - SPEED_STEP, MIN_SPEED);
        Data.playerSpeed.put(player, newSpeed);
        player.setFlySpeed(newSpeed); // Update the player's fly speed
        player.setFlying(false);
        player.sendMessage("Speed decreased to: " + newSpeed);
    }

    private void toggleHover(Player player) {
        if (!player.isGliding()) {
            player.sendMessage("You need to be gliding to hover.");
            return;
        }

        if (Data.isHovering.contains(player)) {
            // Disable hovering
            Data.isHovering.remove(player);
            player.sendMessage("You are no longer hovering.");
        } else {
            // Enable hovering
            Data.isHovering.add(player);
            player.sendMessage("You are now hovering.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !Data.isHovering.contains(player)) {
                        this.cancel();
                        return;
                    }
                    // Reset the player's vertical velocity to zero to maintain hover
                    player.setVelocity(player.getVelocity().setY(0));
                }
            }.runTaskTimer(main, 0L, 1L); // Schedule the task to run every tick
        }
    }
}