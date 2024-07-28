package com.potato.ragelandscustom.Functions.StockSystem;

public class StockManager {

    public void updateStockPrice(StockEnum stock, double newPrice) {
        stock.setPrice(newPrice);
    }

    public void updateStockQuantity(StockEnum stock, int newQuantity) {
        stock.setQuantity(newQuantity);
    }
}