package com.potato.rlcustomitems.Functions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemGiverTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> items = Arrays.asList("magicboots", "shielddeflect", "longrangebow", "fragbomb", "flashbang", "cowboyboots", "freezeclock");
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

