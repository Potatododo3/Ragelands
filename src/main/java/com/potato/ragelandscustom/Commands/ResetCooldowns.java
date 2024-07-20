package com.potato.ragelandscustom.Commands;

import com.potato.ragelandscustom.ItemFunctions.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.potato.ragelandscustom.IronManSuit.events.JARVIS.PlayerTracking.glowingPlayers;

public class ResetCooldowns implements CommandExecutor {
    private final CooldownManager cooldownManager = new CooldownManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("ragelands.cooldownreset")) {
                glowingPlayers.remove(player);
                if (cooldownManager.isOnTrackerCooldown(player.getName())) {
                    cooldownManager.setTrackerCooldown(player.getName(), 0);
                }
                if (cooldownManager.isOnFireAbilityCooldown(player.getName())) {
                    cooldownManager.setFireAbilityCooldown(player.getName(), 0);
                }
                if (cooldownManager.isOnFreezeClockCooldown(player.getName())) {
                    cooldownManager.setFreezeClockCooldowns(player.getName(), 0);
                }
                if (CooldownManager.isOnMagicbootsCooldown(player.getName())) {
                    CooldownManager.setMagicbootsCooldown(player.getName(), 0);
                }
            }
        }

        return false;
    }
}
