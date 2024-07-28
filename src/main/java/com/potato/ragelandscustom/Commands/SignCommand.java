package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.Functions.ItemStackBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SignCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.getInventory().getItemInMainHand().isEmpty()) {
                return true;
            }
            String playerName = player.getDisplayName();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemStack newItemStack = new ItemStackBuilder(itemStack).addLore("Signed by " + playerName).build();
        }

        return false;
    }
}
