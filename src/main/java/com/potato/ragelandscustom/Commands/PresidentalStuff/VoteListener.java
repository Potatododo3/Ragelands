package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class VoteListener implements Listener {
    private Main main;

    public VoteListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Vote for President")) {
            event.setCancelled(true); // Prevent any inventory modification

            if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.CHEST) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                    Player player = (Player) event.getWhoClicked();
                    String playerName = player.getName();
                    String candidateName = ((org.bukkit.inventory.meta.SkullMeta) clickedItem.getItemMeta()).getOwningPlayer().getName();

                    FileConfiguration votingConfig = main.getVotingConfig();

                    // Check if player has already voted
                    if (votingConfig.contains("voters." + playerName)) {
                        player.sendMessage("You have already voted! Use /ragelands removevote to change your vote.");
                        return;
                    }

                    // Register the vote
                    int currentVotes = votingConfig.getInt("votes." + candidateName, 0);
                    votingConfig.set("votes." + candidateName, currentVotes + 1);
                    votingConfig.set("voters." + playerName, candidateName); // Track player's vote
                    main.saveVotingConfig();

                    player.sendMessage("You have voted for " + candidateName + "!");
                    player.closeInventory();
                }
            }
        }
    }
}
