package com.potato.ragelandscustom.ItemFunctions;

import com.potato.ragelandscustom.Items.PDCKeys;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class ShieldDeflect implements Listener {
    private final Main main;
    private static final double REFLECT_CHANCE = 0.3;
    private final Random random = new Random();

    public ShieldDeflect(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            if (damaged.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                if (e.getDamager() instanceof Player) {
                    // Check if the player is blocking
                    double chance = 0.1; // 30% chance
                    Random random = new Random();
                    double randomNumber = random.nextDouble(); // Random number between 0 (inclusive) and 1 (exclusive)
                    ItemStack damagedShield = damaged.getInventory().getItemInOffHand();
                    ItemMeta damagedShieldMeta = damagedShield.getItemMeta();
                    PDCKeys pdcKeys = new PDCKeys(main);
                    NamespacedKey key = pdcKeys.getKey();
                    PersistentDataContainer damagedShieldPDC = null;
                    if (damagedShieldMeta != null) {
                        damagedShieldPDC = (damagedShieldMeta.getPersistentDataContainer());
                    }
                    Boolean damagedShieldBoolean = null;
                    if (damagedShieldPDC != null) {
                        damagedShieldBoolean = damagedShieldPDC.get(key, PersistentDataType.BOOLEAN);
                    }
                    if (damaged.isBlocking() && randomNumber < chance && Boolean.TRUE.equals(damagedShieldBoolean)) {
                        // Log or perform actions on shield deflect
                        double damageDealt = e.getDamage();
                        Player attacker = (Player) e.getDamager();
                        attacker.damage(damageDealt, damaged);
                    }
                }
                else if (e.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) e.getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        Player shooter = (Player) projectile.getShooter();
                        ItemStack damagedShield = damaged.getInventory().getItemInOffHand();
                        ItemMeta damagedShieldMeta = damagedShield.getItemMeta();
                        PDCKeys pdcKeys = new PDCKeys(main);
                        NamespacedKey key = pdcKeys.getKey();
                        PersistentDataContainer damagedShieldPDC = null;
                        if (damagedShieldMeta != null) {
                            damagedShieldPDC = (damagedShieldMeta.getPersistentDataContainer());
                        }
                        Boolean damagedShieldBoolean = null;
                        if (damagedShieldPDC != null) {
                            damagedShieldBoolean = damagedShieldPDC.get(key, PersistentDataType.BOOLEAN);
                        }
                        // Determine if we should reflect the damage
                        if (damaged.isBlocking() && random.nextDouble() < REFLECT_CHANCE && damagedShieldBoolean) {
                            // Reflect the damage back to the shooter
                            double damage = e.getDamage();
                            reflectDamage(shooter, damage);

                        }
                    }
                }
            }
        }
    }
    private void reflectDamage(Player shooter, double damage) {
        shooter.damage(damage);
    }
}