package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Functions.ItemStackEditor;
import com.potato.ragelandscustom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Map;

public class StockGUI implements Listener {
    private final Main main;
    static int peesslot = 11;
    static int ragelandsslot = 13;
    static int potatoslot = 15;

    public StockGUI(Main main) {
        this.main = main;
    }

    public static void openStockGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Stock Market");
        ItemStack frame = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        for (StockEnum stock : StockEnum.values()) {
            ItemStack item = null;
            if (stock.getName().equalsIgnoreCase("RLN")) {
                System.out.println("Creating item for RLN");
                item = new ItemStack(Material.DIAMOND);
                item = ItemStackEditor.setDisplayNameItem(item, Chat.color("&b&l" + stock.getName()));
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                item = ItemStackEditor.addLore(item, "&eLeft click to buy 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-left click to buy 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eRight click to sell 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-right click to sell 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eMiddle click to buy as many " + stock.getName() + "(s) as you can");
                item = ItemStackEditor.addLore(item, "&eDrop to sell as many " + stock.getName() + "(s) as you can");
                inv.setItem(ragelandsslot, item);
                System.out.println("Ragelands item set in slot " + ragelandsslot);
            } else if (stock.getName().equalsIgnoreCase("PeesCoin")) {
                System.out.println("Creating item for PeesCoin");
                item = new ItemStack(Material.PAPER);
                item = ItemStackEditor.setDisplayNameItem(item, Chat.color("&f" + stock.getName()));
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                item = ItemStackEditor.addLore(item, "&eLeft click to buy 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-left click to buy 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eRight click to sell 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-right click to sell 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eMiddle click to buy as many " + stock.getName() + "(s) as you can");
                item = ItemStackEditor.addLore(item, "&eDrop to sell as many " + stock.getName() + "(s) as you can");
                inv.setItem(peesslot, item);
                System.out.println("PeesCoin item set in slot " + peesslot);
            } else if (stock.getName().equalsIgnoreCase("PotatoCoin")) {
                System.out.println("Creating item for Potatocoin");
                item = new ItemStack(Material.PAPER);
                item = ItemStackEditor.setDisplayNameItem(item, Chat.color("&f" + stock.getName()));
                item = ItemStackEditor.addLore(item, "&7Price: $" + stock.getPrice());
                item = ItemStackEditor.addLore(item, "&7Quantity: " + stock.getQuantity());
                item = ItemStackEditor.addLore(item, "&eLeft click to buy 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-left click to buy 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eRight click to sell 1 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eShift-right click to sell 10 " + stock.getName());
                item = ItemStackEditor.addLore(item, "&eMiddle click to buy as many " + stock.getName() + "(s) as you can");
                item = ItemStackEditor.addLore(item, "&eDrop to sell as many " + stock.getName() + "(s) as you can");
                inv.setItem(potatoslot, item);
                System.out.println("Potatocoin item set in slot " + potatoslot);
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
            case DROP:
                handleStockTransaction(player, stocks, slot, Integer.MAX_VALUE, TransactionType.SELL);
                break;
            default:
                break;
        }

        // Update player's metadata
        player.setMetadata("stocks", new FixedMetadataValue(main, stocks));
        // Refresh the inventory to show updated quantities and prices
        updateStockGUI(player);
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

            if (playerBalance >= amount * stockPrice && stock.getQuantity() >= amount) {
                newAmount = currentAmount + amount;
                // Subtract the cost from player's balance
                main.updatePlayerBalance(player, playerBalance - (amount * stockPrice));
                stock.setQuantity(stock.getQuantity() - amount); // Decrease stock quantity
                main.saveStockQuantities(); // Save updated quantities
            } else {
                Chat.msg(player, Chat.prefix + "&7Not enough balance or stocks to buy " + amount + " " + stock.name() + "(s).");
                return;
            }
        } else if (type == TransactionType.SELL) {
            if (amount == Integer.MAX_VALUE) {
                amount = currentAmount;
            }

            if (currentAmount >= amount) {
                newAmount = currentAmount - amount;
                // Add the value to player's balance
                double playerBalance = main.getPlayerBalance(player);
                main.updatePlayerBalance(player, playerBalance + (amount * stockPrice));
                stock.setQuantity(stock.getQuantity() + amount); // Increase stock quantity
                main.saveStockQuantities(); // Save updated quantities
            } else {
                Chat.msg(player, Chat.prefix + "&7Not enough stocks to sell.");
                return;
            }
        }

        stocks.put(stock, newAmount);
        Chat.msg(player, Chat.prefix + (type == TransactionType.BUY ? "&7Bought " : "&7Sold ") + amount + " " + stock.name() + "(s).");
    }
    private StockEnum getStockBySlot(int slot) {
        // Define the slot to stock mapping
        if (slot == ragelandsslot) {
            return StockEnum.RLN;
        } else if (slot == peesslot) {
            return StockEnum.PEESCOIN;
        } else if (slot == potatoslot) {
            return StockEnum.POTATOCOIN;
        } else {
            return null;
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map<StockEnum, Integer> stocks = main.getPlayerStockManager().loadPlayerStocks(player);
        player.setMetadata("stocks", new FixedMetadataValue(main, stocks));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Map<StockEnum, Integer> stocks = (Map<StockEnum, Integer>) player.getMetadata("stocks").get(0).value();
        main.getPlayerStockManager().savePlayerStocks(player, stocks);
    }
    private int getSlotForStock(StockEnum stock) {
        switch (stock) {
            case RLN:
                return ragelandsslot;
            case PEESCOIN:
                return peesslot;
            case POTATOCOIN:
                return potatoslot;
            default:
                return -1;
        }
    }
    public void updateStockGUI(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();

        for (StockEnum stock : StockEnum.values()) {
            ItemStack item = inv.getItem(getSlotForStock(stock)); // Implement getSlotForStock method
            if (item != null && item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Arrays.asList(
                        Chat.color("&7Price: $" + stock.getPrice()),
                        Chat.color("&7Quantity: " + stock.getQuantity()),
                        Chat.color("&eLeft click to buy 1 " + stock.getName()),
                        Chat.color("&eShift-left click to buy 10 " + stock.getName()),
                        Chat.color("&eRight click to sell 1 " + stock.getName()),
                        Chat.color("&eShift-right click to sell 10 " + stock.getName()),
                        Chat.color("&eMiddle click to buy as many " + stock.getName() + "(s) as you can"),
                        Chat.color("&eDrop to sell as many " + stock.getName() + "(s) as you can")
                ));
                item.setItemMeta(meta);
            }
        }
    }
}