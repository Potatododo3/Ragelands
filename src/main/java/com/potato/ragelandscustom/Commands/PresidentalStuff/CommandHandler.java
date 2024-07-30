package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private Main main;

    public CommandHandler(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("ragelands")) {
            if (args.length == 0) {
                player.sendMessage("Usage: /ragelands <subcommand>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "votestart":
                    if (player.hasPermission("ragelands.admin")) {
                        if (args.length > 1) {
                            switch (args[1].toLowerCase()) {
                                case "start":
                                    main.startCampaign();
                                    player.sendMessage("Presidential campaign started!");
                                    break;
                                case "stop":
                                    main.endCampaign();
                                    player.sendMessage("Presidential campaign ended!");
                                    break;
                                default:
                                    player.sendMessage("Usage: /ragelands votestart <start|stop>");
                            }
                        } else {
                            player.sendMessage("Usage: /ragelands votestart <start|stop>");
                        }
                    } else {
                        player.sendMessage("You don't have permission to start or stop a campaign.");
                    }
                    break;
                case "vote":
                    main.openVoteInventory(player);
                    break;
                case "setpresident":
                    if (player.hasPermission("ragelands.admin")) {
                        if (args.length < 2) {
                            player.sendMessage("Usage: /ragelands setpresident <player>");
                            return true;
                        }
                        String presidentName = args[1];
                        main.setPresident(presidentName);
                        player.sendMessage("President set to " + presidentName + "!");
                    } else {
                        player.sendMessage("You don't have permission to set the president.");
                    }
                    break;
                case "removepresident":
                    if (player.hasPermission("ragelands.admin")) {
                        main.removePresident();
                        player.sendMessage("President removed.");
                    } else {
                        player.sendMessage("You don't have permission to remove the president.");
                    }
                    break;
                case "voteresults":
                    main.displayVoteResults(player);
                    break;
                case "runpresident":
                    main.addCandidate(player.getName());
                    player.sendMessage("You are now running for president!");
                    break;
                case "resetvotes":
                    if (player.hasPermission("ragelands.admin")) {
                        main.resetVotes();
                        player.sendMessage("All votes have been reset.");
                    } else {
                        player.sendMessage("You don't have permission to reset votes.");
                    }
                    break;
                case "resetcampaign":
                    if (player.hasPermission("ragelands.admin")) {
                        main.resetCampaign();
                        player.sendMessage("Campaign has been reset.");
                    } else {
                        player.sendMessage("You don't have permission to reset the campaign.");
                    }
                    break;
                case "removevote":
                    main.removePlayerVote(player.getName());
                    player.sendMessage("Your vote has been removed.");
                    break;
                default:
                    player.sendMessage("Unknown subcommand. Usage: /ragelands <subcommand>");
                    break;
            }
            return true;
        }
        return false;
    }
}