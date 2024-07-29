package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Functions.ItemStackEditor;
import com.potato.ragelandscustom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Map;

public class StockGUI implements Listener {
    private final Main main;
    static int peesslot = Main.getMainInstance().getConfig().getInt("Peescoin");
    static int ragelandsslot = Main.getMainInstance().getConfig().getInt("RLN");
    static int potatoslot = Main.getMainInstance().getConfig().getInt("Potatocoin");

    public StockGUI(Main main) {
        this.main = main;
    }

    public static void openStockGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Stock Market");
        ItemStack frame = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        for (StockEnum stock : StockEnum.values()) {
            ItemStack item = null;
            if (stock.getName().equalsIgnoreCase("Ragelands")) {
                item = new ItemStack(Material.DIAMOND);
                item = ItemStackEditor.setDisplayNameItem(item, "&b&l" + stock.getName());
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                inv.setItem(ragelandsslot, item);
            } else if (stock.getName().equalsIgnoreCase("PeesCoin")) {
                item = new ItemStack(Material.PAPER);
                item = ItemStackEditor.setDisplayNameItem(item, "&f" + stock.getName());
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                inv.setItem(peesslot, item);
            } else if (stock.getName().equalsIgnoreCase("Potatocoin")) {
                item = new ItemStack(Material.PAPER);
                item = ItemStackEditor.setDisplayNameItem(item, "&f" + stock.getName());
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                inv.setItem(potatoslot, item);
            }
        }

        for (int i = 0; i <= 26; i++) {
            if (i == ragelandsslot || i == peesslot || i == potatoslot) {
                continue; // Use continue to skip special slots
            }
            inv.setItem(i, frame);
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !event.getView().getTitle().equals("Stock Market")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ClickType clickType = event.getClick();
        int slot = event.getSlot();

        // Retrieve player's stock data from metadata
        Map<StockEnum, Integer> stocks = (Map<StockEnum, Integer>) player.getMetadata("stocks").get(0).value();

        switch (clickType) {
            case LEFT:
                handleStockTransaction(player, stocks, slot, 1, TransactionType.BUY);
                break;
            case SHIFT_LEFT:
                handleStockTransaction(player, stocks, slot, 10, TransactionType.BUY);
                break;
            case RIGHT:
                handleStockTransaction(player, stocks, slot, 1, TransactionType.SELL);
                break;
            case SHIFT_RIGHT:
                handleStockTransaction(player, stocks, slot, 10, TransactionType.SELL);
                break;
            case MIDDLE:
                handleStockTransaction(player, stocks, slot, Integer.MAX_VALUE, TransactionType.BUY);
                break;
            default:
                break;
        }

        // Update player's metadata
        player.setMetadata("stocks", new FixedMetadataValue(main, stocks));
    }
    private void handleStockTransaction(Player player, Map<StockEnum, Integer> stocks, int slot, int amount, TransactionType type) {
        StockEnum stock = getStockBySlot(slot);

        if (stock == null) {
            return;
        }

        int currentAmount = stocks.getOrDefault(stock, 0);
        int newAmount = currentAmount;
        double stockPrice = main.getStockPrice(stock);

        if (type == TransactionType.BUY) {
            double playerBalance = main.getPlayerBalance(player);
            if (amount == Integer.MAX_VALUE) {
                amount = (int) (playerBalance / stockPrice);
            }

            if (playerBalance >= amount * stockPrice) {
                newAmount = currentAmount + amount;
                // Subtract the cost from player's balance
                main.updatePlayerBalance(player, playerBalance - (amount * stockPrice));
            } else {
                player.sendMessage("Not enough balance to buy " + amount + " " + stock.name() + "(s).");
                return;
            }
        } else if (type == TransactionType.SELL) {
            if (currentAmount >= amount) {
                newAmount = currentAmount - amount;
                // Add the value to player's balance
                double playerBalance = main.getPlayerBalance(player);
                main.updatePlayerBalance(player, playerBalance + (amount * stockPrice));
            } else {
                player.sendMessage("Not enough stocks to sell.");
                return;
            }
        }

        stocks.put(stock, newAmount);
        player.sendMessage((type == TransactionType.BUY ? "Bought " : "Sold ") + amount + " " + stock.name() + "(s).");
    }
    private StockEnum getStockBySlot(int slot) {
        // Define the slot to stock mapping
        if (slot == ragelandsslot) {
            return StockEnum.RAGELANDS;
        } else if (slot == peesslot) {
            return StockEnum.PEESCOIN;
        } else if (slot == potatoslot) {
            return StockEnum.POTATOCOIN;
        } else {
            return null;
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Handle inventory close
    }

    public void updateStockGUI(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();

        int slot = 11;
        for (StockEnum stock : StockEnum.values()) {
            ItemStack item = inv.getItem(slot++);
            if (item != null && item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Arrays.asList("Price: $" + stock.getPrice(), "Quantity: " + stock.getQuantity()));
                item.setItemMeta(meta);
            }
        }
    }
}