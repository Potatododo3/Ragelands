package com.potato.rlcustomitems.Commands;

import com.potato.rlcustomitems.CC;
import com.potato.rlcustomitems.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemGiver implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("testplugin.admin")) {
                if (args.length == 0) {
                    player.sendMessage("You have to specify an item!");
                    return false;
                }
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "magicboots":
                            giveMagicBoots(player);
                            return true;
                        case "shielddeflect":
                            giveShieldDeflect(player);
                            return true;
                        case "longrangebow":
                            giveLongRangeBow(player);
                            return true;
                        case "fragbomb":
                            giveFragBomb(player);
                            return true;
                        case "flashbang":
                            giveFlashBang(player);
                            return true;
                        case "cowboyboots":
                            giveCowboyBoots(player);
                            return true;
                        case "freezeclock":
                            giveFreezeClock(player);
                            return true;
                        default:
                            player.sendMessage("Unknown item.");
                            return false;
                    }
                }
                if (args.length == 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        switch (args[0].toLowerCase()) {
                            case "magicboots":
                                giveMagicBoots(target);
                                return true;
                            case "shielddeflect":
                                giveShieldDeflect(target);
                                return true;
                            case "longrangebow":
                                giveLongRangeBow(target);
                                return true;
                            case "fragbomb":
                                giveFragBomb(target);
                                return true;
                            case "flashbang":
                                giveFlashBang(target);
                                return true;
                            case "cowboyboots":
                                giveCowboyBoots(target);
                                return true;
                            case "freezeclock":
                                giveFreezeClock(target);
                                return true;
                            default:
                                player.sendMessage("Unknown item.");
                                return false;
                        }
                    }
                }
                if (args.length >= 3) {
                    return false;
                }
            }
            else if (!(player.hasPermission("testplugin.admin"))) {
                player.sendMessage("Permission 'testplugin.admin' required!");
            }
        }

        return false;
    }

    private void giveShieldDeflect(Player player) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            ItemStack deflectingShield = DataManager.getInstance().getDeflectingShield();
            player.getInventory().setItemInMainHand(deflectingShield);
        }
        else {
            player.sendMessage(CC.translate("&#1FEBBA&lY&#1FEABB&lo&#1EE9BC&lu &#1DE8BD&la&#1DE7BE&ll&#1DE6BF&lr&#1CE5BF&le&#1CE4C0&la&#1CE4C1&ld&#1BE3C2&ly &#1AE1C3&lh&#1AE0C4&la&#1ADFC5&lv&#19DFC5&le &#19DDC7&la&#18DCC8&ln &#17DAC9&li&#17DACA&lt&#17D9CB&le&#16D8CC&lm &#15D6CD&li&#15D5CE&ln &#14D4CF&ly&#14D3D0&lo&#14D2D1&lu&#13D1D2&lr &#12D0D3&lm&#12CFD4&la&#12CED5&li&#11CDD5&ln &#11CBD7&lh&#10CBD8&la&#10CAD8&ln&#0FC9D9&ld&#0FC8DA&l!"));
        }
    }

    private void giveMagicBoots(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null) {
                ItemStack magicBoots = DataManager.getInstance().getMagicBoots();
                player.getInventory().setItem(i, magicBoots);
                return;
            }
        }
    }
    private void giveLongRangeBow(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null) {
                ItemStack longRangeBow = DataManager.getInstance().getLongRangeBow();
                player.getInventory().setItem(i, longRangeBow);
                return;
            }
        }
    }
    private void giveFragBomb(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null) {
                ItemStack fragBomb = DataManager.getInstance().getFragBomb();
                player.getInventory().setItem(i, fragBomb);
                return;
            }
        }
    }
    private void giveFlashBang(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null) {
                ItemStack flashBang = DataManager.getInstance().getFlashBang();
                player.getInventory().setItem(i, flashBang);
                return;
            }
        }
    }
    private void giveCowboyBoots(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR) {
                ItemStack cowBoyBoots = DataManager.getInstance().getCowboyBoots();
                player.getInventory().setItem(i, cowBoyBoots);
                return;
            }
        }
    }
    private void giveFreezeClock(Player player) {
        for (int i = 0; i < 37; i ++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR) {
                ItemStack FreezeClock = DataManager.getInstance().getFreezeClock();
                player.getInventory().setItem(i, FreezeClock);
                return;
            }
        }
    }
}
