package fr.bibiobscur.skyblock;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.bibiobscur.skyblock.group.Group;

public class SkyDatas {
	private final Plugin plugin;
	private HashMap<String, Island> playerIslands = new HashMap<String, Island>();
	private Stack<Island> orphaned = new Stack<>();
	private HashSet<Group> groupList = new HashSet<Group>();
	private ArrayList<Entry<String, Integer>> topList = new ArrayList<Entry<String, Integer>>();
	private Island lastIsland;
	private HashMap<String, Home> playerHomes = new HashMap<String, Home>();
	private Logger logger = Logger.getLogger("Minecraft");
	
	public SkyDatas(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	public void load(String directory) {
		try {
        	logger.info("[" + plugin.getPluginName() + "] Chargement des donnees.");
        	
        	if(new File(directory + "lastIsland.bin").exists())
        		lastIsland = (Island)SLAPI.load(directory + "lastIsland.bin");
        	if(null == lastIsland) {
            	lastIsland = new Island(); 
            	lastIsland.setX(0);
            	lastIsland.setZ(0);
        	}
        	
        	if(new File(directory + "playerIslands.bin").exists()){
        		HashMap<String,Island> load = (HashMap<String,Island>)SLAPI.load(directory + "playerIslands.bin");
        		logger.info("[" + plugin.getPluginName() + "] Chargement des iles.");
    			if(null != load) 
    				playerIslands = load;
        	} else
        		new File(directory + "playerIslands.bin");
        	
        	if(new File(directory + "orphanedIslands.bin").exists()) {
				Stack<Island> load = (Stack<Island>)SLAPI.load(directory + "orphanedIslands.bin");
				logger.info("[" + plugin.getPluginName() + "] Chargement des iles supprimees.");
				if(null != load)
					orphaned = load;
			}else
        		new File(directory + "orphanedIslands.bin");
        	
        	if(new File(directory + "playerHomes.bin").exists()) {
				HashMap<String,Home> load = (HashMap<String,Home>)SLAPI.load(directory + "playerHomes.bin");
				logger.info("[" + plugin.getPluginName() + "] Chargement des homes.");
        		if(null != load)
        			playerHomes = load;
        	} else
        		new File(directory + "playerHomes.bin");
        	
        	if(new File(directory + "groupList.bin").exists()) {
				HashSet<Group> load = (HashSet<Group>)SLAPI.load(directory + "groupList.bin");
				logger.info("[" + plugin.getPluginName() + "] Chargement des groupes.");
        		if(null != load)
        			groupList = load;
        	} else
        		new File(directory + "groupList.bin");
        	
        } catch (Exception e) {
        	logger.severe("[" + plugin.getPluginName() + "] Echec du chargement des donnees.");
			e.printStackTrace();
        }
	}

	public void save(String directory) {
		try {
			logger.info("[" + plugin.getPluginName() + "] Sauvegarde des donnees du skyblock...");
			SLAPI.save(playerIslands, directory + "playerIslands.bin");
			SLAPI.save(lastIsland, directory + "lastIsland.bin");
			SLAPI.save(orphaned, directory + "orphanedIslands.bin");
			SLAPI.save(playerHomes, directory + "playerHomes.bin");
			SLAPI.save(groupList, directory + "groupList.bin");
			logger.info("[" + plugin.getPluginName() + "] Sauvegarde terminee.");
		} catch (Exception e) {
			logger.severe("[" + plugin.getPluginName() + "] La sauvegarde des donnees a echoue.");
			e.printStackTrace();
		}
	}

	
//---------------------------------------------------------------------------------------------------------
	//M�thodes sur les �les
	public void addPlayerIsland(String player, Island newIsland) {
		playerIslands.put(player, newIsland);
	}
	
	public void deleteIsland(String playerName,World world) {
		if(hasIsland(playerName) && isLeader(playerName)) {
			Island island = getPlayerIsland(playerName);
			int length = plugin.getISLAND_SPACING();
			
			for(int x = island.getX() - length/2; x <= island.getX() + length/2; x++)
				for(int y = 0; y < world.getMaxHeight(); y++)
					for(int z = island.getZ() - length/2; z <= island.getZ() + length/2; z++) {
						Block block = world.getBlockAt(x,y,z);
						if(block.getType() != Material.AIR)
							block.setType(Material.AIR);
					}

			island.getChallenges().clear();
			orphaned.push(island);
			playerIslands.remove(playerName);
			
			if(hasGroup(playerName))
				removeGroup(playerName);
			
			playerHomes.remove(playerName);
			
			if(plugin.getHellDatas().hasIsland(playerName))
				plugin.getHellDatas().deleteIsland(playerName, plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));

			if(plugin.isConnected(playerName)) plugin.getServer().getPlayer(playerName).setLevel(0);
		}
	}
	
	public boolean hasIsland(String playername) {
		if(hasGroup(playername))
			return true;
		else if (playerIslands.containsKey(playername))
    		return true;
    	else if (playerIslands.containsKey(playername.toLowerCase()))
    		return true;
    	else
    		return false;
    }
	
    public boolean isOnIsland(Player player) {
    	if(hasIsland(player.getName()) && player.getLocation().getWorld().getName().equals(plugin.getworldname())) {

    		World world = plugin.getServer().getWorld(plugin.getworldname());
    		Island island;

    		if(hasGroup(player.getName()))
    			island = getPlayerIsland(getGroup(player.getName()).getLeader());
    		else
    			island = getPlayerIsland(player.getName());
    		
    		int x = player.getLocation().getBlockX();
			int y = player.getLocation().getBlockY();
			int z = player.getLocation().getBlockZ();

    		if(x > island.getX() - plugin.getISLAND_SPACING()/2 && x < island.getX() + plugin.getISLAND_SPACING()/2
    				&& z > island.getZ() - plugin.getISLAND_SPACING()/2 && z < island.getZ() + plugin.getISLAND_SPACING()/2
    				&& y > 5 && y < world.getMaxHeight() - 5) {
    			return true;
    		}
    		return false;
    	}
    	return plugin.getHellDatas().isOnIsland(player);
    }
    
    public boolean isOnIsland(Player player, Location location) {
    	if(hasIsland(player.getName()) && location.getWorld().getName().equals(plugin.getworldname())) {
    		
    		World world = plugin.getServer().getWorld(plugin.getworldname());
    		Island island;
    		
    		if(hasGroup(player.getName()))
    			island = getPlayerIsland(getGroup(player.getName()).getLeader());
    		else
    			island = getPlayerIsland(player.getName());
    		
    		int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
    		
    		if(x > island.getX() - plugin.getISLAND_SPACING()/2 && x < island.getX() + plugin.getISLAND_SPACING()/2
    				&& z > island.getZ() - plugin.getISLAND_SPACING()/2 && z < island.getZ() + plugin.getISLAND_SPACING()/2
    				&& y > 5 && y < world.getMaxHeight() - 5) {
    			return true;
    		}
    		return false;
    	}
    	return plugin.getHellDatas().isOnIsland(player, location);
    }
    
    public Island getPlayerIsland(String playerName) {
    	if(hasIsland(playerName)) {
    		if(hasGroup(playerName) && !isLeader(playerName)) 
    			return getPlayerIsland(getGroup(playerName).getLeader());
    		if (playerIslands.containsKey(playerName))
    			return playerIslands.get(playerName);
    		else
    			return playerIslands.get(playerName.toLowerCase());
    	}
    	Island spawn = new Island();
    	spawn.setX(0);
    	spawn.setZ(0);
    	return spawn;
    }
    
    public String getHostHere(Location location) {
    	int x = location.getBlockX();
    	int z = location.getBlockZ();
    	Island island;
    	
    	for (Entry<String, Island> entry : playerIslands.entrySet()) {
    		island = (Island) entry.getValue();
    		if(x > island.getX()-plugin.getISLAND_SPACING()/2 && 
    				x < island.getX()+plugin.getISLAND_SPACING()/2 &&
    				z > island.getZ()-plugin.getISLAND_SPACING()/2 &&
    				z < island.getZ()+plugin.getISLAND_SPACING()/2)
    			return (String) entry.getKey();
    	}
    	
    	return plugin.getHellDatas().getHostHere(location);
    }
    
    public Island getLastIsland() {
    	return lastIsland;
    }
    
    public void setLastIsland(Island island) { 
    	lastIsland = island;
    }
    
    //Used ?
    public HashMap<String, Island> getPlayers() {
    	return playerIslands;
    }
    
	public boolean hasOrphanedIsland() {
    	return !orphaned.empty();
    }

	public Island getOrphanedIsland() {
    	if(hasOrphanedIsland()) {
    		return orphaned.pop();
    	}
    	
    	Island spawn = new Island();
    	spawn.setX(0);
    	spawn.setZ(0);
    	
    	return spawn;
    }

	public void clearOrphanedIsland() {
		orphaned.clear();
	}
	
	public void addOrphanedIsland(int x, int z) {
		Island island = new Island(x, z);
		orphaned.push(island);
	}
	
	//Dans Island.java
	public void defineLevel() {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run(){
				logger.info("Calcul du niveau des iles...");
				Island island;
			    for(Entry<String, Island> entry : playerIslands.entrySet()) {
			    	island = (Island) entry.getValue();
			    	if(island != null) {
			    		if(plugin.getHellDatas().hasIsland(entry.getKey()))
			    			island.defineLevel(plugin.getServer().getWorld(plugin.getworldname()), plugin.getISLAND_SPACING(), plugin.getHellDatas().getPlayerIsland(entry.getKey()).defineHellLevel(plugin.getServer().getWorld(plugin.getworldname()), plugin.getISLAND_SPACING()));
			    		else
			    			island.defineLevel(plugin.getServer().getWorld(plugin.getworldname()), plugin.getISLAND_SPACING(), 0);
			    	}
			    }
			    logger.info("Niveau des iles mis a jour.");
				Bukkit.broadcastMessage(ChatColor.GOLD + " -- " + ChatColor.RED + "Niveaux des �les mis � jour." + ChatColor.GOLD + " -- ");
			    createTopList();
			}
		});
	}
	
	public void createTopList() {
		logger.info("Creation du classement...");
		TreeMap<Integer, String> topIslands = new TreeMap<Integer, String>();
		Island island;
		
		for(Entry<String, Island> entry : playerIslands.entrySet()) {
			island = (Island) entry.getValue();
			if(island != null && island.getLevel() > 0)
				topIslands.put(island.getLevel(), entry.getKey());
		}

		ArrayList<Entry<String, Integer>> shortList = new ArrayList<Entry<String, Integer>>(topIslands.size());

		for(Entry<Integer, String> entry : topIslands.descendingMap().entrySet())
		shortList.add(new AbstractMap.SimpleEntry<String, Integer>(entry.getValue(), entry.getKey()));
		
		this.topList = shortList;
		logger.info("Creation du classement termine.");
	}
	
	public ArrayList<Entry<String, Integer>> getTopList() {
		return topList;
	}
	    
	
//---------------------------------------------------------------------------------------------------------
	//M�thodes sur les Homes
	//Used ?
    public HashMap<String, Home> getHomes() {
    	return playerHomes;
    }
    
    public Home getPlayerHome(String playerName) {
    	if(hasIsland(playerName)) {
    		if (playerHomes.containsKey(playerName))
    			return playerHomes.get(playerName);
    		else
    			return playerHomes.get(playerName.toLowerCase());
    	} 
    	
    	Home home = new Home();
    	home.setX(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation().getBlockX());
    	home.setY(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation().getBlockY());
    	home.setZ(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation().getBlockZ());
    	return home;
    }
	
    public boolean hasHome(String playername) {
    	if (playerHomes.containsKey(playername))
    		return true;
    	else if (playerHomes.containsKey(playername.toLowerCase()))
    		return true;
    	else
    		return false;
    }
    
    public boolean createHome(Player player, Location location) {
    	
    	if(hasIsland(player.getName()))
    	{
    		if(location.getWorld().getName().equals(plugin.getworldname()))
    		{
		    	if(isOnIsland(player))
		    	{
		    		if(hasHome(player.getName()))
		    			playerHomes.remove(player.getName());
		    		
		    		Home home = new Home();
		    		home.setX(location.getBlockX());
		    		home.setY(location.getBlockY());
		    		home.setZ(location.getBlockZ());
		    		home.setDirection(location.getDirection().serialize());
		    		playerHomes.put(player.getName(), home);
		    		
		    		return true;
		    	}
	    		player.sendMessage(ChatColor.RED + "Vous devez �tre sur votre �le pour cr�er un home.");
	    		return false;
    		}
    		player.sendMessage(ChatColor.RED + "Vous devez �tre sur la map Skyblock pour cr�er un home.");
    		return false;
    	}
    	
    	player.sendMessage(ChatColor.RED + "Vous devez poss�der une �le pour cr�er un home. Pour cr�er une �le, tapez /is create.");
    	return false;
    }
	
    
//---------------------------------------------------------------------------------------------------------
    //M�thodes sur la t�l�portation
    public void teleportIsland(Player player) {
    	
		Island island = getPlayerIsland(player.getName());
		World world = plugin.getServer().getWorld(plugin.getworldname());
		int h = plugin.getISLANDS_Y();
		
		while(world.getBlockAt(island.getX(), h, island.getZ()).getType() != Material.AIR) {
				h++;
		}
		h++;
		world.loadChunk(island.getX(), island.getZ());
		player.teleport(new Location(world, island.getX(), h, island.getZ()));	
	}
    
    public void teleportIsland(Player player, String playerdest) {
    	
		Island island = getPlayerIsland(playerdest);
		World world = plugin.getServer().getWorld(plugin.getworldname());
		int h = plugin.getISLANDS_Y();
		
		while(world.getBlockAt(island.getX(), h, island.getZ()).getType() != Material.AIR) {
				h++;
		}
		h++;
		world.loadChunk(island.getX(), island.getZ());
		player.teleport(new Location(world, island.getX(), h, island.getZ()));	
	}
    
    public void teleportHome(Player player) {
    	
    	Home home = getPlayerHome(player.getName());
    	World world = plugin.getServer().getWorld(plugin.getworldname());
    	Location location = new Location(world, home.getX(), home.getY(), home.getZ());
		location.setDirection(Vector.deserialize(home.getDirection()));
    	int h = location.getBlockY();
    	
	    while(world.getBlockAt(location.getBlockX(), h, location.getBlockZ()).getType() != Material.AIR) {
				h++;
		}
	    h++;
	    
		world.loadChunk(location.getBlockX(), location.getBlockZ());
		location.setY(h);
		player.teleport(location);
	}



//---------------------------------------------------------------------------------------------------------
    //M�thodes sur les groupes
	public boolean hasGroup(String playername) {
		Group group;
		Iterator<Group> it = groupList.iterator();
		while(it.hasNext())
		{
			group = it.next();
			if(group != null) {
				Iterator<String> itg = group.getMembers().iterator();
				while(itg.hasNext()) {
					if(itg.next().equalsIgnoreCase(playername)) return true;
				}
			}
			/*if(group != null) {
				if(group.getMembers().contains(playername)) return true;
			}*/
		}
		return false;
	}
	
	public boolean isLeader(String playername) {
		if(!hasIsland(playername))
			return false;
		else if(!hasGroup(playername))
			return true;
		else if(hasGroup(playername) && !getGroup(playername).getLeader().equalsIgnoreCase(playername))
			return false;
		else if(hasGroup(playername) && getGroup(playername).getLeader().equalsIgnoreCase(playername))
			return true;
		else
			return false;
	}
	
	public Group getGroup(String playername) {
		if(!hasGroup(playername)) return null;
		Group group;
		Iterator<Group> it = groupList.iterator();
		while(it.hasNext()) {
			group = it.next();
			if(group != null);
				if(group.getMembers().contains(playername)) return group;
		}
		return null;
	}
	
	public void addPlayerToGroup(String leader, String playername) {
		if(!hasGroup(playername)) {
			if(hasGroup(leader))
				getGroup(leader).getMembers().add(playername);
			else  {
				Group group = new Group();
				group.setLeader(leader);
				group.getMembers().add(leader);
				group.getMembers().add(playername);
				groupList.add(group);
			}
			plugin.getServer().getPlayer(playername).getInventory().clear();
			teleportIsland(plugin.getServer().getPlayer(playername));
			createHome(plugin.getServer().getPlayer(playername), new Location(plugin.getServer().getWorld(plugin.getworldname()), getPlayerIsland(leader).getX(), plugin.getISLANDS_Y(), getPlayerIsland(leader).getZ()));
			if(plugin.isConnected(leader)) plugin.getServer().getPlayer(leader).sendMessage(ChatColor.GREEN + "Le joueur " + ChatColor.BLUE + playername + ChatColor.GREEN + " a �t� ajout� � votre groupe.");
			if(plugin.isConnected(playername)) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.GREEN + "Vous avez rejoint le groupe de " + ChatColor.BLUE + leader + ChatColor.GREEN + " !");
		}
	}
	
	public void removePlayerFromGroup(String leader, String playername) {
		if(hasGroup(leader) && leader != playername) {
			Group group = getGroup(leader);
			group.getMembers().remove(playername);
			playerHomes.remove(playername);
			group.getMembers().remove(leader);
			if(group.getMembers().isEmpty())
				removeGroup(leader);
			else
				group.getMembers().add(leader);
			if(plugin.isConnected(leader)) plugin.getServer().getPlayer(leader).sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername + ChatColor.RED + " a �t� exclu de votre groupe.");
			if(plugin.isConnected(playername)) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Vous avez �t� exclu du groupe par " + ChatColor.BLUE + leader + ChatColor.RED + ".");
		}
	}
	
	public void quitGroup(String playername) {
		if(hasGroup(playername) && !isLeader(playername)) {
			Group group = getGroup(playername);
			group.getMembers().remove(playername);
			group.getMembers().remove(group.getLeader());
			if(group.getMembers().isEmpty())
				removeGroup(group.getLeader());
			else
				group.getMembers().add(group.getLeader());
			playerHomes.remove(playername);
			if(plugin.isConnected(playername)) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Vous avez quitt� le groupe de " + ChatColor.BLUE + group.getLeader() + ChatColor.RED + ".");
		}
	}
	
	public void removeGroup(String leader) {
		if(hasGroup(leader)) {
			Group group = getGroup(leader);
			group.getMembers().remove(leader);
			Iterator<String> it = group.getMembers().iterator();
		    while(it.hasNext())
		    	playerHomes.remove(it.next());
			groupList.remove(getGroup(leader));
			if(plugin.isConnected(leader)) plugin.getServer().getPlayer(leader).sendMessage(ChatColor.RED + "Votre groupe a �t� dissout");
		}
	}
	
	public void changeLeader(String oldleader, String newleader) {
		if(hasGroup(oldleader)) {
			getGroup(oldleader).setLeader(newleader);
			playerIslands.put(newleader, playerIslands.remove(oldleader));
			if(plugin.isConnected(oldleader)) plugin.getServer().getPlayer(oldleader).sendMessage(ChatColor.GREEN + "Le nouveau leader du groupe est maintenant " + ChatColor.BLUE + newleader + ChatColor.GREEN + " !");
			if(plugin.isConnected(newleader)) plugin.getServer().getPlayer(newleader).sendMessage(ChatColor.GREEN + "Vous �tes le nouveau leader du groupe !");
		}
	}



	public void afficherIslandList(Player player) {
		for(Entry<String, Island> entry : playerIslands.entrySet()) {
    		String name = (String) entry.getKey();
    		player.sendMessage(name);
		}
	}
	
	public void afficherGroupList(Player player) {
		Iterator<Group> it = groupList.iterator();
		while(it.hasNext()) {
			Group group = it.next();
			player.sendMessage(group.getLeader() + "  " + group.getMembers());
			if(group.getMembers().isEmpty()) {
				groupList.remove(group);
				player.sendMessage(ChatColor.RED + "Groupe de " + group.getLeader() + "  supprim�.");
			}
		}
	}
	
	public void afficherOrphanedList(Player player) {
		Iterator<Island> it = orphaned.iterator();
		while(it.hasNext()) {
			Island island = it.next();
			player.sendMessage(island.getX() + "  " + island.getZ());
		}
	}
}
