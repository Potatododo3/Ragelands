package com.potato.ragelandscustom.Functions.DragonEgg;

import com.potato.ragelandscustom.Functions.Chat;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathWithEgg implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player deadPlayer = e.getPlayer();
        if (deadPlayer.getInventory().contains(new ItemStack(Material.DRAGON_EGG))) {
            String deadPlayersName = deadPlayer.getDisplayName();
            String message = Chat.prefix + NamedTextColor.AQUA + deadPlayersName + " &c&lHAS DIED WITH THE DRAGON EGG!";
            for (Player player : Bukkit.getOnlinePlayers()) {
                Chat.msg(player, message);
            }
        }
    }
}