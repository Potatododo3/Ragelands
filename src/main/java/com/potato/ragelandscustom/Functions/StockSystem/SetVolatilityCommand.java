package com.potato.ragelandscustom.Functions.StockSystem;

import com.potato.ragelandscustom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SetVolatilityCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public SetVolatilityCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("ragelands.admin")) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /setvolatility <stock> <value>");
            return false;
        }

        String stockName = args[0].toLowerCase();
        double volatility;

        try {
            volatility = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid volatility value. It must be a number.");
            return false;
        }

        for (StockEnum stock : StockEnum.values()) {
            if (stock.getName().equalsIgnoreCase(stockName)) {
                stock.setVolatility(volatility);
                File stockConfigFile = new File(main.getDataFolder(), "stocks.yml");
                YamlConfiguration stockConfig = YamlConfiguration.loadConfiguration(stockConfigFile);
                stockConfig.set("stocks." + stockName + ".volatility", volatility);
                try {
                    stockConfig.save(stockConfigFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendMessage("Set volatility of " + stockName + " to " + volatility);
                return true;
            }
        }

        sender.sendMessage("Stock not found.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(StockEnum.values())
                    .map(StockEnum::getName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
