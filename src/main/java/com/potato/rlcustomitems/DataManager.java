package com.potato.rlcustomitems;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DataManager {

    // Single instance of DataManager
    private static DataManager instance;

    // The ItemStack to be shared
    private ItemStack magicBoots;
    private ItemStack deflectingShield;
    private ItemStack longRangeBow;
    private ItemStack fragBomb;
    private ItemStack flashBang;
    private ItemStack cowboyBoots;
    private ItemStack FreezeClock;
    private ItemStack powerOrbFragment;
    private HashMap<UUID, UUID> projectileMap = new HashMap<>();
    // Private constructor to prevent instantiation
    private DataManager() {}

    // Method to get the single instance of DataManager
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // Method to set the ItemStack
    public void setMagicBoots(ItemStack magicBoots) {
        this.magicBoots = magicBoots;
    }

    // Method to get the ItemStack
    public ItemStack getMagicBoots() {
        return this.magicBoots;
    }
    public void setDeflectingShield(ItemStack deflectingShield) {
        this.deflectingShield = deflectingShield;
    }

    // Method to get the ItemStack
    public ItemStack getDeflectingShield() {
        return this.deflectingShield;
    }

    public void setLongRangeBow(ItemStack longRangeBow) {
        this.longRangeBow = longRangeBow;
    }

    // Method to get the ItemStack
    public ItemStack getLongRangeBow() {
        return this.longRangeBow;
    }
    public void setFragBomb(ItemStack fragBomb) {
        this.fragBomb = fragBomb;
    }

    // Method to get the ItemStack
    public ItemStack getFragBomb() {
        return this.fragBomb;
    }
    public void setFlashbang(ItemStack flashBang) {
        this.flashBang = flashBang;
    }

    // Method to get the ItemStack
    public ItemStack getFlashBang() {
        return this.flashBang;
    }
    public void setCowboyBoots(ItemStack cowboyBoots) {
        this.cowboyBoots = cowboyBoots;
    }
    public ItemStack getCowboyBoots() {
        return this.cowboyBoots;
    }
    public void setPowerOrbFragment(ItemStack powerOrbFragment) {
        this.powerOrbFragment = powerOrbFragment;
    }
    public ItemStack getPowerOrbFragment() {
        return this.powerOrbFragment;
    }
    public void setFreezeClock(ItemStack FreezeClock) {
        this.FreezeClock = FreezeClock;
    }
    public ItemStack getFreezeClock() {
        return this.FreezeClock;
    }


}
