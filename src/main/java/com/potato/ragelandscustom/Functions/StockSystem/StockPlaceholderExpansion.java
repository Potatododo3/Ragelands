package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Map;

public class StockPlaceholderExpansion extends PlaceholderExpansion {

    private final Main main;

    public StockPlaceholderExpansion(Main main) {
        this.main = main;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ragelandstocks";
    }

    @Override
    public String getAuthor() {
        return "Potato_dodo0";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (!player.hasMetadata("stocks")) {
            return "0";
        }

        Map<StockEnum, Integer> stocks = (Map<StockEnum, Integer>) player.getMetadata("stocks").get(0).value();

        for (StockEnum stock : StockEnum.values()) {
            if (identifier.equals(stock.name().toLowerCase())) {
                return String.valueOf(stocks.getOrDefault(stock, 0));
            }
        }

        return null;
    }
}