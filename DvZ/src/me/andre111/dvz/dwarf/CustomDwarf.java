package me.andre111.dvz.dwarf;

import java.util.ArrayList;
import java.util.List;

import me.andre111.dvz.DvZ;
import me.andre111.dvz.Game;
import me.andre111.dvz.StatManager;
import me.andre111.dvz.config.ConfigManager;
import me.andre111.dvz.utils.ItemHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomDwarf {
	private int id;
	private int gameId;
	private String name;
	private String prefix;
	private String suffix;
	private int classItem;
	private int classItemDamage;
	private int classChance;
	private String[] items;
	private String[] crystalItems;
	private String[] effects;
	private double damageBuff;
	private ArrayList<String> disabledDamage;
	private int maxMana;
	private int manaRegen;
	private int startHealth;
	private int startHunger;
	private float startSat;
	
	//spells
	private boolean spellEnabled;
	private int spellTime;
	private int spellItem;
	private String spellName;
	private int spellNeedId;
	private int spellNeedData;
	private int spellNeedCount;
	private String spellNeed;
	private String spellFail;
	private boolean spellInv;
	private int spellExp;
	private String[] spellItems;
	//piston
	private boolean pistonEnabled;
	private List<String> pistonChange;
	//rightclick
	private ArrayList<String> transmuteItems;
	private ArrayList<String> transmuteBreakItems;
	
	//become custom Monster
	public void becomeDwarf(Game game, final Player player) {
		game.setPlayerState(player.getName(), id+Game.dwarfMin);
		game.resetCountdowns(player.getName());
		game.getManaManager().setMaxMana(player.getName(), getMaxMana(), true);
		game.getManaManager().setManaRegen(player.getName(), getManaRegen());
		
		player.sendMessage(ConfigManager.getLanguage().getString("string_have_become","You have become a -0-!").replace("-0-", getName()));
		
		ItemHandler.clearInv(player);
		player.setTotalExperience(0);
		player.setLevel(0);
		player.setExp(0);
		
		player.setHealth(startHealth);
		player.setFoodLevel(startHunger);
		player.setSaturation(startSat);
		
		//Effects
		for(int i=0; i<effects.length; i++) {
			String str = effects[i];
			while(str.startsWith(" ")) {
				str = str.substring(1);
			}
			while(str.endsWith(" ")) {
				str = str.substring(0, str.length()-1);
			}
			
			int id = -1;
			int level = 0;
			int duration = 95000;
			String[] strs = str.split(" ");
			if(strs.length>0) id = Integer.parseInt(strs[0]);
			if(strs.length>1) level = Integer.parseInt(strs[1]);
			if(strs.length>2) duration = Integer.parseInt(strs[2]);
			
			if(id!=-1) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.getById(id), duration, level), true);
			}
		}
		//
		
		PlayerInventory inv = player.getInventory();
		
		//items
		for(int i=0; i<items.length; i++) {
			ItemStack it = ItemHandler.decodeItem(items[i]);
			if(it!=null) {
				//disabled, because it breaks the glow effect
				/*if(i == 0) {
					ItemMeta im = it.getItemMeta();
						im.setDisplayName(getSpellName());
						ArrayList<String> li = new ArrayList<String>();
						li.add(DvZ.getLanguage().getString("string_used_seconds","Can be used every -0- Seconds!").replace("-0-", ""+getSpellTime()));
						if(!getSpellNeed().equals("")) {
							li.add(DvZ.getLanguage().getString("string_need","You need -0- to use this!").replace("-0-", getSpellNeed()));
						}
						im.setLore(li);
					it.setItemMeta(im);
				}*/
				inv.addItem(it);
			}
		}
		//
		
		//crstal chest is no longer a global config option
		/*if(!DvZ.getStaticConfig().getString("crystal_storage","0").equals("0")) {
			ItemStack it = new ItemStack(388, 1);
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(DvZ.getLanguage().getString("string_crystal_storage","Crystal Storage"));
			it.setItemMeta(im);
			inv.addItem(it);*/
			
		//crystalchest items
		//if(DvZ.getStaticConfig().getString("crystal_storage","0").equals("1")) {
			Inventory cinv = game.getCrystalChest(player.getName(), false);

			for(int i=0; i<crystalItems.length; i++) {
				ItemStack cit = ItemHandler.decodeItem(crystalItems[i]);
				if(cit!=null) {
					cinv.addItem(cit);
				}
			}
		//}
		//}
		
		DvZ.updateInventory(player);
		
		//update stats
		Bukkit.getScheduler().runTaskLater(DvZ.instance, new Runnable() {
			public void run() {
				StatManager.show(player);
				StatManager.hide(player);
			}
		}, 2);
	}
	
	public void spell(Game game, Player player) {
		if(game.getCountdown(player.getName(), 1)==0) {
			int id = getSpellNeedId();
			int data = getSpellNeedData();
			int count = getSpellNeedCount();
			
			if(ItemHandler.countItems(player, id, data)>=count) {
				ItemHandler.removeItems(player, id, data, count);
				game.setCountdown(player.getName(), 1, getSpellTime());
				
				player.giveExp(getSpellExp());
				
				World w = player.getWorld();
				Location loc = player.getLocation();
				//Random rand = new Random();
				PlayerInventory inv = player.getInventory();
				
				String[] itemstrings = getSpellItems();
				for(String its : itemstrings) {
					ItemStack it = ItemHandler.decodeItem(its);
					
					if(it!=null) {
						if(isSpellInv()) {
							inv.addItem(it);
						} else {
							w.dropItem(loc, it);
						}
					}
				}
				
				DvZ.updateInventory(player);
			} else {
				if(!getSpellFail().equals(""))
					player.sendMessage(getSpellFail());
			}
		} else {
			player.sendMessage(ConfigManager.getLanguage().getString("string_wait","You have to wait -0- Seconds!").replace("-0-", ""+game.getCountdown(player.getName(), 1)));
		}
	}
	
	public boolean transmuteItemOnBlock(Game game, Player player, ItemStack item, Block block) {
		for(String st : getTransmuteItems()) {
			String[] split = st.split(";");
			
			//player block check
			if(player.getLocation().clone().subtract(0, 1, 0).getBlock().getTypeId()!=Integer.parseInt(split[0])) {
				continue;
			}
			if(player.getLocation().clone().subtract(0, -2, 0).getBlock().getTypeId()!=Integer.parseInt(split[1])) {
				continue;
			}
			
			//is it the right item?
			String[] itemSt = split[2].split(":");
			if(Integer.parseInt(itemSt[0])==item.getTypeId() && Integer.parseInt(itemSt[1])==item.getDurability()) {
				//right block clicked?
				String[] bSt = split[3].split(":");
				if(Integer.parseInt(bSt[0])==block.getTypeId() && (Integer.parseInt(bSt[1])==block.getData() || Integer.parseInt(bSt[1])==-1)) {
					//sound
					String[] sound = split[4].split(":");
					String sId = "-1";
					float volume = 1;
					float pitch = 1;

					sId = sound[0];
					if(sound.length>1) volume = Float.parseFloat(sound[1]);
					if(sound.length>2) pitch = Float.parseFloat(sound[2]);

					if(!sId.equals("-1")) {
						player.getWorld().playSound(player.getLocation(), Sound.valueOf(sId), volume, pitch);
					}

					//drop item
					ItemStack it = ItemHandler.decodeItem(split[5]);
					if(it!=null) {
						player.getWorld().dropItemNaturally(player.getLocation(), it);
					}

					//substract item
					if(item.getAmount()-1<=0)
						item.setTypeId(0);
					else
						item.setAmount(item.getAmount()-1);

					player.setItemInHand(item);
					
					//update the invventory, because it is glitchy
					DvZ.updateInventory(player);

					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean transmuteItemOnBreak(Game game, Player player, Block block) {
		for(String st : getTransmuteBreakItems()) {
			String[] split = st.split(";");
			
			//player block check
			if(player.getLocation().clone().subtract(0, 1, 0).getBlock().getTypeId()!=Integer.parseInt(split[0])) {
				continue;
			}
			if(player.getLocation().clone().subtract(0, -2, 0).getBlock().getTypeId()!=Integer.parseInt(split[1])) {
				continue;
			}
			
			String[] itemSt = split[2].split(":");
			PlayerInventory inv = player.getInventory();
			//right block clicked?
			String[] bSt = split[3].split(":");
			if(Integer.parseInt(bSt[0])==block.getTypeId() && (Integer.parseInt(bSt[1])==block.getData() || Integer.parseInt(bSt[1])==-1))  {
				//whole inventory
				for(int i=0; i<inv.getSize(); i++) {
					ItemStack item  = inv.getItem(i);
					if(item!=null)
					if(Integer.parseInt(itemSt[0])==item.getTypeId() && Integer.parseInt(itemSt[1])==item.getDurability()) {
						//sound
						String[] sound = split[4].split(":");
						String sId = "-1";
						float volume = 1;
						float pitch = 1;
						
						sId = sound[0];
						if(sound.length>1) volume = Float.parseFloat(sound[1]);
						if(sound.length>2) pitch = Float.parseFloat(sound[2]);
						
						if(!sId.equals("-1")) {
							player.getWorld().playSound(player.getLocation(), Sound.valueOf(sId), volume, pitch);
						}
						
						//drop item
						ItemStack it = ItemHandler.decodeItem(split[5]);
						if(it!=null) {
							player.getWorld().dropItemNaturally(player.getLocation(), it);
						}
						
						//substract item
						if(item.getAmount()-1<=0)
							item.setTypeId(0);
						else
							item.setAmount(item.getAmount()-1);
						inv.setItem(i, item);
						
						//update the invventory, because it is glitchy
						DvZ.updateInventory(player);
						
						//break for loop
						i = 10000;
					}
				}
			}
		}
		
		return false;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public int getClassItem() {
		return classItem;
	}
	public void setClassItem(int classItem) {
		this.classItem = classItem;
	}
	public int getClassItemDamage() {
		return classItemDamage;
	}
	public void setClassItemDamage(int classItemDamage) {
		this.classItemDamage = classItemDamage;
	}
	public int getClassChance() {
		return classChance;
	}
	public void setClassChance(int classChance) {
		this.classChance = classChance;
	}
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	public String[] getCrystalItems() {
		return crystalItems;
	}
	public void setCrystalItems(String[] crystalItems) {
		this.crystalItems = crystalItems;
	}
	public String[] getEffects() {
		return effects;
	}
	public void setEffects(String[] effects) {
		this.effects = effects;
	}
	public double getDamageBuff() {
		return damageBuff;
	}
	public void setDamageBuff(double damageBuff) {
		this.damageBuff = damageBuff;
	}
	public void addDisabledDamage(String damage) {
		if(disabledDamage==null) disabledDamage = new ArrayList<String>();
		disabledDamage.add(damage);
	}
	public boolean isDamageDisabled(String damage) {
		if(disabledDamage==null) return false;
		return disabledDamage.contains(damage);
	}
	public int getMaxMana() {
		return maxMana;
	}
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	public int getManaRegen() {
		return manaRegen;
	}
	public void setManaRegen(int manaRegen) {
		this.manaRegen = manaRegen;
	}
	public int getStartHealth() {
		return startHealth;
	}
	public void setStartHealth(int startHealth) {
		this.startHealth = startHealth;
	}
	public int getStartHunger() {
		return startHunger;
	}
	public void setStartHunger(int startHunger) {
		this.startHunger = startHunger;
	}
	public float getStartSat() {
		return startSat;
	}
	public void setStartSat(float startSat) {
		this.startSat = startSat;
	}
	//Spells
	public boolean isSpellEnabled() {
		return spellEnabled;
	}
	public void setSpellEnabled(boolean spellEnabled) {
		this.spellEnabled = spellEnabled;
	}
	public int getSpellTime() {
		return spellTime;
	}
	public void setSpellTime(int spellTime) {
		this.spellTime = spellTime;
	}
	public int getSpellItem() {
		return spellItem;
	}
	public void setSpellItem(int spellItem) {
		this.spellItem = spellItem;
	}
	public String getSpellName() {
		return spellName;
	}
	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}
	public int getSpellNeedId() {
		return spellNeedId;
	}
	public void setSpellNeedId(int spellNeedId) {
		this.spellNeedId = spellNeedId;
	}
	public int getSpellNeedData() {
		return spellNeedData;
	}
	public void setSpellNeedData(int spellNeedData) {
		this.spellNeedData = spellNeedData;
	}
	public int getSpellNeedCount() {
		return spellNeedCount;
	}
	public void setSpellNeedCount(int spellNeedCount) {
		this.spellNeedCount = spellNeedCount;
	}
	public String getSpellNeed() {
		return spellNeed;
	}
	public void setSpellNeed(String spellNeed) {
		this.spellNeed = spellNeed;
	}
	public String getSpellFail() {
		return spellFail;
	}
	public void setSpellFail(String spellFail) {
		this.spellFail = spellFail;
	}
	public boolean isSpellInv() {
		return spellInv;
	}
	public void setSpellInv(boolean spellInv) {
		this.spellInv = spellInv;
	}
	public int getSpellExp() {
		return spellExp;
	}
	public void setSpellExp(int spellExp) {
		this.spellExp = spellExp;
	}
	public String[] getSpellItems() {
		return spellItems;
	}
	public void setSpellItems(String[] spellItems) {
		this.spellItems = spellItems;
	}
	//pistons
	public boolean isPistonEnabled() {
		return pistonEnabled;
	}
	public void setPistonEnabled(boolean pistonEnabled) {
		this.pistonEnabled = pistonEnabled;
	}
	public List<String> getPistonChange() {
		return pistonChange;
	}
	public void setPistonChange(List<String> pistonChange) {
		this.pistonChange = pistonChange;
	}
	//items
	public ArrayList<String> getTransmuteItems() {
		return transmuteItems;
	}
	public void setTransmuteItems(ArrayList<String> transmuteItems) {
		this.transmuteItems = transmuteItems;
	}
	public ArrayList<String> getTransmuteBreakItems() {
		return transmuteBreakItems;
	}
	public void setTransmuteBreakItems(ArrayList<String> transmuteBreakItems) {
		this.transmuteBreakItems = transmuteBreakItems;
	}
}
