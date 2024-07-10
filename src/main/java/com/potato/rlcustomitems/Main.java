package com.potato.rlcustomitems;

import com.potato.rlcustomitems.Commands.EjectCommand;
import com.potato.rlcustomitems.Commands.ItemGiver;
import com.potato.rlcustomitems.Commands.ReloadCommand;
import com.potato.rlcustomitems.Functions.BossDropItem;
import com.potato.rlcustomitems.Functions.ItemGiverTabCompleter;
import com.potato.rlcustomitems.Functions.NoTNT;
import com.potato.rlcustomitems.IronManSuit.Chat;
import com.potato.rlcustomitems.IronManSuit.Data;
import com.potato.rlcustomitems.IronManSuit.SuitManager;
import com.potato.rlcustomitems.IronManSuit.cmds.IronManCmds;
import com.potato.rlcustomitems.IronManSuit.cmds.IronManTabCompleter;
import com.potato.rlcustomitems.IronManSuit.cmds.SuitCmds;
import com.potato.rlcustomitems.IronManSuit.cmds.SuitTabCompleter;
import com.potato.rlcustomitems.IronManSuit.cmds.suits.mk42;
import com.potato.rlcustomitems.IronManSuit.events.JARVIS.PlayerFire;
import com.potato.rlcustomitems.IronManSuit.events.JARVIS.PlayerHeal;
import com.potato.rlcustomitems.IronManSuit.events.JARVIS.PlayerLowHealth;
import com.potato.rlcustomitems.IronManSuit.events.PlayerDeath;
import com.potato.rlcustomitems.IronManSuit.events.PlayerJoin;
import com.potato.rlcustomitems.IronManSuit.events.PlayerLeave;
import com.potato.rlcustomitems.IronManSuit.events.PlayerMoveArmourListener;
import com.potato.rlcustomitems.IronManSuit.menu.GuiListener;
import com.potato.rlcustomitems.ItemFunctions.*;
import com.potato.rlcustomitems.Items.*;
import dev.respark.licensegate.LicenseGate;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static com.potato.rlcustomitems.IronManSuit.Chat.ragelands;

public final class Main extends JavaPlugin {
    @Getter
    private static Main plugin;
    @Getter
    private static SuitManager suitManager;

    private HashMap<UUID, Integer> playerData;;
    private Main main;
    private BukkitTask itCheck;
    @Override
    public void onEnable() {
        // Save the default config if it doesn't exist
        saveDefaultConfig();

        // Load the config
        FileConfiguration config = getConfig();

        String LicenseKey = getConfig().getString("licensekey");

        // Your public RSA key (can be found in your settings)
        final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY----- MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWngAoriC/ty4zcRGrax kRD3kQVamK9CLb9DxzldVAQXUI3ONXccHchUqVtyKry00obrrryowRgKOrdvCJHE 2oUa9T/bHC3xAD0xdSjeeUVvHYpHJlxpFGDgBeyxlDqTY0wkFM++Ye8nkE1zt7Px VKIvkgYrKT8Kn0aXu79O8kJ5863tAZ1Wc2na3UrSaZ5WH5PT+DDVb8qFz7hKbFOS p4cIneBZoSgGG8bvxrqsW51jt8mshkH8Z1NwvwvkXfCI0U+PXIkdxCp4OYYhyaBt gN/VGGe9RbRLvw1y36uvM6LN4U9YPHUK1HIFMM0aedHRtKttPngbm4U7yrXmQ+LS QQIDAQAB -----END PUBLIC KEY-----";

        // Initialize the LicenseGate client
        LicenseGate licenseGate = new LicenseGate("a1d10", PUBLIC_KEY);

        // Verify the license
        // The "SCOPE" is usually your plugin name - you have to set the same string when creating the license
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.LIGHT_PURPLE + "======================");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.WHITE + ChatColor.BOLD + "Verifying your license key...");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.LIGHT_PURPLE + "======================");

        // In just one line
        boolean isValid = new LicenseGate("a1d10").verify(LicenseKey).isValid();

        // Take action
        if(isValid) {
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.DARK_GREEN + "======================");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] "  +ChatColor.GREEN + ChatColor.BOLD + "License valid!");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.GREEN + ChatColor.BOLD + "Enjoy the official plugin!");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.DARK_GREEN + "======================");
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.RED + "======================");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.DARK_RED + ChatColor.BOLD + "License invalid!");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.DARK_RED + ChatColor.BOLD + "Please contact plugin author(s) (" + Bukkit.getServer().getPluginManager().getPlugin("RageLandsCUSTOM").getDescription().getAuthors() + ") for renewal of license or more information!");
            getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "] " + ChatColor.RED + "======================");
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        plugin = this;

        suitManager = new SuitManager(this);

        playerData = new HashMap<>();
        PDCKeys pdcKeys = new PDCKeys(this);
        ItemMagicBoots magicBoots = new ItemMagicBoots(this);
        ItemShieldDeflect shieldDeflect = new ItemShieldDeflect(this);
        ItemLongRangeBow longRangeBow = new ItemLongRangeBow(this);
        ItemFragBomb fragBomb = new ItemFragBomb(this);
        ItemFlashBang flashBang = new ItemFlashBang(this);
        ItemCowboyBoots cowboyBoots = new ItemCowboyBoots(this);
        ItemFreezeClock freezeClock = new ItemFreezeClock();
        ItemPowerOrbFragment powerOrbFragment = new ItemPowerOrbFragment();
        //Initialize the shared item stack
        DataManager.getInstance().setMagicBoots(magicBoots.getMagicBoots());
        DataManager.getInstance().setDeflectingShield(shieldDeflect.getDeflectingShield());
        DataManager.getInstance().setLongRangeBow(longRangeBow.getLongRangeBow());
        DataManager.getInstance().setFragBomb(fragBomb.getFragBomb());
        DataManager.getInstance().setFlashbang(flashBang.getFlashBang());
        DataManager.getInstance().setCowboyBoots(cowboyBoots.getCowboyBoots());
        DataManager.getInstance().setFreezeClock(freezeClock.getFreezeClock());
        DataManager.getInstance().setPowerOrbFragment(powerOrbFragment.getPowerOrbFragment());
        //Initialize the command and event
        getServer().getPluginManager().registerEvents(new NoTNT(), this);
        getServer().getPluginManager().registerEvents(new MagicBoots(this), this);
        getServer().getPluginManager().registerEvents(new ShieldDeflect(this), this);
        getServer().getPluginManager().registerEvents(new LongRangeBow(this), this);
        getServer().getPluginManager().registerEvents(new FragBomb(this), this);
        getServer().getPluginManager().registerEvents(new Flashbang(this, this), this);
        getServer().getPluginManager().registerEvents(new CowboyBoots(this), this);
        getServer().getPluginManager().registerEvents(new FreezeClock(this), this);
        getServer().getPluginManager().registerEvents(new BossDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerHeal(), this);
        getServer().getPluginManager().registerEvents(new PlayerLowHealth(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new PlayerFire(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveArmourListener(), this);
        Objects.requireNonNull(getCommand("giveitem")).setExecutor(new ItemGiver());
        Objects.requireNonNull(getCommand("giveitem")).setTabCompleter(new ItemGiverTabCompleter());
        Objects.requireNonNull(getCommand("ejectpassengers")).setExecutor(new EjectCommand());
        Objects.requireNonNull(getCommand("reloadragelandsplugin")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(getCommand("ironman")).setExecutor(new IronManCmds());
        Objects.requireNonNull(getCommand("suits")).setExecutor(new SuitCmds());
        Objects.requireNonNull(getCommand("suits")).setTabCompleter(new SuitTabCompleter());
        Objects.requireNonNull(getCommand("ironman")).setTabCompleter(new IronManTabCompleter());
        Objects.requireNonNull(getCommand("mk42")).setExecutor(new mk42());

        startItCheck();
    }

    @Override
    public void onDisable() {
        if (itCheck != null) {
            itCheck.cancel();
        }
        for (Player players : Data.Suit) {
            Data.Suit.remove(players);
            players.getInventory().setHelmet(null);
            players.getInventory().setChestplate(null);
            players.getInventory().setLeggings(null);
            players.getInventory().setBoots(null);
            players.setFlying(false);
            players.setAllowFlight(false);
            Chat.msg(players, Chat.prefix + "&7Armour removed due to reload!");
            Chat.msg(players, Chat.prefix + "&7Effects removed due to reload!");
            for (PotionEffect effects : players.getActivePotionEffects()) {
                players.removePotionEffect(effects.getType());
            }
        }
    }

    private void startItCheck() {
        itCheck = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isItValid()) {
                    getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "]" + ChatColor.RED + "======================");
                    getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "]" + ChatColor.DARK_RED + ChatColor.BOLD + "License key is deactivated! Disabling plugin...");
                    getLogger().warning("Please contact plugin author(s) (" + Bukkit.getServer().getPluginManager().getPlugin("RageLandsCUSTOM").getDescription().getAuthors() + ") for renewal of license or more information!");
                    getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "["  + ragelands + "]" + ChatColor.RED + "======================");
                    Bukkit.getPluginManager().disablePlugin(Main.this); // Use MyPlugin.this
                }
            }
        }.runTaskTimer(this, 0L, 20L * 60 * 10); // Check every 10 minutes
    }

    private boolean isItValid() {
        // Implement your logic to check the license status here
        // Return true if valid, false if deactivated
        String IT = getConfig().getString("licensekey");
        return new LicenseGate("a1d10").verify(IT).isValid();
    }
    public void setPlayerData(UUID playerId, int value) {
        playerData.put(playerId, value);
    }

    public int getPlayerData(UUID playerId) {
        return playerData.getOrDefault(playerId, 0);
    }
}