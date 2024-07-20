package com.potato.ragelandscustom.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BossDropItemCommand implements CommandExecutor {

    public static boolean BossDrops;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("ragelands.admin")) {
            commandSender.sendMessage("Unknown command. Type \"/help\" for help.");
            return true;
        }
        if (BossDrops) {
            BossDrops = false;
            commandSender.sendMessage("Successfully turned BossDrops off!");
            return true;
        }
        if (!BossDrops) {
            BossDrops = true;
            commandSender.sendMessage("Successfully turned BossDrops on!");
            return true;
        } else {
            BossDrops = true;
            commandSender.sendMessage("Successfully turned BossDrops on!");
            return true;
        }
    }

    public static boolean getNoDrops() {
        return BossDrops;
    }
}