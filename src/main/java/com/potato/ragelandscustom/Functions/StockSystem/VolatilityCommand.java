package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VolatilityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setvolatility") && args.length == 2) {
            String stockName = args[0].toUpperCase();
            double newVolatility;

            try {
                newVolatility = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid volatility rate.");
                return false;
            }

            StockEnum stock;
            try {
                stock = StockEnum.valueOf(stockName);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("Stock not found.");
                return false;
            }

            stock.setVolatility(newVolatility);
            Main.getMainInstance().getConfig().set("stocks." + stock.getName() + ".volatility", newVolatility);
            Main.getMainInstance().saveConfig();
            sender.sendMessage("Volatility rate for " + stock.getName() + " set to " + newVolatility);

            return true;
        }

        return false;
    }

}
