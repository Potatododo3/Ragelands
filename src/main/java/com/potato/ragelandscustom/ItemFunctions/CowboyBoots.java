package com.potato.ragelandscustom.ItemFunctions;

import com.potato.ragelandscustom.Items.PDCKeys;
import com.potato.ragelandscustom.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class CowboyBoots implements Listener {
    private final Main main;

    public CowboyBoots(Main main) {
        this.main = main;
    }

    private boolean hasCowboyBoots(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null && boots.getType() == Material.GOLDEN_BOOTS;
    }
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (e.getHand() == EquipmentSlot.HAND) {
            Player player = e.getPlayer();
            Entity entity = e.getRightClicked();
            if (hasCowboyBoots(player)) {
                PDCKeys pdcKeys = new PDCKeys(main);
                NamespacedKey key = pdcKeys.getCustomItemKey();
                ItemStack playerBoots = player.getInventory().getBoots();
                if (playerBoots != null) {
                    Boolean bootsPDC = Objects.requireNonNull(playerBoots.getItemMeta()).getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN);
                    if (Boolean.TRUE.equals(bootsPDC)) {
                        if (player.isSneaking()) {
                            handleSneaking(player, entity);
                        } else {
                            handleNotSneaking(player, entity);
                        }
                    }
                }
            }
        }
    }

    private void handleSneaking(Player player, Entity entity) {
        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER) {
            player.sendMessage(ChatColor.RED + "You cannot get dragons or withers on top of you!");
        } else if (entity instanceof LivingEntity) {
            if (player.getPassengers().isEmpty()) {
                if (entity != player) {  // Ensure the entity is not the player itself
                    player.addPassenger(entity);
                }
            } else {
                LivingEntity topEntity = getTopmostPassenger(player);
                if (entity != topEntity) {  // Ensure the entity is not already a top passenger
                    topEntity.addPassenger(entity);
                }
            }
        }
    }

    private void handleNotSneaking(Player player, Entity entity) {
        if (entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER) {
            player.sendMessage(ChatColor.RED + "You cannot ride dragons or withers!");
        } else if (entity instanceof LivingEntity) {
            if (entity.getPassengers().isEmpty()) {
                if (entity != player) {  // Ensure the entity is not the player itself
                    entity.addPassenger(player);
                }
            } else {
                LivingEntity topEntity = getTopmostPassenger((LivingEntity) entity);
                if (player != topEntity) {  // Ensure the player is not already a top passenger
                    topEntity.addPassenger(player);
                }
            }
        }
    }

    private LivingEntity getTopmostPassenger(LivingEntity entity) {
        LivingEntity currentEntity = entity;
        while (!currentEntity.getPassengers().isEmpty()) {
            currentEntity = (LivingEntity) currentEntity.getPassengers().get(0);
        }
        return currentEntity;
    }
}
