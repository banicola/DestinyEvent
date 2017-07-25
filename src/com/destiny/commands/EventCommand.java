package com.destiny.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.destiny.main.Countdown;
import com.destiny.main.KickPlayersInEvent;
import com.destiny.main.Main;

import net.md_5.bungee.api.ChatColor;

public class EventCommand implements CommandExecutor{

	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("help")){
					if(sender.hasPermission("event.help")){
						sender.sendMessage(ChatColor.BLUE+"======== Events ========");
						sender.sendMessage(ChatColor.GREEN+"/event list: List of events\n/event join [name]: Join an event\n/event quit: Leave an event");
						if(sender.hasPermission("event.create")||sender.hasPermission("event.stop")){
							sender.sendMessage(ChatColor.LIGHT_PURPLE+"/event create [name] {time}: Create a new event (admin)\n/event stop [name]: Stop and remove an event (admin)\n/event remove [game]: Remove an event for ever (admin)");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					} 
				} else if(args[0].equalsIgnoreCase("list")){
					if(sender.hasPermission("event.list")){
						if(!Main.getPlugin().eventsList.isEmpty()){
							String eventsList = "";
							for(String event : Main.getPlugin().eventsList){
								if(eventsList.isEmpty()){
									eventsList = eventsList.replace(eventsList, event);
								} else {
									eventsList = eventsList.replace(eventsList, eventsList+", "+event);
								}
							}
							sender.sendMessage(ChatColor.BLUE+"List of available events:\n"+ChatColor.GREEN+eventsList);
						} else {
							sender.sendMessage(ChatColor.RED+"Sorry, there are not actives events!");
						}
						
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					} 
					
				} else if(args[0].equalsIgnoreCase("join")){
					if(sender.hasPermission("event.join")){
						if(args.length > 1){
							if(Main.getPlugin().getPlayerEvent(((Player) sender).getUniqueId()) == null){
								if(Main.getPlugin().eventsList.contains(args[1])){
									if(Main.getPlugin().countdownEvent.get(args[1]) == 0){
										Main.getPlugin().oldPlayerLocation.put(((Player) sender).getUniqueId(),((Player) sender).getLocation());
										((Player) sender).teleport(Main.getPlugin().eventLocation.get(args[1]));
										Main.getPlugin().playersEvent.get(args[1]).add(((Player) sender).getUniqueId());
										sender.sendMessage(ChatColor.GREEN+"You joined the event "+ChatColor.LIGHT_PURPLE+args[1]);
									} else if(Main.getPlugin().countdownEvent.get(args[1]) < 0){
										sender.sendMessage(ChatColor.RED+"Sorry, this event is finished!");							
									} else {
										sender.sendMessage(ChatColor.RED+"You would be able to join this event in "+ChatColor.WHITE+Main.getPlugin().countdownEvent.get(args[1])+ChatColor.RED+" seconds!");
									}									
								} else {
									sender.sendMessage(ChatColor.RED+"This event does not exist! Use: "+ChatColor.WHITE+"/event list");
								}
							} else {
								sender.sendMessage(ChatColor.RED+"Leave the previous event first! Use: "+ChatColor.WHITE+"/event quit");
							}
							
						} else {
							sender.sendMessage(ChatColor.RED+"Wrong usage: /event join [name], where [name] is the event");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					}
					
				} else if(args[0].equalsIgnoreCase("quit")){
					if(sender.hasPermission("event.quit")){
						if(Main.getPlugin().getPlayerEvent(((Player) sender).getUniqueId()) != null){
							Main.getPlugin().playersEvent.get(Main.getPlugin().getPlayerEvent(((Player) sender).getUniqueId())).remove(((Player) sender).getUniqueId());
							((Player) sender).teleport(Main.getPlugin().oldPlayerLocation.get(((Player) sender).getUniqueId()));
							sender.sendMessage(ChatColor.GREEN+"You succesfully left the event!");
						} else {
							sender.sendMessage(ChatColor.RED+"You are not currently in an event!");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					}
					
				} else if(args[0].equalsIgnoreCase("create")){
					if(sender.hasPermission("event.create")){
						if(args.length > 1 && args.length <= 3){
							if(!Main.getPlugin().eventsList.contains(args[1])){
								Location eventLocation = ((Player) sender).getLocation();
								Main.getPlugin().eventLocation.put(args[1], eventLocation);
								Main.getPlugin().eventsList.add(args[1]);
								Main.getPlugin().countdownEvent.put(args[1], 0);
								List<UUID> uuidPlayers = new ArrayList<UUID>();
								Main.getPlugin().playersEvent.put(args[1], uuidPlayers);
								if(args.length == 3 && Main.isInteger(args[2])){
									Integer i = Integer.parseInt(args[2]);
									
									Main.getPlugin().countdownEvent.put(args[1], i*60);
									Bukkit.broadcastMessage(ChatColor.GREEN+"The event "+ChatColor.LIGHT_PURPLE+args[1]+ChatColor.GREEN+" starts in "+ChatColor.WHITE+args[2]+ChatColor.GREEN+" minutes!");
									Countdown.CountdownStart(i, args[1]);
									return true;
								}
								Bukkit.broadcastMessage(ChatColor.GREEN+"The event "+ChatColor.LIGHT_PURPLE+args[1]+ChatColor.GREEN+" has just started! Use: "+ChatColor.WHITE+"/event join "+args[1]);
								
							} else {
								sender.sendMessage(ChatColor.RED+"This event already exist!");
							}
						} else {
							sender.sendMessage(ChatColor.RED+"Wrong usage: /event create [name] {time}, where [name] is the event and time the countdown(minutes) before it starts");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					}
					
				} else if(args[0].equalsIgnoreCase("stop")){
					if(sender.hasPermission("event.stop")){
						if(args.length == 2){
							if(Main.getPlugin().eventsList.contains(args[1])){
								if(Main.getPlugin().countdownEvent.get(args[1]) > 0){
									Bukkit.broadcastMessage(ChatColor.RED+"The event "+ChatColor.LIGHT_PURPLE+args[1]+ChatColor.RED+" has been canceled!");
								} else {
									Bukkit.broadcastMessage(ChatColor.RED+"The event "+args[1]+" is now finished! You can use "+ChatColor.WHITE+"/event quit");
								}
								Main.getPlugin().countdownEvent.put(args[1], -1);
								
							} else {
								sender.sendMessage(ChatColor.RED+"This event does not exist! Use: "+ChatColor.WHITE+"/event list");
							}
						} else {
							sender.sendMessage(ChatColor.RED+"Wrong usage: /event stop [name], where [name] is the event");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					}
					
				} else if(args[0].equalsIgnoreCase("remove")){
					if(sender.hasPermission("event.remove")){
						if(args.length == 2){
							if(Main.getPlugin().eventsList.contains(args[1])){
								if(Main.getPlugin().countdownEvent.get(args[1]) >= 0){
									Bukkit.broadcastMessage(ChatColor.RED+"The event "+ChatColor.LIGHT_PURPLE+args[1]+ChatColor.RED+" has been canceled!");
								}
								Main.getPlugin().countdownEvent.put(args[1], -1);
								Main.getPlugin().eventsList.remove(args[1]);
								Main.getPlugin().eventLocation.remove(args[1]);
								
								if(!Main.getPlugin().playersEvent.get(args[1]).isEmpty()){
									List<UUID> uuidList = new ArrayList<UUID>();
									for(UUID uuid : Main.getPlugin().playersEvent.get(args[1])){
										uuidList.add(uuid);
									}
									for(UUID uuidPlayer : uuidList){
										if(uuidPlayer!= null){
											KickPlayersInEvent.kickPlayer(uuidPlayer);
										}										
									}
								}
								Main.getPlugin().playersEvent.remove(args[1]);
								sender.sendMessage(ChatColor.GREEN+"The event "+ChatColor.LIGHT_PURPLE+args[1]+ChatColor.GREEN+" has been removed!");
							} else {
								sender.sendMessage(ChatColor.RED+"This event does not exist! Use: "+ChatColor.WHITE+"/event list");
							}
						} else {
							sender.sendMessage(ChatColor.RED+"Wrong usage: /event remove [name], where [name] is the event");
						}
					} else {
						sender.sendMessage(ChatColor.RED+"You need permissions for this command!");
					}
					
				} else {
					sender.sendMessage(ChatColor.RED+"Wrong command: Use "+ChatColor.WHITE+"/event help"+ChatColor.RED+" for more informations!");
				}
				
			} else {
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED+"Only players can use this command!");
		}
		return true;
	}
}
