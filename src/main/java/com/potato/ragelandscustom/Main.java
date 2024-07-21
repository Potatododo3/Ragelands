package com.potato.ragelandscustom;

import com.potato.ragelandscustom.Commands.*;
import com.potato.ragelandscustom.Functions.BossDropItem;
import com.potato.ragelandscustom.Functions.DragonEgg.*;
import com.potato.ragelandscustom.Functions.IronManItem;
import com.potato.ragelandscustom.Functions.ItemGiverTabCompleter;
import com.potato.ragelandscustom.Functions.NoTNT;
import com.potato.ragelandscustom.IronManSuit.SuitManager;
import com.potato.ragelandscustom.IronManSuit.cmds.IronManCmds;
import com.potato.ragelandscustom.IronManSuit.cmds.IronManTabCompleter;
import com.potato.ragelandscustom.IronManSuit.cmds.SuitCmds;
import com.potato.ragelandscustom.IronManSuit.cmds.SuitTabCompleter;
import com.potato.ragelandscustom.IronManSuit.cmds.suits.mk42;
import com.potato.ragelandscustom.IronManSuit.events.*;
import com.potato.ragelandscustom.IronManSuit.events.JARVIS.PlayerFire;
import com.potato.ragelandscustom.IronManSuit.events.JARVIS.PlayerHeal;
import com.potato.ragelandscustom.IronManSuit.events.JARVIS.PlayerLowHealth;
import com.potato.ragelandscustom.IronManSuit.events.JARVIS.PlayerTracking;
import com.potato.ragelandscustom.IronManSuit.menu.GuiListener;
import com.potato.ragelandscustom.ItemFunctions.*;
import com.potato.ragelandscustom.ItemFunctions.Stinger.ArrowFireListener;
import com.potato.ragelandscustom.ItemFunctions.Stinger.ArrowHitListener;
import com.potato.ragelandscustom.Items.*;
import dev.respark.licensegate.LicenseGate;
import lombok.Getter;
import me.msmaciek.redefinedglowingentities.api.RedefinedGlowingEntitiesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.potato.ragelandscustom.IronManSuit.Chat.ragelands;

public final class Main extends JavaPlugin {
    @Getter
    private static Main plugin;
    @Getter
    private static SuitManager suitManager;
    public static ItemStack stinger;
    private HashMap<UUID, Integer> playerData;;
    private Main main;
    private BukkitTask itCheck;
    public RedefinedGlowingEntitiesAPI geAPI;
    private AbilitySelectionGUI abilitySelectionGUI;
    private ArrowFireListener arrowFireListener;
    public static ItemStack basketOfSeeds;
    public static ItemStack mark50;
    public static ItemStack mark34;
    private static Main mainInstance;
    @Override
    public void onEnable() {
        // Save the default config if it doesn't exist
        saveDefaultConfig();

        // Load the config
        FileConfiguration config = getConfig();

        String LicenseKey = config.getString("licensekey");

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
        geAPI = new RedefinedGlowingEntitiesAPI(this);

        plugin = this;

        mainInstance = this;

        suitManager = new SuitManager(this);

        arrowFireListener = new ArrowFireListener(this);
        // Initialize the Stinger item
        ItemStack item = new ItemStack(Material.CROSSBOW); // Use the appropriate material
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Stinger");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Hit mid air enemies and assert the high ground.");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        double stingerRadius = config.getDouble("StingerRadius");
        double laserRadius = config.getDouble("LaserRadius");

        stinger = item;

        createBasketOfSeeds();
        createMark34();
        createMark50();
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
        getServer().getPluginManager().registerEvents(new PlayerFly(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerHeal(), this);
        getServer().getPluginManager().registerEvents(new PlayerLowHealth(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFire(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveArmourListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerTracking(this), this);
        getServer().getPluginManager().registerEvents(new FireAbility(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileImpactListener(this), this);
        abilitySelectionGUI = new AbilitySelectionGUI(this);
        getServer().getPluginManager().registerEvents(new DragonEggActivationListener(this), this);
        getServer().getPluginManager().registerEvents(new DragonSmashAbility(this), this);
        getServer().getPluginManager().registerEvents(new DragonFlameAbility(this), this);
        getServer().getPluginManager().registerEvents(new DragonSummonAbility(this), this);
        getServer().getPluginManager().registerEvents(new PreventDragonEggPlacement(this), this);
        getServer().getPluginManager().registerEvents(abilitySelectionGUI, this);
        getServer().getPluginManager().registerEvents(new DragonEggPreventer(), this);
        getServer().getPluginManager().registerEvents(new ItemsPreventer(), this);
        getServer().getPluginManager().registerEvents(new BasketOfSeeds(this), this);
        getServer().getPluginManager().registerEvents(new IronManItem(), this);
        getServer().getPluginManager().registerEvents(new ArrowFireListener(this), this);
        getServer().getPluginManager().registerEvents(new ArrowHitListener(this, arrowFireListener), this);
        Objects.requireNonNull(getCommand("cooldownreset")).setExecutor(new ResetCooldowns());
        Objects.requireNonNull(getCommand("giveitem")).setExecutor(new ItemGiver());
        Objects.requireNonNull(getCommand("giveitem")).setTabCompleter(new ItemGiverTabCompleter());
        Objects.requireNonNull(getCommand("ejectpassengers")).setExecutor(new EjectCommand());
        Objects.requireNonNull(getCommand("reloadragelandsplugin")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(getCommand("ironman")).setExecutor(new IronManCmds());
        Objects.requireNonNull(getCommand("suits")).setExecutor(new SuitCmds());
        Objects.requireNonNull(getCommand("suits")).setTabCompleter(new SuitTabCompleter());
        Objects.requireNonNull(getCommand("ironman")).setTabCompleter(new IronManTabCompleter());
        Objects.requireNonNull(getCommand("mk42")).setExecutor(new mk42());
        Objects.requireNonNull(getCommand("notnt")).setExecutor(new NoTNTCommand());
        Objects.requireNonNull(getCommand("nobossdrops")).setExecutor(new BossDropItemCommand());
        startItCheck();
    }

    @Override
    public void onDisable() {
        if (itCheck != null) {
            itCheck.cancel();
        }
    }
    public static ItemStack getHead(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL("http://textures.minecraft.net/texture/" + texture));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
    public static @NotNull ItemStack createBasketOfSeeds() {
        ItemStack item = getHead("94494694001906e9f9331438aafc919db1df5cb75c2ae3887ed236c3cfb5787e");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName("§aBasket of Seeds");
        meta.setLore(Arrays.asList("§7Right-click to plant seeds in a", "§7specified area."));
        item.setItemMeta(meta);
        basketOfSeeds = item;
        return item;
    }
    public static @NotNull ItemStack createMark50() {
        ItemStack item = getHead("d14824d840fb9ae9acd6c4f0c917dbf52803a65314a3b771346e33907e4dee79");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName("§aIronMan");
        meta.setLore(Arrays.asList("§7Right-click to wear the Mark 50"));
        item.setItemMeta(meta);
        mark50 = item;
        return item;
    }
    public static @NotNull ItemStack createMark34() {
        ItemStack item = getHead("b4ee5d3797e339ba55a3ff936be16b26c9d83c173c8ba97c9881e41889c7c3bb");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName("§aIronMan");
        meta.setLore(Arrays.asList("§7Right-click to wear the Mark 34"));
        item.setItemMeta(meta);
        mark34 = item;
        return item;
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
    public static Main getMainInstance() {
        return mainInstance;
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