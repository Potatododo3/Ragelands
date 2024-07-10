package com.potato.rlcustomitems.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EjectCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.getPassengers().isEmpty()) {
                player.sendMessage(ChatColor.RED + "You have to have players on top of you to execute this command!");
                return true;
            }
            else if (!player.getPassengers().isEmpty()) {
                player.eject();
                return true;
            }
        }

        return false;
    }
}
