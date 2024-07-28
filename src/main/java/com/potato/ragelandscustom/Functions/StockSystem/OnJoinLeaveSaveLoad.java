package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

public class OnJoinLeaveSaveLoad implements Listener {
    private final Main main;
    private final PlayerStockManager playerStockManager;

    public OnJoinLeaveSaveLoad(Main main, PlayerStockManager playerStockManager) {
        this.main = main;
        this.playerStockManager = playerStockManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map<StockEnum, Integer> stocks = playerStockManager.loadPlayerStocks(player);
        player.setMetadata("stocks", new FixedMetadataValue(main, stocks));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("stocks")) {
            Map<StockEnum, Integer> stocks = (Map<StockEnum, Integer>) player.getMetadata("stocks").get(0).value();
            playerStockManager.savePlayerStocks(player, stocks);
        }
    }
}
