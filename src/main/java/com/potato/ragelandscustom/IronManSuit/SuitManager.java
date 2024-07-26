package com.potato.ragelandscustom.IronManSuit;

import com.potato.ragelandscustom.CC;
import com.potato.ragelandscustom.Commands.ItemGiver;
import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class SuitManager {
    private final Main main;

    public HashMap<UUID, Boolean> playerFlyStatus;
    public static NamespacedKey MK34 = new NamespacedKey(Main.getMainInstance(), "MK34");
    public static NamespacedKey MK50 = new NamespacedKey(Main.getMainInstance(), "MK50");
    public static NamespacedKey suitOn = new NamespacedKey(Main.getMainInstance(), "suitOn");
    public SuitManager(Main main) {
        this.main = main;
        Mark34ProtectionEnchant = main.getConfig().getInt("Mark34Protection");
        Mark50ProtectionEnchant = main.getConfig().getInt("Mark50Protection");
    }

    public void apply(Player player) {
        Data.buildingSuit.add(player);
        Delay.until(20 * 3, () -> {
            setBoots(player);
            giveAbilities(player);
            player.sendMessage(Chat.jarvis + "Suit percentage: 25%");
        });

        Delay.until(20 * 4, () -> {
            setChestplate(player);
            player.sendMessage(Chat.jarvis + "Suit percentage: 50%");
        });

        Delay.until(20 * 5, () -> {
            setLeggings(player);
            player.sendMessage(Chat.jarvis + "Suit percentage: 75%");
        });

        Delay.until(20 * 6, () -> {
            setHelmet(player);
            player.sendMessage(Chat.jarvis + "Suit percentage: 100%");
            Data.buildingSuit.remove(player);
        });
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        playerpdc.set(suitOn, PersistentDataType.BOOLEAN, true);
    }

    public void eject(Player player) {

        if (Data.buildingSuit.contains(player)) {
            Chat.msg(player, Chat.jarvis + "&4Suit not built yet!");
            return;
        }
        ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                .setName("&c&lLaser hands")
                .addLore("&fShoot explosive arrows")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        ItemStack Tracker = new ItemStackBuilder(Material.AMETHYST_SHARD)
                .setName("&d&lTracker")
                .addLore("&6Track Nearby Players")
                .addEnchant(Enchantment.VANISHING_CURSE, 1)
                .build();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        Chat.msg(player, Chat.jarvis + "&cEjected from suit!");
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        playerpdc.set(suitOn, PersistentDataType.BOOLEAN, false);
        player.setVelocity(new Vector(0, 1, -3));

        double x = player.getLocation().getX();
        double y = player.getLocation().getY() + 1;
        double z = player.getLocation().getZ() - 3;

        Location location = new Location(player.getWorld(), x, y, z);

        Delay.until(20, () -> player.getWorld().createExplosion(location, 0));
        player.setAllowFlight(false);
        player.setFlying(false);

        if (player.getInventory().contains(Laserhands)) {
            player.getInventory().removeItem(Laserhands);
        }
        if (player.getInventory().contains(Tracker)) {
            player.getInventory().removeItem(Tracker);
        }
        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemGiver.giveMark34(player);
        }
        else if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            ItemGiver.giveMark50(player);
        }
        playerpdc.set(MK34, PersistentDataType.BOOLEAN, false);
        playerpdc.set(MK50, PersistentDataType.BOOLEAN, false);
        Data.suitAssigned.remove(player, "MK1");
        Data.suitAssigned.remove(player, "MK42");

        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
    }
    static int Mark50ProtectionEnchant;
    static int Mark34ProtectionEnchant;
    public static void setHelmet(Player player) {
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            ItemStack helmet = Main.getHead("d14824d840fb9ae9acd6c4f0c917dbf52803a65314a3b771346e33907e4dee79");
            ItemMeta meta = helmet.getItemMeta();
            meta.setDisplayName(CC.translate("&#FF0048&lJ&#FF0E52&l.&#FF1C5C&lA&#FF2B67&l.&#FF3971&lR&#FF477B&l.&#FF5585&lV&#FF638F&l.&#FF7199&lI&#FF80A4&l.&#FF8EAE&lS &#FFAAC2&lM&#FFB8CC&la&#FFC6D6&lr&#FFD5E1&lk &#FFF1F5&l5&#FFFFFF&l0"));
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark50ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            helmet.setItemMeta(meta);
            player.getInventory().setHelmet(helmet);
            return;
        }

        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemStack helmet = Main.getHead("b4ee5d3797e339ba55a3ff936be16b26c9d83c173c8ba97c9881e41889c7c3bb");
            ItemMeta meta = helmet.getItemMeta();
            meta.setDisplayName(CC.translate("&#000000&lJ&#050505&l.&#0A0A0A&lA&#101010&l.&#151515&lR&#1A1A1A&l.&#1F1F1F&lV&#242424&l.&#292929&lI&#2F2F2F&l.&#343434&lS &#3E3E3E&lM&#434343&la&#484848&lr&#4E4E4E&lk &#585858&l3&#5D5D5D&l4"));
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);

            AttributeModifier knockbackResistanceModifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.1, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistanceModifier);

            AttributeModifier maxHealthModifier = new AttributeModifier(UUID.randomUUID(), "generic.max_health", 2.5, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, maxHealthModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark34ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            helmet.setItemMeta(meta);
            player.getInventory().setHelmet(helmet);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1));
        }
    }

    public static void setChestplate(Player player) {
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            ItemStack chestplate = new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setName("&8&lMark 50").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
            meta.setColor(Color.fromRGB(169, 45, 69));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.WILD);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark50ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            chestplate.setItemMeta(armorMeta);
            player.getInventory().setChestplate(chestplate);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
            return;
        }

        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemStack chestplate = new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setName("&8&lMark 34").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
            meta.setColor(Color.fromRGB(40, 32, 30));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.WILD);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);

            AttributeModifier knockbackResistanceModifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.1, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistanceModifier);

            AttributeModifier maxHealthModifier = new AttributeModifier(UUID.randomUUID(), "generic.max_health", 2.5, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, maxHealthModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark34ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            chestplate.setItemMeta(armorMeta);
            player.getInventory().setChestplate(chestplate);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
        }
    }

    public static void setLeggings(Player player) {
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            ItemStack leggings = new ItemStackBuilder(Material.LEATHER_LEGGINGS).setName("&8&lMark 50").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) leggings.getItemMeta();
            meta.setColor(Color.fromRGB(169, 45, 69));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.TIDE);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark50ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            leggings.setItemMeta(armorMeta);
            player.getInventory().setLeggings(leggings);
            return;
        }

        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemStack leggings = new ItemStackBuilder(Material.LEATHER_LEGGINGS).setName("&8&lMark 34").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) leggings.getItemMeta();
            meta.setColor(Color.fromRGB(40, 32, 30));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.TIDE);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);

            AttributeModifier knockbackResistanceModifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.1, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistanceModifier);

            AttributeModifier maxHealthModifier = new AttributeModifier(UUID.randomUUID(), "generic.max_health", 2.5, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, maxHealthModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark34ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            leggings.setItemMeta(armorMeta);
            player.getInventory().setLeggings(leggings);
        }
    }

    public static void setBoots(Player player) {
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            ItemStack boots = new ItemStackBuilder(Material.LEATHER_BOOTS).setName("&8&lMark 50").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
            meta.setColor(Color.fromRGB(169, 45, 69));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark50ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            boots.setItemMeta(armorMeta);
            player.getInventory().setBoots(boots);
            return;
        }

        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemStack boots = new ItemStackBuilder(Material.LEATHER_BOOTS).setName("&8&lMark 34").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
            meta.setColor(Color.fromRGB(40, 32, 30));
            ArmorMeta armorMeta = (ArmorMeta) meta;
            ArmorTrim armorTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.EYE);
            armorMeta.setTrim(armorTrim);
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);

            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);

            AttributeModifier knockbackResistanceModifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.1, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistanceModifier);

            AttributeModifier maxHealthModifier = new AttributeModifier(UUID.randomUUID(), "generic.max_health", 2.5, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, maxHealthModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Mark34ProtectionEnchant , true);
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            boots.setItemMeta(armorMeta);
            player.getInventory().setBoots(boots);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1));
        }
    }

    public static void giveAbilities(Player player) {
        PersistentDataContainer playerpdc = player.getPersistentDataContainer();
        if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
            return;
        }
        if (Boolean.TRUE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
            ItemStack Laserhands = new ItemStackBuilder(Material.BLAZE_POWDER)
                    .setName("&c&lLaser hands")
                    .addLore("&fShoot explosive arrows")
                    .addEnchant(Enchantment.VANISHING_CURSE, 1)
                    .build();
            ItemStack Tracker = new ItemStackBuilder(Material.AMETHYST_SHARD)
                    .setName("&d&lTracker")
                    .addLore("&6Track Nearby Players")
                    .addEnchant(Enchantment.VANISHING_CURSE, 1)
                    .build();

            if (player.getInventory().getItem(0) != null) {
                ItemStack oldItem = player.getInventory().getItem(0);
                player.getInventory().setItem(0, null);
                player.getInventory().setItem(0, Laserhands);
                player.getInventory().addItem(oldItem);
            }
            if (player.getInventory().getItem(1) != null) {
                ItemStack oldItem2 = player.getInventory().getItem(1);
                player.getInventory().setItem(1, null);
                player.getInventory().setItem(1, Tracker);
                player.getInventory().addItem(oldItem2);
            }
            if (player.getInventory().getItem(0) == null) {
                player.getInventory().setItem(0, Laserhands);
            }
            if (player.getInventory().getItem(1) == null) {
                player.getInventory().setItem(1, Tracker);
            }
        }
    }
}
