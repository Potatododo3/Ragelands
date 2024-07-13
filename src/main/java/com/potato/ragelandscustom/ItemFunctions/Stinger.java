package com.potato.ragelandscustom.ItemFunctions;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.potato.ragelandscustom.Main;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Stinger implements Listener {

    private final Main main;
    private final NamespacedKey fireworkKey;
    private final Gson gson = new Gson();
    private final double maxHomingDistance = 65.0; // Configurable homing distance

    public Stinger(Main main) {
        this.main = main;
        this.fireworkKey = new NamespacedKey(main, "stinger_firework");
    }

    @EventHandler
    public void onEntityLoadCrossbow(EntityLoadCrossbowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ItemStack crossbow = event.getCrossbow();

        if (crossbow != null && crossbow.getItemMeta() != null &&
                "FIM-92 Stinger".equals(crossbow.getItemMeta().getDisplayName())) {

            CrossbowMeta meta = (CrossbowMeta) crossbow.getItemMeta();
            for (ItemStack projectile : meta.getChargedProjectiles()) {
                if (projectile.getType() == Material.FIREWORK_ROCKET) {
                    FireworkMeta fireworkMeta = (FireworkMeta) projectile.getItemMeta();
                    Map<String, Object> fireworkMetaMap = fireworkMeta.serialize();
                    String fireworkJson = gson.toJson(fireworkMetaMap);
                    PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                    dataContainer.set(fireworkKey, PersistentDataType.STRING, fireworkJson);
                    crossbow.setItemMeta(meta);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getItemMeta() != null &&
                "FIM-92 Stinger".equals(item.getItemMeta().getDisplayName())) {

            // Release the charged crossbow
            if (event.getAction().toString().contains("LEFT_CLICK_AIR") || event.getAction().toString().contains("LEFT_CLICK_BLOCK")) {
                CrossbowMeta crossbowMeta = (CrossbowMeta) item.getItemMeta();
                if (crossbowMeta != null && crossbowMeta.hasChargedProjectiles()) {
                    PersistentDataContainer dataContainer = crossbowMeta.getPersistentDataContainer();
                    if (dataContainer.has(fireworkKey, PersistentDataType.STRING)) {
                        String fireworkJson = dataContainer.get(fireworkKey, PersistentDataType.STRING);
                        Type type = new TypeToken<Map<String, Object>>() {}.getType();
                        Map<String, Object> fireworkMetaMap = gson.fromJson(fireworkJson, type);

                        FireworkMeta fireworkMeta = getFireworkMetaFromMap(fireworkMetaMap);

                        Firework firework = (Firework) player.getWorld().spawn(player.getEyeLocation(), Firework.class);
                        firework.setFireworkMeta(fireworkMeta);
                        firework.setMetadata("StingerMissile", new FixedMetadataValue(main, true));
                        firework.setVelocity(player.getLocation().getDirection().multiply(2));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (firework.isDead()) {
                                    this.cancel();
                                    return;
                                }
                                Entity target = findNearestFlyingEntity(firework, maxHomingDistance);
                                if (target != null) {
                                    Vector direction = target.getLocation().toVector().subtract(firework.getLocation().toVector()).normalize();
                                    firework.setVelocity(direction.multiply(2));
                                }
                            }
                        }.runTaskTimer(main, 0L, 1L);
                    }
                }
            }
        }
    }

    private FireworkMeta getFireworkMetaFromMap(Map<String, Object> fireworkMetaMap) {
        ItemStack fireworkItem = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = (FireworkMeta) fireworkItem.getItemMeta();

        List<Map<String, Object>> effects = (List<Map<String, Object>>) fireworkMetaMap.get("effects");
        if (effects != null) {
            for (Map<String, Object> effectMap : effects) {
                FireworkEffect effect = (FireworkEffect) FireworkEffect.deserialize(effectMap);
                fireworkMeta.addEffect(effect);
            }
        }

        Integer power = (Integer) fireworkMetaMap.get("power");
        if (power != null) {
            fireworkMeta.setPower(power);
        }

        return fireworkMeta;
    }

    private Entity findNearestFlyingEntity(Entity source, double maxDistance) {
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;
        for (Entity entity : source.getWorld().getEntities()) {
            if (entity instanceof Player && ((Player) entity).isFlying()) {
                double distance = entity.getLocation().distance(source.getLocation());
                if (distance < closestDistance && distance <= maxDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }
}
