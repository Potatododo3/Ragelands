package com.potato.ragelandscustom.ItemFunctions;

import com.potato.ragelandscustom.Items.PDCKeys;
import com.potato.ragelandscustom.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class FragBomb implements Listener {
    private final Main main;
    public FragBomb(Main main) {
        this.main = main;
    }
    private final Random random = new Random();
    private final Map<UUID, ItemStack> fireworkMap = new HashMap<>();
    @EventHandler
    public void onFireworkLaunch(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = null;
            if (event.getItem() != null) {
                item = event.getItem();
            }
            ItemMeta meta = null;
            if (item != null && item.getItemMeta() != null) {
                meta = item.getItemMeta();
            }
            PDCKeys pdcKeys = new PDCKeys(main);
            NamespacedKey key = pdcKeys.getKey();
            Boolean fragBombBoolean = null;
            if (meta != null) {
                fragBombBoolean = meta.getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN);
            }
            String playerName = event.getPlayer().getName();
            if (item != null && item.getType() == Material.FIREWORK_ROCKET && Boolean.TRUE.equals(fragBombBoolean)) {
                event.setCancelled(true);
                if (CooldownManager.isOnFragbombCooldown(playerName)) {
                    double remainingTime = (double) CooldownManager.getFragbombCooldown(playerName) / 50;
                    player.sendMessage("Ability on cooldown! " + remainingTime + " seconds remaining.");
                    return;
                }
                // Store the item in the map with a unique ID
                Location clickedBlockLocation = event.getClickedBlock().getLocation();
                clickedBlockLocation.setY(clickedBlockLocation.getY() + 1);
                Firework firework = (Firework) event.getPlayer().getWorld().spawnEntity(clickedBlockLocation, EntityType.FIREWORK);
                if (item.getItemMeta() != null) {
                    firework.setFireworkMeta((FireworkMeta) item.getItemMeta());
                }
                fireworkMap.put(firework.getUniqueId(), item);
                event.setCancelled(true);
                int ItemAmount = item.getAmount();
                item.setAmount(ItemAmount-1);
                CooldownManager.setFragbombCooldown(playerName, 20);
            }
        }
    }
    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();
        UUID id = firework.getUniqueId();

        if (fireworkMap.containsKey(id)) {
            ItemStack item = fireworkMap.get(id);
            // Remove the firework from the map after explosion
            fireworkMap.remove(id);

            Location location = firework.getLocation();

            for (int i = 0; i < 5; i++) {
                // Spawn TNT at the explosion location
                TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);

                // Apply a small random velocity to spread the TNT
                double x = random.nextDouble() * 2 - 1; // Random double between -1 and 1
                double z = random.nextDouble() * 2 - 1; // Random double between -1 and 1
                Vector velocity = new Vector(x, 0.5, z); // Y-velocity to ensure it spreads outwards
                tnt.setVelocity(velocity);
            }
        }
    }
}