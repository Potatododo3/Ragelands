package com.potato.ragelandscustom.Commands.PresidentalStuff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletionHandler implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("votestart", "vote", "setpresident", "removepresident", "voteresults", "runpresident", "resetvotes", "resetcampaign", "removevote", "resign");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(subcommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("votestart")) {
            if ("start".startsWith(args[1].toLowerCase())) {
                suggestions.add("start");
            }
            if ("stop".startsWith(args[1].toLowerCase())) {
                suggestions.add("stop");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setpresident")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(player.getName());
                }
            }
        }
        return suggestions;
    }
}