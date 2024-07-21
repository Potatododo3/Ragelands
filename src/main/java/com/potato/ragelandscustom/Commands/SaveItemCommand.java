package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SaveItemCommand implements CommandExecutor {

    private final Main main;

    public SaveItemCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        if (!sender.hasPermission("ragelands.admin")) {
            return true;
        }

        Player player = (Player) sender;
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (heldItem == null) {
            player.sendMessage("You are not holding any item.");
            return true;
        }

        FileConfiguration customConfig = main.getCustomConfig();
        customConfig.set("saved-item", heldItem);
        main.saveCustomConfig();

        player.sendMessage("Item saved successfully.");
        return true;
    }
}