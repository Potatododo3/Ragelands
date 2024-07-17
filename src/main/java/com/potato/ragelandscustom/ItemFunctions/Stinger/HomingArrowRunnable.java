package com.potato.ragelandscustom.ItemFunctions.Stinger;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HomingArrowRunnable extends BukkitRunnable {

    private final Entity arrow;
    private Entity target;

    public HomingArrowRunnable(Entity arrow) {
        this.arrow = arrow;
    }

    @Override
    public void run() {
        Long timeElapsed = 0L;
        timeElapsed = timeElapsed + 5;
        if (timeElapsed >= 205) {
            arrow.remove();
            this.cancel();
        }
        if (arrow.isDead() || (target != null && target.isDead())) {
            cancel();
            return;
        }

        if (target == null) {
            setTarget();
            if (target == null) {
                cancel();
                return;
            }
        }

        if (target instanceof Player) {
            Player player = (Player) target;
            if (player.isGliding() || player.isFlying()) {
                Projectile projectile = (Projectile) arrow;
                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    if (!isInSameFaction(shooter, player)) {
                        Vector newVector = target.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize().multiply(1.9);
                        arrow.setVelocity(newVector);
                    }
                }
            }
        }
    }

    private void setTarget() {
        List<Entity> nearbyEntities = arrow.getNearbyEntities(20, 20, 20);
        if (nearbyEntities.isEmpty()) {
            target = null;
            return;
        }

        Optional<Entity> optionalEntity = nearbyEntities.stream()
                .filter(entity -> entity instanceof LivingEntity)
                .min(Comparator.comparing(entity -> entity.getLocation().distanceSquared(arrow.getLocation())));

        if (optionalEntity.isPresent()) {
            target = optionalEntity.get();
        } else {
            target = null;
        }
    }

    private boolean isInSameFaction(Player player1, Player player2) {
        FPlayer fPlayer1 = FPlayers.getInstance().getByPlayer(player1);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        Faction faction1 = fPlayer1.getFaction();
        Faction faction2 = fPlayer2.getFaction();

        boolean sameFaction = !faction1.isWilderness() && !faction2.isWilderness() && faction1.equals(faction2);
        System.out.println("HomingArrowRunnable: " + player1.getName() + " and " + player2.getName() + " in same faction: " + sameFaction);
        return sameFaction;
    }
}