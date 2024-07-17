package com.potato.ragelandscustom.ItemFunctions.Stinger;

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
    private final HashMap<UUID, Projectile> arrowMap;

    public ArrowFireListener(Main main) {
        this.main = main;
        this.arrowMap = new HashMap<>();
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            System.out.println("ArrowFireListener: Shooter is not a player.");
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();

        ItemStack itemInHand = shooter.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            System.out.println("ArrowFireListener: Shooter's item has no meta.");
            return;
        }

        if (!itemMeta.hasLore()) {
            System.out.println("ArrowFireListener: Shooter's item has no lore.");
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null || !lore.contains(ChatColor.GRAY + "Hit mid air enemies and assert the high ground.")) {
            System.out.println("ArrowFireListener: Shooter's item is not similar to Stinger.");
            System.out.println("ArrowFireListener: Shooter's item lore: " + lore);
            return;
        }

        if (event.getEntity() instanceof Arrow || event.getEntity() instanceof Firework) {
            Projectile projectile = event.getEntity();
            arrowMap.put(projectile.getUniqueId(), projectile); // Store the arrow's UUID
            System.out.println("ArrowFireListener: Projectile launched and stored with UUID: " + projectile.getUniqueId());
            new HomingArrowRunnable(projectile).runTaskTimer(main, 5, 1);
        }
    }

    public Projectile getArrow(UUID uuid) {
        return arrowMap.get(uuid); // Retrieve the arrow by UUID
    }
}