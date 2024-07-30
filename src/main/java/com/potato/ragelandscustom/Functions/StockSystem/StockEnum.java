package com.potato.ragelandscustom.Functions.StockSystem;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

public enum StockEnum {
    RLN("RLN", 23500, 25000, 0.05),
    PEESCOIN("PeesCoin", 0.002, 16000000, 0.10),
    POTATOCOIN("PotatoCoin", 0.05, 1600000, 0.20);

    @Getter
    private final String name;
    @Getter
    @Setter
    private double price;
    @Getter
    @Setter
    private int quantity;
    @Getter
    @Setter
    private double volatility;

    StockEnum(String name, double price, int quantity, double volatility) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.volatility = volatility;
    }

    // Load quantity from configuration
    public static void loadQuantitiesFromConfig(FileConfiguration config) {
        for (StockEnum stock : StockEnum.values()) {
            int quantity = config.getInt("stocks." + stock.getName() + ".quantity", stock.getQuantity());
            stock.setQuantity(quantity);
        }
    }
}