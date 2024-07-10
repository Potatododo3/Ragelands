package com.potato.rlcustomitems.IronManSuit;

import com.potato.rlcustomitems.Main;
import org.bukkit.Bukkit;

public class Delay {
    public static void until(long time, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), runnable, time);
    }
}
