package com.potato.ragelandscustom.Functions;

import com.potato.ragelandscustom.DataManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BossDropItem implements Listener {

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        LivingEntity deathEntity = e.getEntity();
        if (!(deathEntity instanceof Player)) {
            if (deathEntity.getType() == EntityType.ENDER_DRAGON || deathEntity.getType() == EntityType.WITHER) {
                Player killer = deathEntity.getKiller();
                if (killer != null) {
                        ItemStack powerOrbFragment = DataManager.getInstance().getPowerOrbFragment();
                        killer.getInventory().addItem(powerOrbFragment);
                    }
                }
            }
        }
    }
