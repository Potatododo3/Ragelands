package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.Functions.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PlayerDisableSit implements CommandExecutor {

    public static HashMap<Player, Boolean> toggledPlayers = new HashMap<>();
    public static boolean globalToggle = false;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            // Check if the correct number of arguments is provided
            if (args.length > 0 && args[0].equalsIgnoreCase("global")) {
                if (!player.hasPermission("ragelands.admin")) {
                    return true;
                }
                globalToggle = !globalToggle; // Toggle the global boolean
                Chat.msg(player, Chat.prefix + "Global cowboy riding " + (globalToggle ? "enabled" : "disabled"));
                return true;
            }

            // Toggle the player's status
            boolean currentStatus = toggledPlayers.getOrDefault(player, true);
            toggledPlayers.put(player, !currentStatus);
            Chat.msg(player, Chat.prefix + "You have " + (currentStatus ? "disabled" : "enabled") + " cowboy riding.");
        } else {
            commandSender.sendMessage(Chat.prefix + "This command can only be used by players.");
        }

        return true;
    }
}