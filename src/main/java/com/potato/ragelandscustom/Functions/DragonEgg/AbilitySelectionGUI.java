package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AbilitySelectionGUI implements Listener {

    private final Main main;

    public AbilitySelectionGUI(Main main) {
        this.main = main;
    }

    public void openAbilitySelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "Select Dragon Ability");

        ItemStack dragonSmash = new ItemStack(Material.ANVIL);
        ItemMeta smashMeta = dragonSmash.getItemMeta();
        smashMeta.setDisplayName("Dragon Smash");
        dragonSmash.setItemMeta(smashMeta);

        ItemStack dragonFlame = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta flameMeta = dragonFlame.getItemMeta();
        flameMeta.setDisplayName("Dragon Flame");
        dragonFlame.setItemMeta(flameMeta);

        ItemStack dragonSummon = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta summonMeta = dragonSummon.getItemMeta();
        summonMeta.setDisplayName("Dragon Summon");
        dragonSummon.setItemMeta(summonMeta);

        gui.setItem(2, dragonSmash);
        gui.setItem(4, dragonFlame);
        gui.setItem(6, dragonSummon);

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Select Dragon Ability")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            if (clickedItem.getItemMeta().getDisplayName().equals("Dragon Smash")) {
                // Trigger Dragon Smash ability
                triggerDragonSmash(player);
            } else if (clickedItem.getItemMeta().getDisplayName().equals("Dragon Flame")) {
                // Trigger Dragon Flame ability
                triggerDragonFlame(player);
            } else if (clickedItem.getItemMeta().getDisplayName().equals("Dragon Summon")) {
                // Trigger Dragon Summon ability
                triggerDragonSummon(player);
            }

            player.closeInventory();
        }
    }

    private void triggerDragonSmash(Player player) {
        new DragonSmashAbility(main).triggerAbility(player);
    }

    private void triggerDragonFlame(Player player) {
        new DragonFlameAbility(main).triggerAbility(player);
    }

    private void triggerDragonSummon(Player player) {
        new DragonSummonAbility(main).triggerAbility(player);
    }
}