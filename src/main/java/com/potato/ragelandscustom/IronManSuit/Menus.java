package com.potato.ragelandscustom.IronManSuit;

import com.potato.ragelandscustom.IronManSuit.menu.Gui;
import com.potato.ragelandscustom.IronManSuit.menu.GuiItem;
import com.potato.ragelandscustom.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.*;

public class Menus {

    public static void createTestMenu(Player player) {
        Gui gui = new Gui(Chat.jarvisMenu + "&8Suit Selection", 1);

        gui.setItems(
                new GuiItem<>(
                        new ItemStackBuilder(Material.IRON_CHESTPLATE)
                                .setName("&8&lMark 50")
                                .addLore("Code Name: None")
                                .addLore("Armour Type: None")
                                .build(),
                        (clicker, event) -> {
                            PersistentDataContainer playerpdc = clicker.getPersistentDataContainer();
                            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                                if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
                                    Main.getSuitManager().eject(clicker);
                                    playerpdc.set(MK50, PersistentDataType.BOOLEAN, true);
                                    Main.getSuitManager().apply(clicker);
                                    clicker.sendMessage(Chat.jarvis + "Mark 42 ejected and Mark 1 is on its way!");
                                    clicker.closeInventory();
                                    return;
                                }
                                Main.getSuitManager().eject(clicker);
                                clicker.sendMessage(Chat.jarvis + "Mark 1 ejected!");
                                clicker.closeInventory();
                                return;
                            }
                            Main.getSuitManager().apply(clicker);
                            clicker.sendMessage(Chat.jarvis + "Mark 1 is on its way!");
                            playerpdc.set(MK50, PersistentDataType.BOOLEAN, true);
                            clicker.closeInventory();
                        }
                ),

                new GuiItem<>(
                        new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                                .setName("&8&lMark 34")
                                .addLore("Code Name: The Prodigal Son")
                                .addLore("Armour Type: Prehensile Suit")
                                .addLore("Abilities :")
                                .addLore("- Tracker (tracks nearby players)")
                                .addLore("- Explosive Laser (explodes on blocks and lights people on fire")
                                .build(),
                        (clicker, event) -> {
                            PersistentDataContainer playerpdc = clicker.getPersistentDataContainer();
                            if (Boolean.TRUE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                                if (Boolean.TRUE.equals(playerpdc.get(MK50, PersistentDataType.BOOLEAN))) {
                                    Main.getSuitManager().eject(clicker);
                                    playerpdc.set(MK34, PersistentDataType.BOOLEAN, true
                                    );
                                    Main.getSuitManager().apply(clicker);
                                    clicker.sendMessage(Chat.jarvis + "Mark 1 ejected and Mark 42 is on its way!");
                                    clicker.closeInventory();
                                    return;
                                }
                                Main.getSuitManager().eject(clicker);
                                clicker.sendMessage(Chat.jarvis + "Mark 42 ejected!");
                                clicker.closeInventory();
                                return;
                            }
                            Main.getSuitManager().apply(clicker);
                            clicker.sendMessage(Chat.jarvis + "Mark 42 is on its way!");
                            playerpdc.set(MK34, PersistentDataType.BOOLEAN, true);
                            clicker.closeInventory();
                        }
                )

        );

        gui.open(player);

    }

}
