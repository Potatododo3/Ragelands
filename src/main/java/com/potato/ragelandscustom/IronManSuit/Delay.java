package com.potato.ragelandscustom.IronManSuit;

import com.potato.ragelandscustom.Main;
import org.bukkit.Bukkit;

public class Delay {
    public static void until(long time, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), runnable, time);
    }
}
