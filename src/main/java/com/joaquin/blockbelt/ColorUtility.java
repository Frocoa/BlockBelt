package com.joaquin.blockbelt;

import org.bukkit.ChatColor;

public class ColorUtility {

    static public String colorFormat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
