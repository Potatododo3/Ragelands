package com.potato.ragelandscustom.IronManSuit;

import com.potato.ragelandscustom.IronManSuit.menu.Gui;
import com.potato.ragelandscustom.IronManSuit.menu.GuiItem;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Menus {

    public static void createTestMenu(Player player) {
        Gui gui = new Gui(Chat.jarvisMenu + "&8Suit Selection", 1);

        gui.setItems(
                new GuiItem<>(
                        new ItemStackBuilder(Material.IRON_CHESTPLATE)
                                .setName("&8&lMark 1")
                                .addLore("Code Name: None")
                                .addLore("Armour Type: None")
                                .build(),
                        (clicker, event) -> {
                            if (Data.Suit.contains(clicker)) {
                                Main.getSuitManager().eject(clicker);
                                clicker.sendMessage(Chat.jarvis + "Mark 1 ejected!");
                                clicker.closeInventory();
                                return;
                            }
                            Main.getSuitManager().apply(clicker);
                            clicker.sendMessage(Chat.jarvis + "Mark 1 is on its way!");
                            Data.suitAssigned.put(player, "MK1");
                            clicker.closeInventory();
                        }
                ),

                new GuiItem<>(
                        new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                                .setName("&8&lMark 42")
                                .addLore("Code Name: The Prodigal Son")
                                .addLore("Armour Type: Prehensile Suit")
                                .addLore("Abilities :")
                                .addLore("- Tracker (tracks nearby players)")
                                .addLore("- Explosive Laser (explodes on blocks and lights people on fire")
                                .build(),
                        (clicker, event) -> {
                            if (Data.Suit.contains(clicker)) {
                                Main.getSuitManager().eject(clicker);
                                clicker.sendMessage(Chat.jarvis + "Mark 42 ejected!");
                                clicker.closeInventory();
                                return;
                            }
                            Main.getSuitManager().apply(clicker);
                            clicker.sendMessage(Chat.jarvis + "Mark 42 is on its way!");
                            Data.suitAssigned.put(player, "MK42");
                            clicker.closeInventory();
                        }
                )

        );

        gui.open(player);

    }

}
