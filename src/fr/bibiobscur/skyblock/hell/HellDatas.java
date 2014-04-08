package fr.bibiobscur.skyblock.hell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.WorldCreator;

import fr.bibiobscur.skyblock.Home;
import fr.bibiobscur.skyblock.Island;
import fr.bibiobscur.skyblock.NullChunkGenerator;
import fr.bibiobscur.skyblock.Plugin;
import fr.bibiobscur.skyblock.SLAPI;

public class HellDatas {
	private final Plugin plugin;
	private String worldname;
	private HashMap<String, Island> playerHellIslands = new HashMap<String, Island>();
	private Stack<Island> hellOrphaned = new Stack<>();
	private Island lastHellIsland;
	private HashMap<String, Home> playerHellHomes = new HashMap<String, Home>();
	private Logger logger = Logger.getLogger("Minecraft");
	
	public HellDatas(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	public void load(String directory) {
		try {
        	logger.info("[" + plugin.getPluginName() + "] Chargement des donnees du nether.");
        	
        	if(new File(directory + "lastHellIsland.bin").exists())
        		lastHellIsland = (Island)SLAPI.load(directory + "lastHellIsland.bin");
        	if(null == lastHellIsland) {
            	lastHellIsland = new Island(); 
            	lastHellIsland.setX(0);
            	lastHellIsland.setZ(0);
        	}
        	
        	if(new File(directory + "playerHellIslands.bin").exists()){
        		HashMap<String,Island> load = (HashMap<String,Island>)SLAPI.load(directory + "playerHellIslands.bin");
        		logger.info("[" + plugin.getPluginName() + "] Chargement des iles du nether.");
    			if(null != load) 
    				playerHellIslands = load;
        	} else
        		new File(directory + "playerHellIslands.bin");
        	
        	if(new File(directory + "hellOrphanedIslands.bin").exists()) {
				Stack<Island> load = (Stack<Island>)SLAPI.load(directory + "hellOrphanedIslands.bin");
				logger.info("[" + plugin.getPluginName() + "] Chargement des iles supprimees du nether.");
				if(null != load)
					hellOrphaned = load;
			}else
        		new File(directory + "hellOrphanedIslands.bin");
        	
        	if(new File(directory + "playerHellHomes.bin").exists()) {
				HashMap<String,Home> load = (HashMap<String,Home>)SLAPI.load(directory + "playerHellHomes.bin");
				logger.info("[" + plugin.getPluginName() + "] Chargement des homes du nether.");
        		if(null != load)
        			playerHellHomes = load;
        	} else
        		new File(directory + "playerHomes.bin");
        	
        } catch (Exception e) {
        	logger.severe("[" + plugin.getPluginName() + "] Echec du chargement des donnees du nether.");
			e.printStackTrace();
        }
	}
	
	public void save(String directory) {
		try {
			logger.info("[" + plugin.getPluginName() + "] Sauvegarde des donnees du nether...");
			SLAPI.save(playerHellIslands, directory + "playerHellIslands.bin");
			SLAPI.save(lastHellIsland, directory + "lastHellIsland.bin");
			SLAPI.save(hellOrphaned, directory + "hellOrphanedIslands.bin");
			SLAPI.save(playerHellHomes, directory + "playerHellHomes.bin");
			logger.info("[" + plugin.getPluginName() + "] Sauvegarde terminee.");
		} catch (Exception e) {
			logger.severe("[" + plugin.getPluginName() + "] La sauvegarde des donnees du nether a echoue.");
			e.printStackTrace();
		}
	}

	public void loadworldname(String directory) {
		try {
			if(new File(directory + "HellWorldName.txt").exists()){
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(directory + "HellWorldName.txt")));
				worldname = br.readLine();
				br.close();
				WorldCreator wc = new WorldCreator(worldname);
				wc.generator(new NullChunkGenerator());
				wc.environment(Environment.NETHER);
				wc.createWorld();
		        World world = plugin.getServer().getWorld(worldname);
				world.setDifficulty(Difficulty.HARD);
				world.setPVP(true);
				world.setMonsterSpawnLimit(220);
	    	} else
	    		new File(directory + "HellWorldName.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getworldname() {
		return worldname;
	}
	
	public void setworldname(String worldname) {
		this.worldname = worldname;
	}
//---------------------------------------------------------------------------------------------------------
	//Méthodes sur les îles
	public void addPlayerIsland(String player, Island newIsland) {
		playerHellIslands.put(player, newIsland);
	}
	
	public void deleteIsland(String playerName,World world) {
		System.out.println("TEST");
		System.out.println(plugin.getDatas().isLeader(playerName));
		if(hasIsland(playerName) && plugin.getDatas().isLeader(playerName)) {
			System.out.println("TEST2");
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
			hellOrphaned.push(island);
			playerHellIslands.remove(playerName);
			
			playerHellHomes.remove(playerName);
			System.out.println("Suppression de l'île nether de playerName");
		}
	}
	
	public boolean hasIsland(String playername) {
		if(plugin.getDatas().hasGroup(playername))
			return (playerHellIslands.containsKey(plugin.getDatas().getGroup(playername).getLeader()));
		else if (playerHellIslands.containsKey(playername))
    		return true;
    	else
    		return false;
    }
	
	public boolean isOnIsland(Player player) {
    	if(hasIsland(player.getName()) && player.getLocation().getWorld().getName().equals(worldname)) {

    		World world = plugin.getServer().getWorld(worldname);
    		Island island;

    		if(plugin.getDatas().hasGroup(player.getName()))
    			island = getPlayerIsland(plugin.getDatas().getGroup(player.getName()).getLeader());
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
    	return false;
    }

	public boolean isOnIsland(Player player, Location location) {
    	if(hasIsland(player.getName()) && location.getWorld().getName().equals(worldname)) {
    		
    		World world = plugin.getServer().getWorld(worldname);
    		Island island;
    		
    		if(plugin.getDatas().hasGroup(player.getName()))
    			island = getPlayerIsland(plugin.getDatas().getGroup(player.getName()).getLeader());
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
    	return false;
    }
	
	public Island getPlayerIsland(String playerName) {
    	if(hasIsland(playerName)) {
    		if(plugin.getDatas().hasGroup(playerName) && !plugin.getDatas().isLeader(playerName)) 
    			return getPlayerIsland(plugin.getDatas().getGroup(playerName).getLeader());
    		if (playerHellIslands.containsKey(playerName))
    			return playerHellIslands.get(playerName);
    		else
    			return playerHellIslands.get(playerName.toLowerCase());
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
    	
    	for (Entry<String, Island> entry : playerHellIslands.entrySet()) {
    		island = (Island) entry.getValue();
    		if(x > island.getX()-plugin.getISLAND_SPACING()/2 && 
    				x < island.getX()+plugin.getISLAND_SPACING()/2 &&
    				z > island.getZ()-plugin.getISLAND_SPACING()/2 &&
    				z < island.getZ()+plugin.getISLAND_SPACING()/2)
    			return (String) entry.getKey();
    	}
    	
    	return null;
    }


    public Island getLastIsland() {
    	return lastHellIsland;
    }
    
    public void setLastIsland(Island island) { 
    	lastHellIsland = island;
    }
    
    //Used ?
    public HashMap<String, Island> getPlayers() {
    	return playerHellIslands;
    }
    
	public boolean hasOrphanedIsland() {
    	return !hellOrphaned.empty();
    }

	public Island getOrphanedIsland() {
    	if(hasOrphanedIsland()) {
    		return hellOrphaned.pop();
    	}
    	
    	Island spawn = new Island();
    	spawn.setX(0);
    	spawn.setZ(0);
    	
    	return spawn;
    }
	

	//---------------------------------------------------------------------------------------------------------
		//Méthodes sur les Homes
		//Used ?
	    public HashMap<String, Home> getHomes() {
	    	return playerHellHomes;
	    }
	    
	    public Home getPlayerHome(String playerName) {
	    	if(hasIsland(playerName)) {
	    		if (playerHellHomes.containsKey(playerName))
	    			return playerHellHomes.get(playerName);
	    		else
	    			return playerHellHomes.get(playerName.toLowerCase());
	    	} 
	    	
	    	Home home = new Home();
	    	home.setX(plugin.getServer().getWorld(worldname).getSpawnLocation().getBlockX());
	    	home.setY(plugin.getServer().getWorld(worldname).getSpawnLocation().getBlockY());
	    	home.setZ(plugin.getServer().getWorld(worldname).getSpawnLocation().getBlockZ());
	    	return home;
	    }
		
	    public boolean hasHome(String playername) {
	    	if (playerHellHomes.containsKey(playername))
	    		return true;
	    	else if (playerHellHomes.containsKey(playername.toLowerCase()))
	    		return true;
	    	else
	    		return false;
	    }
	    
	    public boolean createHome(Player player, Location location) {
	    	
	    	if(hasIsland(player.getName()))
	    	{
		    	if(isOnIsland(player))
		    	{
		    		if(hasHome(player.getName()))
		    			playerHellHomes.remove(player.getName());
		    		
		    		Home home = new Home();
		    		home.setX(location.getBlockX());
		    		home.setY(location.getBlockY());
		    		home.setZ(location.getBlockZ());
		    		home.setDirection(location.getDirection().serialize());
		    		playerHellHomes.put(player.getName(), home);
		    		
		    		return true;
		    	}
	    		player.sendMessage(ChatColor.RED + "Vous devez être sur votre île pour créer un home.");
	    		return false;
	    	}
	    	
	    	player.sendMessage(ChatColor.RED + "Vous devez posséder une île pour créer un home. Pour créer une île, tapez /is create.");
	    	return false;
	    }
		
	    
	//---------------------------------------------------------------------------------------------------------
	    //Méthodes sur la téléportation
	    public void teleportIsland(Player player) {
	    	
			Island island = getPlayerIsland(player.getName());
			World world = plugin.getServer().getWorld(worldname);
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
			World world = plugin.getServer().getWorld(worldname);
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
	    	World world = plugin.getServer().getWorld(worldname);
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

}
