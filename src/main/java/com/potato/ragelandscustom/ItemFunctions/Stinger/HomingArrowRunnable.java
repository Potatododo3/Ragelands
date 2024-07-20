package com.potato.ragelandscustom.ItemFunctions.Stinger;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.potato.ragelandscustom.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class HomingArrowRunnable extends BukkitRunnable {
    private final Entity arrow;
    private Entity nearestPlayer;
    private Main main;
    public HomingArrowRunnable(Entity arrow, Main main) {
        this.arrow = arrow;
        this.main = main;
        radius = main.getConfig().getDouble("StingerRadius");
    }

    @Override
    public void run() {
        Long timeElapsed = 0L;
        timeElapsed = timeElapsed + 5;
        if (timeElapsed >= 205) {
            arrow.remove();
            this.cancel();
        }
        if (arrow.isDead() || (nearestPlayer != null && nearestPlayer.isDead())) {
            cancel();
            return;
        }
        nearestPlayer = findNearestPlayer((Projectile) arrow);
        if (nearestPlayer != null) {
            Player player = (Player) nearestPlayer;
            if (player.isGliding() || player.isFlying()) {
                Projectile projectile = (Projectile) arrow;
                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player) projectile.getShooter();
                    if (shooter == player) {return;}
                    if (!isInSameFaction(shooter, player)) {
                        Vector newVector = nearestPlayer.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize().multiply(1.9);
                        arrow.setVelocity(newVector);
                    }
                }
            }
        }
    }
    double radius;
    private Player findNearestPlayer(Projectile projectile) {
        double minDistanceSquared = radius; // 8 blocks squared
        Player nearestPlayer = null;
        Collection<Player> nearbyPlayers = projectile.getWorld().getPlayers();

        for (Player player : nearbyPlayers) {
            Player shooter = (Player) projectile.getShooter();
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(shooter);
            FPlayer nearbyPlayer = FPlayers.getInstance().getByPlayer(player);
            Faction shooterFPlayerFaction = fPlayer.getFaction();
            Faction nearbyPlayerFaction = nearbyPlayer.getFaction();
            // Exclude the shooter (usually the player)
            if (!player.equals(projectile.getShooter()) && shooterFPlayerFaction != nearbyPlayerFaction) {
                double distanceSquared = player.getLocation().distanceSquared(projectile.getLocation());
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    nearestPlayer = player;
                }
            }
        }

        return nearestPlayer;
    }

    private boolean isInSameFaction(Player player1, Player player2) {
        FPlayer fPlayer1 = FPlayers.getInstance().getByPlayer(player1);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        Faction faction1 = fPlayer1.getFaction();
        Faction faction2 = fPlayer2.getFaction();

        boolean sameFaction = !faction1.isWilderness() && !faction2.isWilderness() && faction1.equals(faction2);
        return sameFaction;
    }
}