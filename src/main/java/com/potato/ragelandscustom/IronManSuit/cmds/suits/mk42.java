package com.potato.ragelandscustom.IronManSuit.cmds.suits;

import com.potato.ragelandscustom.Functions.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static com.potato.ragelandscustom.IronManSuit.SuitManager.MK34;
import static com.potato.ragelandscustom.IronManSuit.SuitManager.suitOn;

public class mk42 implements CommandExecutor {

    ArrayList<Player> nightVision = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        Player player = (Player) sender;

        try {
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            if (Boolean.FALSE.equals(playerpdc.get(suitOn, PersistentDataType.BOOLEAN))) {
                Chat.msg(player, Chat.prefix + "&7Suit required to run this command!");
                return true;
            }
        }
        catch (NullPointerException e ) {
            Chat.msg(player, Chat.prefix + "&7Suit required to run this command!");
            return true;
        }
        try {
            PersistentDataContainer playerpdc = player.getPersistentDataContainer();
            if (Boolean.FALSE.equals(playerpdc.get(MK34, PersistentDataType.BOOLEAN))) {
                Chat.msg(player, Chat.jarvis + "&7Mk42 suit required to run this command!");
                return true;
            }
        } catch (NullPointerException e) {
            Chat.msg(player, Chat.jarvis + "&7Mk42 suit required to run this command!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("mk42")) {
            if (args.length == 0) {
                Chat.msg(player,
                        Chat.prefix + "Available mk42 suit commands:",
                        "&8&l >> &7/mk42 nightvision");
                return true;
            }

            if (args[0].equalsIgnoreCase("nightvision")) {
                if (nightVision.contains(player)) {
                    Chat.msg(player, Chat.jarvis + "&7Night vision deactivated!");
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    nightVision.remove(player);
                    return true;
                }
                Chat.msg(player, Chat.jarvis + "&7Night vision activated!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                nightVision.add(player);
                return true;
            }
        }

        return false;
    }
}
