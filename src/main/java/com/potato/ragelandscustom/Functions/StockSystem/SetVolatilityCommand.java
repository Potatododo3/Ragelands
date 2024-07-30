package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetVolatilityCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public SetVolatilityCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ragelands.admin")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("Usage: /setvolatility <stock> <volatility>");
            return true;
        }

        String stockName = args[0].toUpperCase();
        double volatility;
        try {
            volatility = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid volatility value. Please enter a valid number.");
            return true;
        }

        StockEnum stock;
        try {
            stock = StockEnum.valueOf(stockName);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Invalid stock name. Available stocks: RLN, PEESCOIN, POTATOCOIN.");
            return true;
        }

        stock.setVolatility(volatility);
        main.getConfig().set("stocks." + stockName.toLowerCase() + ".volatility", volatility);
        main.saveConfig();
        player.sendMessage("Set volatility of " + stock.getName() + " to " + volatility);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("RLN", "PEESCOIN", "POTATOCOIN");
        } else if (args.length == 2) {
            return new ArrayList<>();
        }
        return null;
    }
}