package com.potato.ragelandscustom.ItemFunctions;

import com.potato.ragelandscustom.Items.PDCKeys;
import com.potato.ragelandscustom.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class LongRangeBow implements Listener {
    private final Main main;

    public LongRangeBow(Main main) {
        this.main = main;
    }
    @EventHandler
    public void onProjectileLaunch(EntityShootBowEvent e) {
        // Check if the projectile is an arrow
        if (e.getEntity() instanceof Player) {
            if (e.getProjectile() instanceof Arrow) {
                PDCKeys pdcKeys = new PDCKeys(main);
                NamespacedKey key = pdcKeys.getKey();
                Arrow arrow = (Arrow) e.getProjectile();
                ItemStack bow = e.getBow();
                if (Objects.requireNonNull(bow).getItemMeta() != null) {
                    Boolean bowBoolean = bow.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN);
                    if (bowBoolean != null) {
                        if (bowBoolean) {
                            // Get the current velocity of the arrow
                            Vector currentVelocity = arrow.getVelocity();

                            // Modify the velocity (e.g., double the speed)
                            Vector newVelocity = currentVelocity.multiply(1.3);

                            // Set the new velocity
                            arrow.setVelocity(newVelocity);
                        }
                    }
                }
            }
        }
    }
}

