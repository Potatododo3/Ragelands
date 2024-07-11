package com.potato.ragelandscustom.ItemFunctions;

import java.util.HashMap;

public class CooldownManager {
    private static final HashMap<String, Long> magicBootsCooldown = new HashMap<>();
    private static final HashMap<String, Long> fragBombCooldowns = new HashMap<>();
    private static final HashMap<String, Long> freezeClockCooldowns = new HashMap<>();

    public boolean isOnFreezeClockCooldown(String playerName) {
        return freezeClockCooldowns.containsKey(playerName) && freezeClockCooldowns.get(playerName) > System.currentTimeMillis();
    }

    public void setFreezeClockCooldowns(String playerName, int seconds) {
        freezeClockCooldowns.put(playerName, System.currentTimeMillis() + (seconds * 1000L));
    }

    // Set cooldown in ticks
    public static void setMagicbootsCooldown(String player, int ticks) {
        long currentTick = System.currentTimeMillis() / 50; // 1 tick = 50 ms
        long cooldownTick = currentTick + ticks;
        magicBootsCooldown.put(player, cooldownTick);
    }

    // Get remaining cooldown time in ticks
    public static int getMagicbootsCooldown(String player) {
        Long cooldownTick = magicBootsCooldown.get(player);
        if (cooldownTick == null) return 0;
        long currentTick = System.currentTimeMillis() / 50;
        if (currentTick >= cooldownTick) {
            magicBootsCooldown.remove(player);
            return 0;
        }

        return (int) (cooldownTick - currentTick);
    }

    // Check if player is on cooldown
    public static boolean isOnMagicbootsCooldown(String player) {
        return getMagicbootsCooldown(player) > 0;
    }

    // Set cooldown in ticks
    public static void setFragbombCooldown(String player, int ticks) {
        long currentTick = System.currentTimeMillis() / 50; // 1 tick = 50 ms
        long cooldownTick = currentTick + ticks;
        fragBombCooldowns.put(player, cooldownTick);
    }

    // Get remaining cooldown time in ticks
    public static int getFragbombCooldown(String player) {
        Long cooldownTick = fragBombCooldowns.get(player);
        if (cooldownTick == null) return 0;
        long currentTick = System.currentTimeMillis() / 50;
        if (currentTick >= cooldownTick) {
            fragBombCooldowns.remove(player);
            return 0;
        }

        return (int) (cooldownTick - currentTick);
    }

    // Check if player is on cooldown
    public static boolean isOnFragbombCooldown(String player) {
        return getFragbombCooldown(player) > 0;
    }
    public long getRemainingFreezeClockCooldown(String playerName) {
        if (!freezeClockCooldowns.containsKey(playerName)) {
            return 0L;
        }
        long remainingTime = freezeClockCooldowns.get(playerName) - System.currentTimeMillis();
        return remainingTime > 0 ? remainingTime : 0L; // Ensure non-negative remaining time
    }
}

