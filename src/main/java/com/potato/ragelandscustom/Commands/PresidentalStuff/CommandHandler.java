package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Functions.Chat;
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
            case "resetvotes":
            case "resetcampaign":
                if (!sender.hasPermission("ragelands.admin")) {
                    sender.sendMessage(Chat.prefix + " You do not have permission to use this command.");
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
                if (start) {
                    main.startCampaign();
                } else {
                    main.endCampaign();
                }
                sender.sendMessage(Chat.prefix + " Voting campaign " + (start ? "started" : "stopped"));
                break;

            case "vote":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Chat.prefix + " Only players can use this command.");
                    return true;
                }
                Player player = (Player) sender;
                if (!config.getBoolean("campaign_active")) {
                    player.sendMessage(Chat.prefix + " There is no active voting campaign.");
                    return true;
                }

                List<String> votedPlayers = config.getStringList("voted_players");
                if (votedPlayers.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Chat.prefix + " You have already voted.");
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
                sender.sendMessage(Chat.prefix + " President set to " + presidentName);
                break;

            case "removepresident":
                config.set("president", "");
                main.saveVotingConfig();
                sender.sendMessage(Chat.prefix + " President removed.");
                break;

            case "voteresults":
                if (config.contains("votes")) {
                    Map<String, Object> votes = config.getConfigurationSection("votes").getValues(false);
                    sender.sendMessage(Chat.prefix + " Current vote results:");
                    for (Map.Entry<String, Object> entry : votes.entrySet()) {
                        sender.sendMessage(entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    sender.sendMessage(Chat.prefix + " No votes have been cast yet.");
                }
                break;

            case "runpresident":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Chat.prefix + " Only players can use this command.");
                    return true;
                }
                player = (Player) sender;
                List<String> candidates = config.getStringList("candidates");
                if (candidates.contains(player.getName())) {
                    player.sendMessage(Chat.prefix + " You are already running for president.");
                    return true;
                }
                candidates.add(player.getName());
                config.set("candidates", candidates);
                main.saveVotingConfig();
                player.sendMessage(Chat.prefix + " You are now running for president.");
                break;

            case "resetvotes":
                config.set("votes", null);
                main.saveVotingConfig();
                sender.sendMessage(Chat.prefix + " Votes have been reset.");
                break;

            case "resetcampaign":
                config.set("president", "");
                config.set("candidates", List.of());
                config.set("votes", null);
                config.set("campaign_active", false);
                config.set("voted_players", List.of());
                main.saveVotingConfig();
                sender.sendMessage(Chat.prefix + " Campaign has been reset.");
                break;
            case "removevote":
                player = (Player) sender;
                if (main.getVotingConfig().contains("voters." + player.getName())) {
                    main.removePlayerVote(player.getName());
                    player.sendMessage("Your vote has been removed.");
                } else {
                    player.sendMessage("You have not voted yet.");
                }
                break;
            default:
                sender.sendMessage(Chat.prefix + " Unknown subcommand.");
        }
        return true;
    }

    private void openVoteGUI(Player player) {
        List<String> candidates = main.getVotingConfig().getStringList("candidates");
        Inventory voteInventory = Bukkit.createInventory(null, 9, "Vote for President");

        for (String candidate : candidates) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(candidate));
            skull.setItemMeta(meta);
            voteInventory.addItem(skull);
        }

        player.openInventory(voteInventory);
    }
}