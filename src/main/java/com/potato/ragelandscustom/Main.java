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
import com.potato.ragelandscustom.Functions.StockSystem.*;
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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    @Getter
    private PlayerStockManager playerStockManager;
    private static Economy econ = null;
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Save the default config if it doesn't exist
        saveDefaultConfig();
        createCustomConfig();
        // Initialize stock prices and volatility
        for (StockEnum stock : StockEnum.values()) {
            String path = "stocks." + stock.getName();
            stock.setPrice(getConfig().getDouble(path + ".initial_price", stock.getPrice()));
            stock.setVolatility(getConfig().getDouble(path + ".volatility", stock.getVolatility()));
        }
        scheduleStockPriceUpdates();
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
        playerStockManager = new PlayerStockManager(new File(getDataFolder(), "playerdata"));
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
        votingFile = new File(getDataFolder(), "voting.yml");
        if (!votingFile.exists()) {
            try {
                votingFile.createNewFile();
                getLogger().info("voting.yml file created.");
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("Could not create voting.yml file.");
            }
        }
        votingConfig = YamlConfiguration.loadConfiguration(votingFile);
        if (!votingConfig.contains("votes")) {
            votingConfig.createSection("votes");
        }
        if (!votingConfig.contains("voters")) {
            votingConfig.createSection("voters");
        }
        if (!votingConfig.contains("candidates")) {
            votingConfig.createSection("candidates");
        }
        if (!votingConfig.contains("president")) {
            votingConfig.createSection("president");
        }
        new StockPlaceholderExpansion(this).register();
        saveVotingConfig();
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
        getServer().getPluginManager().registerEvents(new StockGUI(this), this);
        getServer().getPluginManager().registerEvents(new OnJoinLeaveSaveLoad(this, playerStockManager), this);
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
        Objects.requireNonNull(getCommand("sign")).setExecutor(new SignCommand());
        Objects.requireNonNull(getCommand("stocks")).setExecutor(new StockCommand());
        Objects.requireNonNull(getCommand("togglecowboy")).setExecutor(new PlayerDisableSit());
        Objects.requireNonNull(getCommand("setvolatility")).setExecutor(new SetVolatilityCommand(this));
        Objects.requireNonNull(getCommand("setvolatility")).setTabCompleter(new SetVolatilityCommand(this));
        startItCheck();
        loadStockConfig();
        loadStockQuantities();
    }

    @Override
    public void onDisable() {
        if (itCheck != null) {
            itCheck.cancel();
        }
        saveVotingConfig();
        saveStockQuantities();
    }
    private void scheduleStockPriceUpdates() {
        Bukkit.getScheduler().runTaskTimer(this, this::updateStockPrices, 0L, 72000L); // 72000 ticks = 1 hour
    }

    private void loadStockQuantities() {
        File stocksFile = new File(getDataFolder(), "stocks.yml");
        if (!stocksFile.exists()) {
            saveResource("stocks.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(stocksFile);
        StockEnum.loadFromConfig(config);
    }

    public void saveStockQuantities() {
        FileConfiguration config = new YamlConfiguration();
        StockEnum.saveToConfig(config);
        try {
            config.save(new File(getDataFolder(), "stocks.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStockConfig() {
        File stockConfigFile = new File(getDataFolder(), "stocks.yml");
        if (!stockConfigFile.exists()) {
            saveResource("stocks.yml", false);
        }
        YamlConfiguration stockConfig = YamlConfiguration.loadConfiguration(stockConfigFile);

        for (StockEnum stock : StockEnum.values()) {
            String path = "stocks." + stock.getName().toLowerCase();
            stock.setPrice(stockConfig.getDouble(path + ".price"));
            stock.setQuantity(stockConfig.getInt(path + ".quantity"));
            stock.setVolatility(stockConfig.getDouble(path + ".volatility"));
        }
    }
    private void updateStockPrices() {
        File stockConfigFile = new File(getDataFolder(), "stocks.yml");
        YamlConfiguration stockConfig = YamlConfiguration.loadConfiguration(stockConfigFile);

        for (StockEnum stock : StockEnum.values()) {
            stock.updatePrice();
            String path = "stocks." + stock.getName().toLowerCase();
            stockConfig.set(path + ".price", stock.getPrice());
            stockConfig.set(path + ".quantity", stock.getQuantity());
            stockConfig.set(path + ".volatility", stock.getVolatility());
        }

        try {
            stockConfig.save(stockConfigFile);
            Chat.broadcastMessage(Chat.prefix + "&7Stock prices have been updated!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public double getPlayerBalance(Player player) {
        return econ.getBalance(player);
    }

    public void updatePlayerBalance(Player player, double newBalance) {
        double currentBalance = econ.getBalance(player);
        double difference = newBalance - currentBalance;
        if (difference > 0) {
            econ.depositPlayer(player, difference);
        } else {
            econ.withdrawPlayer(player, -difference);
        }
    }

    public double getStockPrice(StockEnum stock) {
        switch (stock) {
            case RLN:
                return StockEnum.RLN.getPrice(); // Placeholder value
            case PEESCOIN:
                return StockEnum.PEESCOIN.getPrice(); // Placeholder value
            case POTATOCOIN:
                return StockEnum.POTATOCOIN.getPrice(); // Placeholder value
            default:
                return 0.0;
        }
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
        votingConfig.set("votes", null);
        votingConfig.set("voters", null);
        votingConfig.set("candidates", null);
        saveVotingConfig();
        getServer().broadcastMessage("A new presidential campaign has started!");
    }
    public void endCampaign() {
        Map<String, Object> votes = votingConfig.getConfigurationSection("votes").getValues(false);
        String topCandidate = null;
        int highestVotes = 0;

        for (Map.Entry<String, Object> entry : votes.entrySet()) {
            int voteCount = (int) entry.getValue();
            if (voteCount > highestVotes) {
                highestVotes = voteCount;
                topCandidate = entry.getKey();
            }
        }

        if (topCandidate != null) {
            setPresident(topCandidate);
        }

        resetVotes(); // Optionally, reset votes after ending the campaign
        Chat.broadcastMessage(Chat.prefix + "&7The presidential campaign has ended! " + topCandidate + " is the new president!");
    }

    public void resetCampaign() {
        votingConfig.set("votes", null);
        votingConfig.set("voters", null);
        votingConfig.set("candidates", null);
        saveVotingConfig();
        getServer().broadcastMessage("The presidential campaign has been reset!");
    }

    public void resetVotes() {
        votingConfig.set("votes", null);
        votingConfig.set("voters", null);
        saveVotingConfig();
        getServer().broadcastMessage("All votes have been reset!");
    }

    public void setPresident(String playerName) {
        votingConfig.set("president", ChatColor.stripColor(playerName));
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.setGlowing(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
            getServer().broadcastMessage(playerName + " has been elected as the President!");
        }
        saveVotingConfig();
    }

    public void removePresident() {
        String presidentName = votingConfig.getString("president");
        Player player = Bukkit.getPlayer(presidentName);
        if (player != null) {
            player.setGlowing(false);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            getServer().broadcastMessage(presidentName + " is no longer the President.");
        }
        votingConfig.set("president", null);
        saveVotingConfig();
    }

    public void addCandidate(String playerName) {
        List<String> candidates = votingConfig.getStringList("candidates");
        candidates.add(playerName);
        votingConfig.set("candidates", candidates);
        saveVotingConfig();
    }

    public void removePlayerVote(String playerName) {
        if (votingConfig.contains("voters." + playerName)) {
            String votedCandidate = votingConfig.getString("voters." + playerName);
            int currentVotes = votingConfig.getInt("votes." + votedCandidate, 0);
            if (currentVotes > 0) {
                votingConfig.set("votes." + votedCandidate, currentVotes - 1);
            }
            votingConfig.set("voters." + playerName, null);
            saveVotingConfig();
        }
    }

    public void displayVoteResults(Player player) {
        if (votingConfig.contains("votes")) {
            player.sendMessage("Current Vote Results:");
            Map<String, Object> votes = votingConfig.getConfigurationSection("votes").getValues(false);
            for (Map.Entry<String, Object> entry : votes.entrySet()) {
                player.sendMessage(entry.getKey() + " : " + entry.getValue());
            }
        } else {
            player.sendMessage("No votes have been cast yet.");
        }
    }


    public void saveVotingConfig() {
        if (votingFile != null) {
            try {
                votingConfig.save(votingFile);
                getLogger().info("voting.yml file saved.");
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("Could not save voting.yml file.");
            }
        } else {
            getLogger().severe("votingFile is null, cannot save.");
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
    public void openVoteInventory(Player player) {
        List<String> candidates = votingConfig.getStringList("candidates");
        if (candidates.isEmpty()) {
            player.sendMessage("No candidates available for voting.");
            return;
        }

        Inventory voteInventory = Bukkit.createInventory(null, 9, "Vote for President");
        for (String candidateName : candidates) {
            ItemStack candidateHead = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) candidateHead.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(candidateName));
            meta.setDisplayName(candidateName);
            candidateHead.setItemMeta(meta);
            voteInventory.addItem(candidateHead);
        }
        player.openInventory(voteInventory);
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