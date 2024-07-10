package com.potato.rlcustomitems.Items;

import com.potato.rlcustomitems.Items.PDCKeys;
import com.potato.rlcustomitems.CC;
import com.potato.rlcustomitems.Main;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemFragBomb {

    private final Main main;

    public ItemFragBomb(Main main) {
        this.main = main;
    }

    public ItemStack getFragBomb() {
        PDCKeys pdcKeys = new PDCKeys(main);
        NamespacedKey key = pdcKeys.getKey();
        ItemStack fragBomb = new ItemStack(Material.FIREWORK_ROCKET, 1);
        FireworkMeta meta = (FireworkMeta) fragBomb.getItemMeta();
        if (meta != null) {
            meta.setPower(1);
            meta.addEffects(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withTrail().withColor(Color.RED).withFlicker().build());
            meta.setDisplayName(CC.translate("&#B32A18&lF&#B04532&lr&#AD5F4C&la&#AA7A66&lg &#A4AF99&lb&#A1CAB3&lo&#9EE4CD&lm&#9BFFE7&lb"));
            PersistentDataContainer fragBombPDC = meta.getPersistentDataContainer();
            fragBombPDC.set(key, PersistentDataType.BOOLEAN, true);
        }
        fragBomb.setItemMeta(meta);
        return fragBomb;
    }

}
