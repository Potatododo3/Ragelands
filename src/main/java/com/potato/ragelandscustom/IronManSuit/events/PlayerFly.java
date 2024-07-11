package com.potato.ragelandscustom.IronManSuit.events;

import com.potato.ragelandscustom.IronManSuit.Data;
import com.potato.ragelandscustom.Main;
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

    public PlayerFly(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        //Bukkit.broadcastMessage(e.isGliding() + " ");
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!player.getAllowFlight()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(false);
            Player player = e.getPlayer();
            if (Data.Suit.contains(player)) {
                Data.isGliding.remove(player);
            }
        }
    }
    void boostPlayer(Player player) {
        if(!player.isGliding())
            return;

        player.fireworkBoost(new ItemStack(Material.FIREWORK_ROCKET));
    }
    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        if (e.isFlying() && Data.Suit.contains(e.getPlayer())) {
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setGliding(true);
            Data.isGliding.add(e.getPlayer());
            Player player = e.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Data.Suit.contains(player) && Data.isGliding.contains(player)) {
                        boostPlayer(player);
                    }
                    else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(main, 0L, 10L); // Schedule the task to run every 20 ticks (1 second)
        }
    }
}
