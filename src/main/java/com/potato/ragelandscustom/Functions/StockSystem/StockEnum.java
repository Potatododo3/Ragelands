package com.potato.ragelandscustom.Functions.StockSystem;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

public enum StockEnum {
    RLN("RLN", 23500, 25000, 0.05, false),
    PEESCOIN("PeesCoin", 0.002, 16000000, 0.10, true),
    POTATOCOIN("PotatoCoin", 0.05, 1600000, 0.20, true);

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
    @Getter
    private final boolean infiniteSupply;

    StockEnum(String name, double price, int quantity, double volatility, boolean infiniteSupply) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.volatility = volatility;
        this.infiniteSupply = infiniteSupply;
    }

    public void updatePrice() {
        double change = (Math.random() - 0.5) * 2 * volatility;
        this.price = Math.max(0, this.price + this.price * change);
        this.price = Math.round(this.price * 100000.0) / 100000.0; // Round to 5 decimal places
    }

    public static void loadFromConfig(FileConfiguration config) {
        for (StockEnum stock : values()) {
            String path = "stocks." + stock.getName();
            stock.setPrice(config.getDouble(path + ".price", stock.getPrice()));
            String quantity = config.getString(path + ".quantity", String.valueOf(stock.getQuantity()));
            if (quantity.equals("infinite")) {
                stock.setQuantity(Integer.MAX_VALUE); // Use a large number to represent infinite quantity
            } else {
                stock.setQuantity(Integer.parseInt(quantity));
            }
            stock.setVolatility(config.getDouble(path + ".volatility", stock.getVolatility()));
        }
    }

    public static void saveToConfig(FileConfiguration config) {
        for (StockEnum stock : values()) {
            String path = "stocks." + stock.getName();
            config.set(path + ".price", stock.getPrice());
            config.set(path + ".quantity", stock.isInfiniteSupply() ? "infinite" : stock.getQuantity());
            config.set(path + ".volatility", stock.getVolatility());
        }
    }
}