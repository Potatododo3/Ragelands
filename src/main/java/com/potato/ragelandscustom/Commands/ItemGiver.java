package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                        case "stinger":
                            giveStinger(player);
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
                            case "stinger":
                                giveStinger(target);
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
    private ItemStack createStinger() {
        ItemStack stinger = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = stinger.getItemMeta();
        meta.setDisplayName("FIM-92 Stinger");
        stinger.setItemMeta(meta);
        return stinger;
    }
    private void giveShieldDeflect(Player player) {
        ItemStack deflectingShield = DataManager.getInstance().getDeflectingShield();
        player.getInventory().addItem(deflectingShield);
    }

    private void giveMagicBoots(Player player) {
        ItemStack magicBoots = DataManager.getInstance().getMagicBoots();
        player.getInventory().addItem(magicBoots);
    }
    private void giveLongRangeBow(Player player) {
        ItemStack longRangeBow = DataManager.getInstance().getLongRangeBow();
        player.getInventory().addItem(longRangeBow);
    }
    private void giveFragBomb(Player player) {
        ItemStack fragBomb = DataManager.getInstance().getFragBomb();
        player.getInventory().addItem(fragBomb);
    }
    private void giveFlashBang(Player player) {
        ItemStack flashBang = DataManager.getInstance().getFlashBang();
        player.getInventory().addItem(flashBang);
    }
    private void giveCowboyBoots(Player player) {
        ItemStack cowboyBoots = DataManager.getInstance().getCowboyBoots();
        player.getInventory().addItem(cowboyBoots);
    }
    private void giveFreezeClock(Player player) {
        ItemStack freezeClock = DataManager.getInstance().getFreezeClock();
        player.getInventory().addItem(freezeClock);
    }
    private void giveStinger(Player player) {

    }
}
