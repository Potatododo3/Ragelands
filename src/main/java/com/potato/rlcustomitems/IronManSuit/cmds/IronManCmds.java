package com.potato.rlcustomitems.IronManSuit.cmds;

import com.potato.rlcustomitems.IronManSuit.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IronManCmds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("ironman")) {
            if (args.length == 0) {
                Chat.msg(
                        player,
                        Chat.prefix + "&7Commands found:",
                        "&8&l >> &7/ironman info",
                        "&8&l >> &7/ironman version",
                        "&8&l >> &7/ironman commands",
                        "&8&l >> &7/ironman reload"
                );
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {
                    Chat.msg(
                            player,
                            Chat.prefix + "&7Information:",
                            "&8&l >>&7Ironman is an extension to the ragelands custom plugin created by no other than Licensed",
                            "&8&l >>This extension aims to bring his abilities, J.A.R.V.I.S and suits to ragelands!",
                            "&8&l >> &7Plugin Version: " + Chat.version()
                            );
                    return true;
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("ironman.reload")) {
                        Chat.msg(player, Chat.prefix + "Plugin has been reloaded!");
                        return true;
                    }
                    else{
                        Chat.msg(player, Chat.perm);
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("commands")) {
                    Chat.msg(
                            player,
                            Chat.prefix + "&7Commands found:",
                            "&8&l >> &7/ironman",
                            "&8&l >> &7/suits"
                    );
                }
            }
        }
        return false;
    }
}