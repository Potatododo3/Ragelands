package com.potato.ragelandscustom.Functions.StockSystem;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

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

    public void updatePrice() {
        Random random = new Random();
        double changeFactor = 1 + (random.nextGaussian() * volatility);
        this.price *= changeFactor;
    }
}