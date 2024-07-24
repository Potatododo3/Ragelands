package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor {
    private Main main;

    public CommandHandler(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /ragelands <subcommand>");
            return true;
        }

        String subcommand = args[0];
        FileConfiguration config = main.getVotingConfig();

        switch (subcommand.toLowerCase()) {
            case "votestart":
            case "setpresident":
            case "removepresident":
            case "voteresults":
                if (!sender.hasPermission("ragelands.admin")) {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }
                break;
        }

        switch (subcommand.toLowerCase()) {
            case "votestart":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /ragelands votestart <start/stop>");
                    return true;
                }
                boolean start = args[1].equalsIgnoreCase("start");
                config.set("campaign_active", start);
                main.saveVotingConfig();
                sender.sendMessage("Voting campaign " + (start ? "started" : "stopped"));
                break;

            case "vote":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }
                Player player = (Player) sender;
                if (!config.getBoolean("campaign_active")) {
                    player.sendMessage("There is no active voting campaign.");
                    return true;
                }
                openVoteGUI(player);
                break;

            case "setpresident":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /ragelands setpresident <player>");
                    return true;
                }
                String presidentName = args[1];
                config.set("president", presidentName);
                main.saveVotingConfig();
                sender.sendMessage("President set to " + presidentName);
                break;

            case "removepresident":
                config.set("president", "");
                main.saveVotingConfig();
                sender.sendMessage("President removed.");
                break;

            case "voteresults":
                if (config.contains("votes")) {
                    Map<String, Object> votes = config.getConfigurationSection("votes").getValues(false);
                    sender.sendMessage("Current vote results:");
                    for (Map.Entry<String, Object> entry : votes.entrySet()) {
                        sender.sendMessage(entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    sender.sendMessage("No votes have been cast yet.");
                }
                break;

            case "runpresident":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }
                player = (Player) sender;
                List<String> candidates = config.getStringList("candidates");
                if (candidates.size() >= 3) {
                    player.sendMessage("There are already 3 candidates running for president.");
                    return true;
                }
                if (candidates.contains(player.getName())) {
                    player.sendMessage("You are already running for president.");
                    return true;
                }
                candidates.add(player.getName());
                config.set("candidates", candidates);
                main.saveVotingConfig();
                player.sendMessage("You are now running for president.");
                break;

            default:
                sender.sendMessage("Unknown subcommand.");
        }
        return true;
    }

    private void openVoteGUI(Player player) {
        List<String> candidates = main.getVotingConfig().getStringList("candidates");
        Inventory voteInventory = Bukkit.createInventory(null, 9, "Vote for President");

        for (String candidate : candidates) {
            ItemStack item = createPlayerHead(candidate);
            voteInventory.addItem(item);
        }

        player.openInventory(voteInventory);
    }

    private ItemStack createPlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        meta.setDisplayName(playerName);
        head.setItemMeta(meta);
        return head;
    }
}