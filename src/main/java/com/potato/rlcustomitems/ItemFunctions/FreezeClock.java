package com.potato.rlcustomitems.ItemFunctions;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.potato.rlcustomitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class FreezeClock implements Listener {

    private final Main main;
    private final Set<String> frozenPlayers = new HashSet<>();
    private final CooldownManager cooldownManager;

    public FreezeClock(Main main) {
        this.main = main;
        this.cooldownManager = new CooldownManager();
    }

    @EventHandler
    public void onPlayerUseFreezeClock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR && Objects.requireNonNull(event.getItem()).getType() == Material.CLOCK) {
            if (isFreezeClock(event.getItem())) {
                Player player = event.getPlayer();
                String playerName = player.getName();

                if (cooldownManager.isOnFreezeClockCooldown(playerName)) {
                    float remainingTime = (float) CooldownManager.getFreezeClockCooldown(playerName) / 50;
                    player.sendMessage("Ability on cooldown! " + remainingTime + " seconds remaining.");
                    return;
                }

                cooldownManager.setFreezeClockCooldowns(playerName, 45);

                for (Player nearbyPlayer : getNearbyPlayers(player, 7)) {
                    if (!isInSameFaction(player, nearbyPlayer)) {
                        freezePlayer(nearbyPlayer, 4);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (frozenPlayers.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    private boolean isFreezeClock(ItemStack item) {
        if (item.getType().equals(Material.CLOCK)) {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getItemMeta().getLore().contains(ChatColor.GRAY + "Custom Item")) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isInSameFaction(Player player1, Player nearbyPlayer) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player1);
        FPlayer nearbyfPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
        if (fPlayer.getFaction().equals(nearbyfPlayer.getFaction())) {
            return true;
        }
        else {
            return false;
        }

    }
    private Set<Player> getNearbyPlayers(Player player, int radius) {
        Set<Player> nearbyPlayers = new HashSet<>();
        for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
            if (nearbyPlayer.getWorld().equals(player.getWorld()) && nearbyPlayer.getLocation().distance(player.getLocation()) <= radius) {
                nearbyPlayers.add(nearbyPlayer);
            }
        }
        return nearbyPlayers;
    }

    private void freezePlayer(Player player, int seconds) {
        String playerName = player.getName();
        frozenPlayers.add(playerName);

        new BukkitRunnable() {
            @Override
            public void run() {
                frozenPlayers.remove(playerName);
            }
        }.runTaskLater(main, seconds * 20L);
    }

}
