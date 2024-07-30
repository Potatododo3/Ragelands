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

import java.util.List;
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
                inv.setItem(peesslot, item);
                System.out.println("PeesCoin item set in slot " + peesslot);
            } else if (stock.getName().equalsIgnoreCase("Potatocoin")) {
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

    public void updateStockGUI(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();

        for (StockEnum stock : StockEnum.values()) {
            int slot;
            if (stock.getName().equalsIgnoreCase("RLN")) {
                slot = ragelandsslot;
            } else if (stock.getName().equalsIgnoreCase("PeesCoin")) {
                slot = peesslot;
            } else if (stock.getName().equalsIgnoreCase("Potatocoin")) {
                slot = potatoslot;
            } else {
                continue;
            }

            ItemStack item = inv.getItem(slot);
            if (item != null && item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                lore.set(0, "Price: $" + stock.getPrice());
                lore.set(1, "Quantity: " + stock.getQuantity());
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }
}