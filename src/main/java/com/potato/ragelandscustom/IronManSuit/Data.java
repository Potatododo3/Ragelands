package com.potato.ragelandscustom.IronManSuit;

import org.bukkit.entity.Player;

import java.util.*;

public class Data {

    public static ArrayList<Player> Suit = new ArrayList<>();
    public static ArrayList<Player> buildingSuit = new ArrayList<>();
    public static ArrayList<Player> isLowHealth = new ArrayList<>();
    public static ArrayList<Player> isOnFire = new ArrayList<>();
    public static ArrayList<Player> isPoisoned = new ArrayList<>();
    public static HashMap<Player, String> suitAssigned = new HashMap<>();
    public static Map<Player, Float> playerSpeed = new HashMap<>();
    public static Set<Player> isHovering = new HashSet<>();
    public static ArrayList<Player> isGliding = new ArrayList<>();

}