package pl.inder00.dghc.guilds.utils;

import org.bukkit.ChatColor;

public class Util {

    public static String fixColor(String in){
        return ChatColor.translateAlternateColorCodes('&', in);
    }

}
