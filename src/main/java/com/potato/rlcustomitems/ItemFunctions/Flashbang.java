package com.potato.rlcustomitems.ItemFunctions;

import com.potato.rlcustomitems.Items.PDCKeys;
import com.potato.rlcustomitems.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Flashbang implements Listener {
    private final Main main;
    private final JavaPlugin plugin;

    public Flashbang(Main main, JavaPlugin plugin) {
        this.main = main;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerDropItemEvent event) {
        Item droppedItem = event.getItemDrop();
        if (droppedItem.getItemStack().getType() == Material.SNOWBALL) {
            Player player = event.getPlayer();
            ItemStack item = null;
            if (event.getItemDrop().getItemStack() != null) {
                item = event.getItemDrop().getItemStack();
            }
            ItemMeta meta = null;
            if (item.getItemMeta() != null) {
                meta = item.getItemMeta();
            }
            PDCKeys pdcKeys = new PDCKeys(main);
            NamespacedKey key = pdcKeys.getKey();
            PersistentDataContainer flashBangPDC = null;
            if (meta.getPersistentDataContainer() != null) {
                flashBangPDC = meta.getPersistentDataContainer();
                Boolean flashBangBoolean = flashBangPDC.get(key, PersistentDataType.BOOLEAN);
                if (!(flashBangBoolean == null)) {
                    if (flashBangBoolean) {
                        Location flashbangLocation = droppedItem.getLocation();
                        for (Player target : player.getWorld().getPlayers()) {
                            if (target.getLocation().distance(flashbangLocation) < 10) {
                                if (isLookingAt(target, flashbangLocation, 45)) {
                                    target.playSound(target.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
                                }
                            }
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player target : player.getWorld().getPlayers()) {
                                    if (target.getLocation().distance(flashbangLocation) < 10) {
                                        if (isLookingAt(target, flashbangLocation, 65)) {
                                            sendFlashbangEffect(target);
                                            droppedItem.remove();
                                        }
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 40); // 40 ticks = 2 seconds
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Item droppedItemEntity = e.getItem();
            ItemStack droppedItem = droppedItemEntity.getItemStack();
            PDCKeys pdcKeys = new PDCKeys(main);
            NamespacedKey key = pdcKeys.getKey();
            if (droppedItem.getType() == Material.SNOWBALL && droppedItem.hasItemMeta() && Boolean.TRUE.equals(droppedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN))) {
                e.setCancelled(true);
            }
        }
    }
    private boolean isLookingAt(Player player, Location targetLocation, double maxAngle) {
        Location eyeLocation = player.getEyeLocation();
        Vector toTarget = targetLocation.toVector().subtract(eyeLocation.toVector());
        Vector direction = eyeLocation.getDirection();

        double angle = direction.angle(toTarget);
        return angle < Math.toRadians(maxAngle);
    }

    private void sendFlashbangEffect(Player player) {
        PotionEffect slowness = new PotionEffect(PotionEffectType.BLINDNESS, 120, 8, true, true);
        PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 120, 3, true, true);
        player.addPotionEffect(slowness);
        player.addPotionEffect(nausea);
    }
}
