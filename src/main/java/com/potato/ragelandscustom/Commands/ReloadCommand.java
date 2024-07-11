package com.potato.ragelandscustom.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloadCommand implements CommandExecutor {
    private final Plugin plugin;

    public ReloadCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("reloadragelandsplugin")) {
            if (!sender.hasPermission("ragelands.reload")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }
            // Perform your reload actions here, e.g., reload configuration files
            plugin.reloadConfig();
            sender.sendMessage("Plugin reloaded successfully!");
            return true;
        }
        return false;
    }
}
