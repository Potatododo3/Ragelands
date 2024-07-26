package com.potato.ragelandscustom;

import com.potato.ragelandscustom.Commands.*;
import com.potato.ragelandscustom.Commands.PresidentalStuff.*;
import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Functions.DragonEgg.DragonEggPreventer;
import com.potato.ragelandscustom.Functions.DragonEgg.PlayerDeathWithEgg;
import com.potato.ragelandscustom.Functions.DragonEgg.PlayerObtainEgg;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.potato.ragelandscustom.Functions.Chat.ragelands;

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
    private RetrieveItemUtil retrieveItemUtil;
    private ArrowFireListener arrowFireListener;
    public static ItemStack basketOfSeeds;
    public static ItemStack mark50;
    public static ItemStack mark34;
    private static Main mainInstance;
    private File customConfigFile;
    @Getter
    private FileConfiguration customConfig;
    private File votingFile;
    @Getter
    private FileConfiguration votingConfig;
    private static final long CAMPAIGN_DURATION = 86400000L; // 24 hours in milliseconds
    @Override
    public void onEnable() {
        // Save the default config if it doesn't exist
        saveDefaultConfig();
        createCustomConfig();
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

        retrieveItemUtil = new RetrieveItemUtil(this);



        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getInventory().contains(Material.DRAGON_EGG)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20, 0));
                    }
                }
            }
        }.runTaskTimer(this, 0L, 60L);

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
        // Register commands

        // Example usage of retrieving the item
        RetrieveItemUtil retrieveItemUtil = new RetrieveItemUtil(this);
        ItemStack savedItem = retrieveItemUtil.getSavedItem();
        if (savedItem != null) {
            getLogger().info("Successfully retrieved saved item.");
        }
        else {
            getLogger().info("No item found in custom config.");
        }
        createBasketOfSeeds();
        createMark34();
        createMark50();
        votingConfig = new YamlConfiguration();

        // Load the voting configuration
        try {
            votingConfig.load(new File(getDataFolder(), "voting.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new PresidentListener(this), this);

        // Only schedule task to end the campaign if active
        if (votingConfig.getBoolean("campaign_active")) {
            long timeRemaining = CAMPAIGN_DURATION - (System.currentTimeMillis() - votingConfig.getLong("campaign_start_time"));
            if (timeRemaining > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        endCampaign();
                    }
                }.runTaskLater(this, timeRemaining / 50); // Convert milliseconds to ticks
            } else {
                endCampaign();
            }
        }
        createVotingConfig();
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
        getServer().getPluginManager().registerEvents(new PlayerFly(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFire(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveArmourListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerTracking(this), this);
        getServer().getPluginManager().registerEvents(new FireAbility(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileImpactListener(this), this);
        getServer().getPluginManager().registerEvents(new DragonEggPreventer(), this);
        getServer().getPluginManager().registerEvents(new ItemsPreventer(), this);
        getServer().getPluginManager().registerEvents(new BasketOfSeeds(this), this);
        getServer().getPluginManager().registerEvents(new IronManItem(), this);
        getServer().getPluginManager().registerEvents(new ArrowFireListener(this), this);
        getServer().getPluginManager().registerEvents(new ArrowHitListener(this, arrowFireListener), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathWithEgg(), this);
        getServer().getPluginManager().registerEvents(new PlayerObtainEgg(), this);
        getServer().getPluginManager().registerEvents(new VoteListener(this), this);
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
        Objects.requireNonNull(getCommand("saveitem")).setExecutor(new SaveItemCommand(this));
        Objects.requireNonNull(getCommand("ragelands")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(getCommand("ragelands")).setTabCompleter(new TabCompletionHandler());
        Objects.requireNonNull(getCommand("assassinationtoggle")).setExecutor(new AssassinationPresidentToggle());
        startItCheck();
    }

    @Override
    public void onDisable() {
        if (itCheck != null) {
            itCheck.cancel();
        }
        saveVotingConfig();
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "items.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getItemCount(Player player, Material material) {
        int itemCount = 0;

        // Iterate through the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {
            // Check if the item is not null and matches the specified material
            if (item != null && item.getType() == material) {
                itemCount += item.getAmount();
            }
        }

        return itemCount;
    }
    public void startCampaign() {
        if (votingConfig.getBoolean("campaign_active")) {
            Bukkit.getLogger().info(Chat.prefix + "A campaign is already active. Cannot start a new campaign.");
            return;
        }

        votingConfig.set("campaign_active", true);
        votingConfig.set("campaign_start_time", System.currentTimeMillis());
        saveVotingConfig();

        // Schedule task to end the campaign after 24 hours
        new BukkitRunnable() {
            @Override
            public void run() {
                endCampaign();
            }
        }.runTaskLater(this, CAMPAIGN_DURATION / 50); // Convert milliseconds to ticks

        Chat.broadcastMessage(Chat.prefix + "A new voting campaign has started! Cast your votes now!");
    }
    public void endCampaign() {
        votingConfig.set("campaign_active", false);
        saveVotingConfig();

        // Determine the winner
        String president = null;
        int maxVotes = 0;
        Map<String, Object> votes = votingConfig.getConfigurationSection("votes").getValues(false);
        for (Map.Entry<String, Object> entry : votes.entrySet()) {
            int voteCount = (int) entry.getValue();
            if (voteCount > maxVotes) {
                maxVotes = voteCount;
                president = entry.getKey();
            }
        }

        // Broadcast the winner
        if (president != null) {
            votingConfig.set("president", president);
            saveVotingConfig();
            Bukkit.broadcastMessage(Chat.prefix + "The new president is " + president + " with " + maxVotes + " votes!");

            // Apply effects to the new president
            Player presidentPlayer = Bukkit.getPlayer(president);
            if (presidentPlayer != null) {
                presidentPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, true, false));
                presidentPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
            }
        } else {
            Bukkit.broadcastMessage(Chat.prefix + "No president was elected.");
        }
    }
    public void createVotingConfig() {
        votingFile = new File(getDataFolder(), "voting.yml");
        if (!votingFile.exists()) {
            votingFile.getParentFile().mkdirs();
            saveResource("voting.yml", false);
        }

        votingConfig = YamlConfiguration.loadConfiguration(votingFile);
    }

    public void saveVotingConfig() {
        try {
            votingConfig.save(votingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void decreaseItemCount(Player player, Material material, int amount) {
        ItemStack[] items = player.getInventory().getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null && item.getType() == material) {
                int currentAmount = item.getAmount();
                if (currentAmount > amount) {
                    item.setAmount(currentAmount - amount);
                    break;
                } else {
                    amount -= currentAmount;
                    player.getInventory().clear(i);
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }
    public static List<InventoryType> getAllInventoryTypes() {
        return Arrays.asList(
                InventoryType.CHEST,
                InventoryType.DISPENSER,
                InventoryType.DROPPER,
                InventoryType.HOPPER,
                InventoryType.BREWING,
                InventoryType.ENCHANTING,
                InventoryType.ANVIL,
                InventoryType.MERCHANT,
                InventoryType.SHULKER_BOX,
                InventoryType.ENDER_CHEST,
                InventoryType.BARREL,
                InventoryType.SMITHING,
                InventoryType.CARTOGRAPHY,
                InventoryType.FURNACE,
                InventoryType.LOOM,
                InventoryType.BLAST_FURNACE,
                InventoryType.SMOKER,
                InventoryType.GRINDSTONE,
                InventoryType.STONECUTTER
        );
    }
    public void saveCustomConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
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
    public void removePlayerVote(String playerName) {
        FileConfiguration votingConfig = getVotingConfig();
        if (votingConfig.contains("voters." + playerName)) {
            String votedCandidate = votingConfig.getString("voters." + playerName);
            int currentVotes = votingConfig.getInt("votes." + votedCandidate, 0);
            votingConfig.set("votes." + votedCandidate, currentVotes - 1);
            votingConfig.set("voters." + playerName, null);
            saveVotingConfig();
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