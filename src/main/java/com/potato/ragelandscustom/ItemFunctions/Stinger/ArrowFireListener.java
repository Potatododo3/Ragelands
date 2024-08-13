package com.potato.ragelandscustom.ItemFunctions.Stinger;

import com.potato.ragelandscustom.Functions.Chat;
import com.potato.ragelandscustom.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArrowFireListener implements Listener {

    private final Main main;
    public static HashMap<UUID, Projectile> arrowMap;

    public ArrowFireListener(Main main) {
        this.main = main;
        arrowMap = new HashMap<>();
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();

        ItemStack itemInHand = shooter.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        if (!itemMeta.hasLore()) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null || !lore.contains(ChatColor.GRAY + "Hit mid air enemies and assert the high ground.")) {
            return;
        }
        if (shooter.isGliding() || shooter.isFlying()) {
            event.setCancelled(true);
            Chat.msg(shooter, Chat.color(Chat.prefix + "&7You can not use the stinger while flying!"));
            return;
        }
        if (event.getEntity() instanceof Arrow || event.getEntity() instanceof Firework) {
            Projectile projectile = event.getEntity();
            arrowMap.put(projectile.getUniqueId(), projectile); // Store the arrow's UUID
            new HomingArrowRunnable(projectile, main).runTaskTimer(main, 5, 1);
        }
    }

    public Projectile getArrow(UUID uuid) {
        return arrowMap.get(uuid); // Retrieve the arrow by UUID
    }
}