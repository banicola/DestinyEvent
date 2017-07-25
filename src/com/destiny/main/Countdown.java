package com.destiny.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Countdown extends JavaPlugin{
	
	static int time;
	static int TaskID;
	String game;
	
	public static void CountdownStart(int amount, String event) {
        time = amount*60;
        
        TaskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("DestinyEvent"), new Runnable() {
            @Override
            public void run() {
            	if(Main.getPlugin().countdownEvent.get(event) == -1){
            		Bukkit.getServer().getScheduler().cancelTask(TaskID);
            	}
            	if(time == 30){
            		Bukkit.broadcastMessage(ChatColor.GREEN+"The event "+ChatColor.LIGHT_PURPLE+event+ChatColor.GREEN+" will start in "+ChatColor.WHITE+"30"+ChatColor.GREEN+" seconds!");
            	}
            	if(time == 0){
            		Bukkit.broadcastMessage(ChatColor.GREEN+"The event "+ChatColor.LIGHT_PURPLE+event+ChatColor.GREEN+" has just started! Use: "+ChatColor.WHITE+"/event join "+event);
            		Bukkit.getServer().getScheduler().cancelTask(TaskID);
            		return;
            	}
            	Main.getPlugin().countdownEvent.put(event, Main.getPlugin().countdownEvent.get(event)-1);
            	time--;
            	
            }
        }, 0L, 20L);
    }
}
