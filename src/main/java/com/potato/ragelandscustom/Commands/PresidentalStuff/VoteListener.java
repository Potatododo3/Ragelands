package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class VoteListener implements Listener {
    private Main main;

    public VoteListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Vote for President")) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.CHEST) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                    Player player = (Player) event.getWhoClicked();
                    String playerName = player.getName();
                    SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                    String candidateName = skullMeta.getOwningPlayer().getName();

                    FileConfiguration votingConfig = main.getVotingConfig();

                    // Check if player has already voted
                    if (votingConfig.contains("voters." + playerName)) {
                        Chat.msg(player, Chat.prefix + "&7You have already voted! Use /ragelands removevote to change your vote.");
                        main.getLogger().info(playerName + " attempted to vote again.");
                        return;
                    }

                    // Register the vote
                    int currentVotes = votingConfig.getInt("votes." + candidateName, 0);
                    votingConfig.set("votes." + candidateName, currentVotes + 1);
                    votingConfig.set("voters." + playerName, candidateName);
                    main.saveVotingConfig();

                    Chat.msg(player, Chat.prefix + "&7You have voted for " + candidateName + "!");
                    main.getLogger().info(playerName + " voted for " + candidateName);
                    player.closeInventory();
                }
            }
        }
    }
}
