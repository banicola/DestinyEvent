package com.destiny.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.destiny.commands.EventCommand;

public class Main extends JavaPlugin{
	
	public List<String> eventsList = new ArrayList<String>();
	
	public HashMap<UUID, Location> oldPlayerLocation = new HashMap<UUID, Location>();
	public HashMap<String, Location> eventLocation = new HashMap<String, Location>();
	public HashMap<String, Integer> countdownEvent = new HashMap<String, Integer>();
	public HashMap<String, List<UUID>> playersEvent = new HashMap<String, List<UUID>>();
	
	public File eventsFile;
	public FileConfiguration eventsConfig;
	
	public void onEnable(){		
		CommandExecutor eventCommandExecutor = new EventCommand();
    	getCommand("evenements").setExecutor(eventCommandExecutor);
	}
	public void onDisable(){
		
	}
	
	public static Main getPlugin() {
	    return JavaPlugin.getPlugin(Main.class);
	}
	public String getPlayerEvent(UUID uuid){
		for(String event : eventsList){
			if(playersEvent.get(event).contains(uuid)){
				return event;
			}
		}
		return null;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
