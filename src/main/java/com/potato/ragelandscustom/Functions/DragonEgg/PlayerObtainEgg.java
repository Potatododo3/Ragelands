package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.IronManSuit.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PlayerObtainEgg implements Listener {

    @EventHandler
    public void onObtainEgg(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (e.getItem().getItemStack().getType() == Material.DRAGON_EGG) {
                String deadPlayersName = player.getDisplayName();
                String message = Chat.prefix + ChatColor.AQUA + deadPlayersName + ChatColor.RESET + " &c&lHAS OBTAINED THE DRAGON EGG!";
                for (Player announcingplayer : Bukkit.getOnlinePlayers()) {
                    Chat.msg(announcingplayer, message);

                }
            }
        }
    }
}
