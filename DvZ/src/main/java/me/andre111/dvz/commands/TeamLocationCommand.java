package me.andre111.dvz.commands;

import java.io.File;

import me.andre111.dvz.DvZ;
import me.andre111.dvz.Game;
import me.andre111.dvz.config.ConfigManager;
import me.andre111.dvz.config.DVZFileConfiguration;
import me.andre111.dvz.utils.Slapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamLocationCommand extends DvZCommand {
	//Set the Dwarf Spawnpoint
	public TeamLocationCommand(String name) {
		super(name);
	}

	@Override
	public boolean handle(int gameID, CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			DvZ.sendPlayerMessageFormated(sender, "Y U NO PLAYER??!111");
			return true;
		}
		Player player = (Player)sender;
		if(!sender.hasPermission("dvz.setspawn")) {
			DvZ.sendPlayerMessageFormated(sender, "You don't have the Permission to do that!");
			return false;
		}
		
		//TODO - add message for what went wrong
		if(args.length<2) {
			return false;
		}
		String team = args[0];
		String loc = args[1].toLowerCase();
		Location location = player.getLocation();
		boolean found = false;
		
		//DvZ.sendPlayerMessageFormated(player, ConfigManager.getLanguage().getString("string_setspawn_dwarf","Set Dwarf Spawn to your current Location!"));
		if(loc.equals("spawn")) {
			found = true;
		} else if(loc.equals("monument")) {
			found = true;
			DvZ.instance.getDummy().createMonument(location, true);
		}
		
		if(!found) {
			return false;
		}
		
		String path = Bukkit.getServer().getWorldContainer().getPath()+"/"+player.getWorld().getName()+"/dvz/locations.yml";
		DVZFileConfiguration save = DVZFileConfiguration.loadConfiguration(new File(path));
		
		save.set(team+"."+loc+".x", location.getX());
		save.set(team+"."+loc+".y", location.getY());
		save.set(team+"."+loc+".z", location.getZ());
		save.set(team+"."+loc+".yaw", location.getYaw());
		save.set(team+"."+loc+".pitch", location.getPitch());
		
		//TODO - set location in active game
		if(gameID!=-1) {
			Game game = DvZ.instance.getGame(gameID);
		}
		
		return true;
	}
}
