package me.andre111.dvz.disguise;

import me.andre111.dvz.DvZ;
import me.andre111.dvz.Game;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.events.UndisguiseEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DSystem_LibsDisguises implements DSystem, Listener {
	private int nextID = Integer.MIN_VALUE;
	
	@Override
	public void initListeners(DvZ plugin) {
		DisguiseAPI.setHearSelfDisguise(true);
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void disguiseP(Player player, String disguise) {
		DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.getType(EntityType.fromName(disguise)), true, true));
	}

	@Override
	public void undisguiseP(Player player) {
		DisguiseAPI.undisguiseToAll(player);
	}

	@Override
	public void redisguiseP(Player player) {
		Disguise dis = DisguiseAPI.getDisguise(player);
		DisguiseAPI.undisguiseToAll(player);
		DisguiseAPI.disguiseToAll(player, dis);
	}

	@Override
	public int newEntityID() {
		return nextID++;
	}
	
	//TODO - maybe needs rightclick detection

	//TODO - check if this is really needed
	@EventHandler
	public void onPlayerUndisguise(UndisguiseEvent event) {
		Entity e = event.getEntity();
		if(!(e instanceof Player)) return;
		
		Player p = (Player) event.getEntity();
		Game game = DvZ.instance.getPlayerGame(p.getName());
		if(game!=null) {
			if(game.getState()>1)
				if(game.isMonster(p.getName()) || game.getPlayerState(p.getName())>Game.dragonMin) {
					event.setCancelled(true);
				}
		}
	}
}
