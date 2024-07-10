package com.potato.rlcustomitems.IronManSuit.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IronManTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> items = Arrays.asList("info", "version", "commands", "reload");
            List<String> suggestions = new ArrayList<>();
            for (String item : items) {
                if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(item);
                }
            }
            return suggestions;
        }

        return null;
    }
}