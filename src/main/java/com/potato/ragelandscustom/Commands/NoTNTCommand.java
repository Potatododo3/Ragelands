package com.potato.ragelandscustom.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class NoTNTCommand implements CommandExecutor {

    public static boolean NoTNT;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("ragelands.admin")) {
            commandSender.sendMessage("Unknown command. Type \"/help\" for help.");
            return true;
        }
        if (NoTNT) {
            NoTNT = false;
            commandSender.sendMessage("Successfully turned NoTNT off!");
            return true;
        }
        if (!NoTNT) {
            NoTNT = true;
            commandSender.sendMessage("Successfully turned NoTNT on!");
            return true;
        }
        else {
            NoTNT = true;
            commandSender.sendMessage("Successfully turned NoTNT on!");
            return true;
        }
    }
    public static boolean getNoTNT() {
        return NoTNT;
    }
}
