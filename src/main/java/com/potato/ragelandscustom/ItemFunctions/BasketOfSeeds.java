package com.potato.ragelandscustom.ItemFunctions;

import com.potato.ragelandscustom.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BasketOfSeeds implements Listener {
    private final Main main;

    public BasketOfSeeds(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND  && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.PLAYER_HEAD && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Basket of Seeds")) {
                plantSeeds(event.getClickedBlock(), event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    private void plantSeeds(Block block, Player player) {
        Material[] seedTypes = {
                Material.WHEAT_SEEDS,
                Material.BEETROOT_SEEDS,
                Material.PUMPKIN_SEEDS,
                Material.MELON_SEEDS
        };

        final Material chosenSeedType;
        final int seedCount;

        Material tempSeedType = null;
        int tempSeedCount = 0;

        for (Material seedType : seedTypes) {
            tempSeedCount = countSeeds(player, seedType);
            if (tempSeedCount >= 1) {
                tempSeedType = seedType;
                break;
            }
        }

        if (tempSeedType == null) {
            player.sendMessage("§cYou need at least 1 seed to use the Basket of Seeds.");
            return;
        }

        chosenSeedType = tempSeedType;
        seedCount = tempSeedCount;

        player.sendMessage("§aStarting to plant seeds...");

        new BukkitRunnable() {
            int distance = 0;
            int seedsPlanted = 0;
            double yaw = player.getLocation().getYaw();

            // Determine direction
            int dx = 0;
            int dz = 0;

            @Override
            public void run() {
                if (yaw >= -45.0 && yaw < 45.0) {
                    dz = 1; // South
                }
                else if (yaw >= 45.0 && yaw < 135.0) {
                    dx = -1; // West
                }
                else if (yaw >= 135.0 || yaw < -135.0) {
                    dz = -1; // North
                }
                else if (yaw >= -135.0 && yaw < -45.0) {
                    dx = 1; // East
                }
                Block targetBlock = block.getRelative(dx * distance, 0, dz * distance);
                if (targetBlock.getType() == Material.FARMLAND && seedsPlanted < seedCount) {
                    Block cropBlock = targetBlock.getRelative(0, 1, 0);
                    if (cropBlock.getType() == chosenSeedType) return;
                    cropBlock.setType(getCropType(chosenSeedType));

                    // Add particle effect
                    cropBlock.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, cropBlock.getLocation().add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5, 0);

                    removeSeedFromInventory(player, chosenSeedType, 1);
                    seedsPlanted++;
                    distance++;
                }
                else {
                    if (targetBlock.getType() != Material.FARMLAND) {
                        player.sendMessage("§cStopped planting: not farmland");
                    } else {
                        player.sendMessage("§cStopped planting: ran out of seeds.");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 5); // Runs every 5 ticks (1/4 second)
    }
    private int countSeeds(Player player, Material seedType) {
        int count = 0;
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() == seedType) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private void removeSeedFromInventory(Player player, Material seedType, int amount) {
        Map<Integer, ? extends ItemStack> items = player.getInventory().all(seedType);
        for (Map.Entry<Integer, ? extends ItemStack> entry : items.entrySet()) {
            ItemStack item = entry.getValue();
            int newAmount = item.getAmount() - amount;
            if (newAmount > 0) {
                item.setAmount(newAmount);
                break;
            } else {
                player.getInventory().clear(entry.getKey());
                amount = -newAmount;
                if (amount == 0) break;
            }
        }
    }

    private Material getCropType(Material seedType) {
        switch (seedType) {
            case WHEAT_SEEDS:
                return Material.WHEAT;
            case BEETROOT_SEEDS:
                return Material.BEETROOTS;
            case PUMPKIN_SEEDS:
                return Material.PUMPKIN_STEM;
            case MELON_SEEDS:
                return Material.MELON_STEM;
            default:
                return Material.AIR;
        }
    }
}
