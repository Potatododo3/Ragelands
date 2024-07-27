package com.potato.ragelandscustom.Commands.PresidentalStuff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AssassinationPresidentToggle implements CommandExecutor {

    public static boolean assassinationToggle = true;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender.hasPermission("ragelands.admin")) {
            if (assassinationToggle) {
                assassinationToggle = false;
                commandSender.sendMessage("Assassination toggle has been disabled!");
                return true;
            }
            else {
                assassinationToggle = true;
                commandSender.sendMessage("Assassination toggle has been enabled!");
                return true;
            }
        }
        else {
            commandSender.sendMessage("You do not have permission to execute this command!");
            return true;
        }
    }
}
