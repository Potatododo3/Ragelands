package com.potato.ragelandscustom.Commands.PresidentalStuff;

import com.potato.ragelandscustom.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class VoteListener implements Listener {
    private Main main;

    public VoteListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Vote for President")) {
            return;
        }
        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
            return;
        }

        String candidate = event.getCurrentItem().getItemMeta().getDisplayName();
        main.getVotingConfig().set("votes." + candidate, main.getVotingConfig().getInt("votes." + candidate, 0) + 1);
        main.saveVotingConfig();
        event.getWhoClicked().sendMessage("You voted for " + candidate);
        event.getWhoClicked().closeInventory();
    }
}