package com.potato.ragelandscustom.Functions.StockSystem;

import lombok.Getter;
import lombok.Setter;

public enum StockEnum {
    RAGELANDS("RLN", 23500, 25000),
    PEESCOIN("PeesCoin", 0.002, 16000000),
    POTATOCOIN("PotatoCoin", 0.05, 1600000);

    @Getter
    private final String name;
    @Getter
    @Setter
    private double price;
    @Getter
    @Setter
    private int quantity;

    StockEnum(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}