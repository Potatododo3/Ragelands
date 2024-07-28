package com.potato.ragelandscustom.Functions.StockSystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerStockManager {
    private final File dataFolder;

    public PlayerStockManager(File dataFolder) {
        this.dataFolder = dataFolder;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public void savePlayerStocks(Player player, Map<StockEnum, Integer> stocks) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        for (Map.Entry<StockEnum, Integer> entry : stocks.entrySet()) {
            config.set(entry.getKey().name(), entry.getValue());
        }

        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<StockEnum, Integer> loadPlayerStocks(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        Map<StockEnum, Integer> stocks = new HashMap<>();
        for (StockEnum stock : StockEnum.values()) {
            int quantity = config.getInt(stock.name(), 0);
            stocks.put(stock, quantity);
        }

        return stocks;
    }
}
