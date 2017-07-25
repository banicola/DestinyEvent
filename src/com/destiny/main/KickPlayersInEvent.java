package com.destiny.main;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class KickPlayersInEvent extends JavaPlugin{
	
	public static void kickPlayer(UUID uuid){
		Player p = Bukkit.getServer().getPlayer(uuid);
		p.teleport(Main.getPlugin().oldPlayerLocation.get(uuid));
		Main.getPlugin().playersEvent.get(Main.getPlugin().getPlayerEvent(uuid)).remove(uuid);
		p.sendMessage(ChatColor.RED+"This event has been closed!");
	}

}
