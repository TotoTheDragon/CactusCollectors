package com.developerdragon.cactuscollectors.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static void sendPluginMessage(Player player, String string){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lImmortalCollectors &8» &f" + string));
    }
}
