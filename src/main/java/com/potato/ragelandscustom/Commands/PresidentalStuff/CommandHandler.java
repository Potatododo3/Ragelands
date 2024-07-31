package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Functions.Chat;
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
                Chat.msg(player, Chat.prefix + "&7Usage: /ragelands <subcommand>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "votestart":
                    if (player.hasPermission("ragelands.admin")) {
                        if (args.length > 1) {
                            switch (args[1].toLowerCase()) {
                                case "start":
                                    main.startCampaign();
                                    Chat.msg(player ,Chat.prefix + "&7Presidential campaign started!");
                                    break;
                                case "stop":
                                    main.endCampaign();
                                    Chat.msg(player, Chat.prefix + "&7Presidential campaign ended!");
                                    break;
                                default:
                                    Chat.msg(player, Chat.prefix + "&7Usage: /ragelands votestart <start|stop>");
                            }
                        } else {
                            Chat.msg(player,Chat.prefix + "&7Usage: /ragelands votestart <start|stop>");
                        }
                    } else {
                        Chat.msg(player, Chat.prefix  +"&7You don't have permission to start or stop a campaign.");
                    }
                    break;
                case "vote":
                    main.openVoteInventory(player);
                    break;
                case "setpresident":
                    if (player.hasPermission("ragelands.admin")) {
                        if (args.length < 2) {
                            Chat.msg(player, Chat.prefix + "&7Usage: /ragelands setpresident <player>");
                            return true;
                        }
                        String presidentName = args[1];
                        main.setPresident(presidentName);
                        Chat.msg(player, Chat.prefix + "&7President set to " + presidentName + "!");
                    } else {
                        Chat.msg(player, Chat.prefix + "&7You don't have permission to set the president.");
                    }
                    break;
                case "removepresident":
                    if (player.hasPermission("ragelands.admin")) {
                        main.removePresident();
                        Chat.msg(player, Chat.prefix  +"&7President removed.");
                    } else {
                        Chat.msg(player, Chat.prefix + "&7You don't have permission to remove the president.");
                    }
                    break;
                case "voteresults":
                    main.displayVoteResults(player);
                    break;
                case "runpresident":
                    main.addCandidate(player.getName());
                    Chat.msg(player, Chat.prefix + "&7You are now running for president!");
                    break;
                case "resetvotes":
                    if (player.hasPermission("ragelands.admin")) {
                        main.resetVotes();
                        Chat.msg(player, Chat.prefix + "&7All votes have been reset.");
                    } else {
                        Chat.msg(player,Chat.prefix  + "&7You don't have permission to reset votes.");
                    }
                    break;
                case "resetcampaign":
                    if (player.hasPermission("ragelands.admin")) {
                        main.resetCampaign();
                        Chat.msg(player, Chat.prefix + "&7Campaign has been reset.");
                    } else {
                        Chat.msg(player, Chat.prefix + "&7You don't have permission to reset the campaign.");
                    }
                    break;
                case "removevote":
                    main.removePlayerVote(player.getName());
                    Chat.msg(player, Chat.prefix  + "&7Your vote has been removed.");
                    break;
                default:
                    Chat.msg(player ,Chat.prefix  + "&7Unknown subcommand. Usage: /ragelands <subcommand>");
                    break;
            }
            return true;
        }
        return false;
    }
}