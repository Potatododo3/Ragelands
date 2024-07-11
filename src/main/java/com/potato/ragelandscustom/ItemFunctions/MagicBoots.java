package com.potato.ragelandscustom.ItemFunctions;


import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.potato.ragelandscustom.CC;
import com.potato.ragelandscustom.DataManager;
import com.potato.ragelandscustom.Items.PDCKeys;
import com.potato.ragelandscustom.Main;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MagicBoots implements Listener {
    ItemStack magicBoots = DataManager.getInstance().getMagicBoots();
    private final Main main;

    private final Map<String, Boolean> abilityActivated = new HashMap<>();

    private final Map<String, Long> sneakStartTimes = new HashMap<>();

    public MagicBoots(Main main) {
        this.main = main;
    }

    //Method to check if player has the magic boots on
    private boolean hasMagicBoots(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null && boots.getType() == Material.IRON_BOOTS;
    }

    //Method to handle the procedure after the player sneaks
    private void handleSneaking(Player sneakedPlayer, Location blockOfSneak) {
        Firework firework = sneakedPlayer.getPlayer().getWorld().spawn(sneakedPlayer.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GRAY).withColor(Color.SILVER).with(Type.STAR).withFlicker().withTrail().build());
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 2);
        sneakedPlayer.addPotionEffect(slowness);
        //You now can not be seen while sneaking!
        sneakedPlayer.sendMessage(CC.translate("&#408147&lY&#408147&lo&#408147&lu &#408147&ln&#3F7C46&lo&#3E7845&lw &#3B6E42&lc&#3A6A41&la&#396540&ln &#365C3D&ln&#35573C&lo&#34533B&lt &#314939&lb&#304537&le &#2D3B35&ls&#2C3734&le&#2B3233&le&#2A2D31&ln &#27242F&lw&#25213E&lh&#241F4C&li&#221C5B&ll&#20196A&le &#1D1487&ls&#1B1196&ln&#1A0FA4&le&#180CB3&la&#1609C2&lk&#1507D0&li&#1304DF&ln&#1304DF&lg&#1304DF&l!"));
        Objects.requireNonNull(blockOfSneak.getWorld()).spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, blockOfSneak, 50);
        abilityActivated.put(sneakedPlayer.toString(), true);
    }

    private void handleNotSneaking(Player sneakedPlayer) {
        sneakedPlayer.removePotionEffect(PotionEffectType.SLOW);
        abilityActivated.put(sneakedPlayer.toString(), false);
        //You are visible again!
        if (!(CooldownManager.isOnMagicbootsCooldown(String.valueOf(sneakedPlayer)))) {
            sneakedPlayer.sendMessage(CC.translate("&#408147&lY&#408147&lo&#408147&lu &#3C7243&la&#3A6A41&lr&#38623F&le &#34533B&lv&#314B39&li&#2F4337&ls&#2D3B35&li&#2B3433&lb&#292C31&ll&#27242F&le &#20196A&la&#1D1487&lg&#1A0FA4&la&#1609C2&li&#1304DF&ln&#1304DF&l!"));
        }
    }

    private void hidePlayerToAll(Player sneakedPlayer) {
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            FPlayer targetFPlayer = FPlayers.getInstance().getByPlayer(targetPlayer);
            FPlayer sneakedFPlayer = FPlayers.getInstance().getByPlayer(sneakedPlayer);
            Faction targetPlayerFaction = targetFPlayer.getFaction();
            Faction sneakedPlayerFaction = sneakedFPlayer.getFaction();
            targetPlayer.hidePlayer(sneakedPlayer);
            if (sneakedPlayerFaction == null) {
                return;
            }
            else if (targetPlayerFaction == null) {
                return;
            }
            else {
                if (targetPlayerFaction == sneakedPlayerFaction) {
                    targetPlayer.showPlayer(sneakedPlayer);
                }
            }
        }
    }

    private void showPlayerToAll(Player sneakedPlayer) {
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            targetPlayer.showPlayer(sneakedPlayer);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (hasMagicBoots(e.getPlayer())) {
            PDCKeys pdcKeys = new PDCKeys(main);
            NamespacedKey key = pdcKeys.getKey();
            Location blockOfSneak = e.getPlayer().getLocation();
            Player sneakedPlayer = e.getPlayer();
            ItemStack playerBoots = sneakedPlayer.getInventory().getBoots();
            Boolean bootsPDC = playerBoots.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN);
            if (bootsPDC != null) {
                if (bootsPDC) {
                    String playerName = e.getPlayer().getName();
                    if (CooldownManager.isOnMagicbootsCooldown(playerName)) {
                        float remainingTime = (float) CooldownManager.getMagicbootsCooldown(playerName) / 50;
                        sneakedPlayer.sendMessage("Ability on cooldown! " + remainingTime + " seconds remaining.");
                        return;
                    }
                    if (!e.isSneaking() && !sneakedPlayer.isFlying()) {
                        CooldownManager.setMagicbootsCooldown(playerName, 2000);
                        handleNotSneaking(sneakedPlayer);
                        showPlayerToAll(sneakedPlayer);
                    }
                    if (e.isSneaking() && !sneakedPlayer.isFlying()) {
                        sneakStartTimes.putIfAbsent(playerName, System.currentTimeMillis());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (sneakedPlayer.isSneaking()) {
                                    Long startTime = sneakStartTimes.get(playerName);
                                    if (startTime == null) {
                                        this.cancel();  // Stop the runnable if the start time is not found
                                        return;
                                    }
                                    long sneakDuration = System.currentTimeMillis() - startTime;
                                    if (sneakDuration >= 3000) {
                                        handleSneaking(sneakedPlayer, blockOfSneak);
                                        hidePlayerToAll(sneakedPlayer);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                CooldownManager.setMagicbootsCooldown(playerName, 2000);
                                                handleNotSneaking(sneakedPlayer);
                                                showPlayerToAll(sneakedPlayer);
                                                sneakStartTimes.remove(playerName);  // Clean up the start time
                                            }
                                        }.runTaskLater(main, 25 * 20L);  // 25 seconds (20 ticks per second)

                                        this.cancel();  // Stop the runnable
                                    }
                                }
                                else {
                                    this.cancel();  // Stop the runnable if the player stopped sneaking
                                }
                            }
                        }.runTaskTimer(main, 0L, 20L);  // Check every tick (20 ticks per second)
                    }
                    else {
                        // Player stopped sneaking
                        sneakStartTimes.remove(playerName);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMilkDrink(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            Player player = e.getPlayer();
            if (abilityActivated.getOrDefault(player.toString(), false)) {
                e.setCancelled(true);
                player.sendMessage(CC.translate("&#048CF0&lY&#078BED&lo&#098AEA&lu &#0E89E5&lc&#1188E2&la&#1487DF&ln &#1986DA&ln&#1C85D7&lo&#1E84D4&lt &#2382CE&lr&#2682CC&le&#2981C9&lm&#2B80C6&lo&#2E7FC3&lv&#307EC0&le &#367DBB&ls&#387CB8&ll&#3B7BB5&lo&#3D7AB2&lw&#407AB0&ln&#4379AD&le&#4578AA&ls&#4877A7&ls &#4D76A2&le&#50759F&lf&#52749C&lf&#557399&le&#587397&lc&#5A7294&lt &#5F708E&lw&#626F8B&lh&#656F89&li&#676E86&ll&#6A6D83&le &#6F6B7D&ls&#726B7B&ln&#746A78&le&#776975&la&#7A6872&lk&#7C676F&li&#7F676D&ln&#81666A&lg&#846567&l!"));
            }
        }
    }
}