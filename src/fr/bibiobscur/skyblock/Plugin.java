package fr.bibiobscur.skyblock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Stack;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin{
	
	private HashMap<String, Island> playerIslands = new HashMap<String, Island>();
	private Stack<Island> orphaned = new Stack<>();
	private HashSet<Group> groupList = new HashSet<Group>();
	private final int ISLAND_SPACING = 100;
	private final int ISLANDS_Y = 64;
	private Island lastIsland;
	private HashMap<String, Home> playerHomes = new HashMap<String, Home>();
	private String worldname = "";
	private final String path = new File("").getAbsolutePath();
	private String directory;
	private Logger logger = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		getCommand("is").setExecutor(new IslandCommands(this));
		getCommand("isg").setExecutor(new IslandGroupCommands(this));
		getCommand("sky").setExecutor(new IslandOpCommands(this));
		getCommand("createskyworld").setExecutor(new CreateWorldCommand(this));
		new IslandProtect(this);
		new ChallengeDetector(this);
		
		
		new File(path + "/plugins/SkyblockDatas").mkdirs();
		directory = path + "/plugins/SkyblockDatas/";
		
        try {
        	if(new File(directory + "SkyblockWorldName.txt").exists()){
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(directory + "SkyblockWorldName.txt")));
				worldname = br.readLine();
				br.close();
				WorldCreator wc = new WorldCreator(worldname);
    			wc.generator(new NullChunkGenerator());
    			wc.seed(-1168696288156746053L);
    			wc.createWorld();
    	        World world = getServer().getWorld(worldname);
    			world.setDifficulty(Difficulty.HARD);
    			world.setPVP(true);
        	} else
        		new File(directory + "SkyblockWorldName.txt");
        	
        	/*File f = new File ("./");
        	for (File tmp : f.listFiles()){
        		if(tmp.isDirectory()) {
        			if(tmp.getName().equals(worldname)) {
        				logger.info("Loaded world '" + worldname + "' sucessfully!");
        				getServer().getWorlds().add(this.getServer().getWorld(worldname));
        			}
        		}
        	}*/
        	
        	if(new File(directory + "lastIsland.bin").exists())
        		lastIsland = (Island)SLAPI.load(directory + "lastIsland.bin");
        	if(null == lastIsland) {
            	lastIsland = new Island(); 
            	lastIsland.x = 0;
            	lastIsland.z = 0;
        	}
        	if(new File(directory + "playerIslands.bin").exists()){
            	@SuppressWarnings("unchecked")
        		HashMap<String,Island> load = (HashMap<String,Island>)SLAPI.load(directory + "playerIslands.bin");
            	System.out.println("[BibiSkyblock] Chargement des iles.");
    			if(null != load) 
    				playerIslands = load;
        	} else
        		new File(directory + "playerIslands.bin");
        	
        	if(new File(directory + "orphanedIslands.bin").exists()) {
        		@SuppressWarnings("unchecked")
				Stack<Island> load = (Stack<Island>)SLAPI.load(directory + "orphanedIslands.bin");
        		System.out.println("[BibiSkyblock] Chargement des iles supprim�es.");
				if(null != load)
					orphaned = load;
			}else
        		new File(directory + "orphanedIslands.bin");
        	
        	if(new File(directory + "playerHomes.bin").exists()) {
        		@SuppressWarnings("unchecked")
				HashMap<String,Home> load = (HashMap<String,Home>)SLAPI.load(directory + "playerHomes.bin");
        		System.out.println("[BibiSkyblock] Chargement des homes.");
        		if(null != load)
        			playerHomes = load;
        	} else
        		new File(directory + "playerHomes.bin");
        	
        	if(new File(directory + "groupList.bin").exists()) {
        		@SuppressWarnings("unchecked")
				HashSet<Group> load = (HashSet<Group>)SLAPI.load(directory + "groupList.bin");
        		System.out.println("[BibiSkyblock] Chargement de la liste des groupes.");
        		if(null != load)
        			groupList = load;
        	} else
        		new File(directory + "groupList.bin");
        	
        } catch (Exception e) {
			System.out.println("Could not load Island data from disk.");
			e.printStackTrace();
        }
        
        System.out.println("Determination des level des iles. Cette operation peut prendre un peu de temps");
        defineLevel();
        
	}
	
	public void onDisable() {
		getServer().getWorld(worldname).save();
		try {
			SLAPI.save(playerIslands, directory + "playerIslands.bin");
			SLAPI.save(lastIsland, directory + "lastIsland.bin");
			SLAPI.save(orphaned, directory + "orphanedIslands.bin");
			SLAPI.save(playerHomes, directory + "playerHomes.bin");
			SLAPI.save(groupList, directory + "groupList.bin");
		} catch (Exception e) {
			System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
			e.printStackTrace();
		}
	}
	
	public int getISLAND_SPACING(){
		return ISLAND_SPACING;
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
    	if(hasIsland(player.getName()) && player.getLocation().getWorld().getName().equals(worldname)) {

    		World world = getServer().getWorld(worldname);
    		Island island;

    		if(hasGroup(player.getName()))
    			island = getPlayerIsland(getGroup(player.getName()).getLeader());
    		else
    			island = getPlayerIsland(player.getName());
    		
    		int x = player.getLocation().getBlockX();
			int y = player.getLocation().getBlockY();
			int z = player.getLocation().getBlockZ();

    		if(x > island.x - ISLAND_SPACING/2 && x < island.x + ISLAND_SPACING/2
    				&& z > island.z - ISLAND_SPACING/2 && z < island.z + ISLAND_SPACING/2
    				&& y > 5 && y < world.getMaxHeight() - 5) {
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
    
    public boolean isOnIsland(Player player, Location location) {
    	if(hasIsland(player.getName()) && location.getWorld().getName().equals(worldname)) {
    		
    		World world = getServer().getWorld(worldname);
    		Island island;
    		
    		if(hasGroup(player.getName()))
    			island = getPlayerIsland(getGroup(player.getName()).getLeader());
    		else
    			island = getPlayerIsland(player.getName());
    		
    		int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
    		
    		if(x > island.x - ISLAND_SPACING/2 && x < island.x + ISLAND_SPACING/2
    				&& z > island.z - ISLAND_SPACING/2 && z < island.z + ISLAND_SPACING/2
    				&& y > 5 && y < world.getMaxHeight() - 5) {
    			return true;
    		}
    		return false;
    	}
    	return false;
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
    	spawn.x = 0;
    	spawn.z = 0;
    	return spawn;
    }
    
    public String getHostHere(Location location) {
    	int x = location.getBlockX();
    	int z = location.getBlockZ();
    	Island island;
    	
    	for (Entry<String, Island> entry : playerIslands.entrySet()) {
    		island = (Island) entry.getValue();
    		if(x > island.x-ISLAND_SPACING/2 && 
    				x < island.x+ISLAND_SPACING/2 &&
    				z > island.z-ISLAND_SPACING/2 &&
    				z < island.z+ISLAND_SPACING/2)
    			return (String) entry.getKey();
    	}
    	
    	return null;
    }
    
	public void defineLevel() {
    	
    	Island island;
    	World world = getServer().getWorld(worldname);
    	Block block;
    	int level;
    	double xp;
    	double xpblock[] = new double[9];
    	int i, j, k;
    	
    	for(Entry<String, Island> entry : playerIslands.entrySet()) {
    		
    		island = (Island) entry.getValue();
    		level = 0;
    		xp = 0;
    		for(i = 0; i < xpblock.length; i++)
    			xpblock[i] = 0;
    		xpblock[2] = - 50;
    		xpblock[3] = - 27;
    		
    		for(i = island.x - ISLAND_SPACING/2; i < island.x + ISLAND_SPACING/2; i++) {
    			for(j = 5; j < getServer().getWorld(worldname).getMaxHeight()-5; j++) {
	    			for(k = island.z - ISLAND_SPACING/2; k < island.z + ISLAND_SPACING/2; k++) {
	    				block = world.getBlockAt(new Location(world, i, j, k));
	        			if(block.getType() != Material.AIR && block.getType() != Material.WATER) {
	        				if(block.getType() == Material.GRASS || block.getType() == Material.MYCEL)
	        					xpblock[1] += 1;
	        				else if(block.getType() == Material.DIRT)
	        					xpblock[2] += 1 - ((xpblock[2] > 576)?0.5:0);
	        				else if(block.getType() == Material.SAND)
	        					xpblock[3] += 1 - ((xpblock[3] > 576)?0.5:0);
	        				else if(block.getType() == Material.WOOD)
	        					xpblock[4] += 1 - ((xpblock[4] > 576)?0.5:0) - ((xpblock[4] > 3584)?0.25:0);
	        				else if(block.getType() == Material.SMOOTH_BRICK || block.getType() == Material.STONE)
	        					xpblock[5] += 1 - ((xpblock[5] > 576)?0.5:0) - ((xpblock[5] > 3584)?0.25:0);
	        				else if(block.getType() == Material.STEP)
	        					xpblock[6] += 1 - ((xpblock[6] > 576*2)?0.5:0) - ((xpblock[6] > 3584*2)?0.25:0);
	        				else if(block.getType() == Material.SMOOTH_STAIRS)
	        					xpblock[7] += 1 - ((xpblock[7] > 576)?0.5:0) - ((xpblock[7] > 3584)?0.25:0);
	        				else if(block.getType() == Material.COBBLESTONE)
	        					xpblock[8] += 0.5 - ((xpblock[8] > 576)?0.5:0);
	        				else
	        					xpblock[0] ++;
	        			}
	        		}
    			}
    		}
    		xp = 1 * xpblock[0] + 8 * xpblock[1] + 6 * xpblock[2] + 5 * xpblock[3] + 2 * xpblock[4] + 4 * xpblock[5] + 2 * xpblock[6] + 4 * xpblock[7] + 0.5 * xpblock[8] + 250 * island.challenges.size();
    		//level = (int)(Math.pow((xp), 1.0/3) * 10);
    		if(xp <= 16383.875)
    			level = (int)(xp/32.76775);
    		else
    			level = (int)(Math.pow((xp/(Math.pow(2, 17)-1)), 1.0/3) * 1000);
			
    		island.level = level;
    	}
    }
    
    public Home getPlayerHome(String playerName) {
    	if(hasIsland(playerName)) {
    		if (playerHomes.containsKey(playerName))
    			return playerHomes.get(playerName);
    		else
    			return playerHomes.get(playerName.toLowerCase());
    	} 
    	
    	Home home = new Home();
    	home.x = getServer().getWorld(worldname).getSpawnLocation().getBlockX();
    	home.y = getServer().getWorld(worldname).getSpawnLocation().getBlockY();
    	home.z = getServer().getWorld(worldname).getSpawnLocation().getBlockZ();
    	return home;
    }
    
	public boolean hasOrphanedIsland() {
    	return !orphaned.empty();
    }

	public Island getOrphanedIsland() {
    	if(hasOrphanedIsland()) {
    		return orphaned.pop();
    	}
    	
    	Island spawn = new Island();
    	spawn.x = 0;
    	spawn.z = 0;
    	
    	return spawn;
    }

    public Island getLastIsland() { return lastIsland; }
    public void setLastIsland(Island island) { lastIsland = island; try{SLAPI.save(lastIsland, directory + "lastIsland.bin");}catch(Exception e){}}
    public HashMap<String, Island> getPlayers() {return playerIslands; }
    public int getISLANDS_Y() { return ISLANDS_Y; }
    public HashMap<String, Home> getHomes() {return playerHomes; }
    public String getworldname() { return worldname; }
    
    public void teleportIsland(Player player) {
    	
		Island island = getPlayerIsland(player.getName());
		World world = getServer().getWorld(worldname);
		int h = ISLANDS_Y;
		
		while(world.getBlockAt(island.x, h, island.z).getType() != Material.AIR) {
				h++;
		}
		h++;
		world.loadChunk(island.x, island.z);
		player.teleport(new Location(world, island.x, h, island.z));	
	}
    
    public void teleportIsland(Player player, String playerdest) {
    	
		Island island = getPlayerIsland(playerdest);
		World world = getServer().getWorld(worldname);
		int h = ISLANDS_Y;
		
		while(world.getBlockAt(island.x, h, island.z).getType() != Material.AIR) {
				h++;
		}
		h++;
		world.loadChunk(island.x, island.z);
		player.teleport(new Location(world, island.x, h, island.z));	
	}
    
    public void teleportHome(Player player) {
    	
    	Home home = getPlayerHome(player.getName());
    	World world = getServer().getWorld(worldname);
    	Location location = new Location(world, home.x, home.y, home.z);
    	int h = location.getBlockY();
    	
	    while(world.getBlockAt(location.getBlockX(), h, location.getBlockZ()).getType() != Material.AIR) {
				h++;
		}
	    h++;
	    
		world.loadChunk(location.getBlockX(), location.getBlockZ());
		player.teleport(new Location(world, location.getBlockX(), h, location.getBlockZ()));
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
    	
    	if(hasIsland(player.getName())) {
	    	if(isOnIsland(player)) {
	    		if(hasHome(player.getName()))
	    			playerHomes.remove(player.getName());
	    		
	    		Home home = new Home();
	    		home.x = location.getBlockX();
	    		home.y = location.getBlockY();
	    		home.z = location.getBlockZ();
	    		playerHomes.put(player.getName(), home);
	    		try {
					SLAPI.save(playerHomes, directory + "playerHomes.bin");
				} catch (Exception e) {
					System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
					e.printStackTrace();
				}
	    		
	    		return true;
	    	}
    		player.sendMessage(ChatColor.RED + "Vous devez �tre sur votre �le pour cr�er un home.");
    		return false;
    	}
    	
    	player.sendMessage(ChatColor.RED + "Vous devez poss�der une �le pour cr�er un home. Pour cr�er une �le, tapez /is create.");
    	return false;
    }
	
	public void registerPlayerIsland(String player, Island newIsland) {
		playerIslands.put(player, newIsland);
		try {
			SLAPI.save(playerIslands, directory + "playerIslands.bin");
			SLAPI.save(lastIsland, directory + "lastIsland.bin");
			SLAPI.save(orphaned, directory + "orphanedIslands.bin");
			SLAPI.save(playerHomes,directory +  "playerHomes.bin");
			SLAPI.save(groupList, directory + "groupList.bin");
		} catch (Exception e) {
			System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
			e.printStackTrace();
		}
	}
	
	public void deleteIsland(String playerName,World world) {
		if(hasIsland(playerName) && isLeader(playerName)) {
			Island island = getPlayerIsland(playerName);
			int length = ISLAND_SPACING;
			for(int x = island.x - length/2; x <= island.x + length/2; x++)
				for(int y = 0; y < world.getMaxHeight(); y++)
					for(int z = island.z - length/2; z <= island.z + length/2; z++) {
						Block block = world.getBlockAt(x,y,z);
						if(block.getType() != Material.AIR)
							block.setType(Material.AIR);
					}
			
			island.challenges.clear();
			orphaned.push(island);
			playerIslands.remove(playerName);
			
			if(hasGroup(playerName))
				removeGroup(playerName);
			
			playerHomes.remove(playerName);
			
			getServer().getPlayer(playerName).setLevel(0);
			
			try {
				SLAPI.save(playerIslands, directory + "playerIslands.bin");
				SLAPI.save(lastIsland, directory + "lastIsland.bin");
				SLAPI.save(orphaned, directory + "orphanedIslands.bin");
				SLAPI.save(playerHomes, directory + "playerHomes.bin");
				SLAPI.save(groupList, directory + "groupList.bin");
			} catch (Exception e) {
				System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
				e.printStackTrace();
			}
		}
	}

	public boolean isOnSpawn(Location location) {
		if(location.getBlockX() <= ISLAND_SPACING/2 && location.getBlockX() >= -ISLAND_SPACING/2 &&
				location.getBlockZ() <= ISLAND_SPACING/2 && location.getBlockZ() >= -ISLAND_SPACING/2)
			return true;
		return false;
	}
	
	public void changeWorldName(String newWorldName) {
		worldname = newWorldName;
		new File(directory + "SkyblockWorldName.txt").delete();
		
		try {
			new File(directory + "SkyblockWorldName.txt").createNewFile();
			FileWriter w = new FileWriter(directory + "SkyblockWorldName.txt");
			w.write(newWorldName);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean hasGroup(String playername) {
		Group group;
		Iterator<Group> it = groupList.iterator();
		while(it.hasNext())
		{
			group = it.next();
			if(group != null) {
				if(group.getMembers().contains(playername)) return true;
			}
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
			getServer().getPlayer(playername).getInventory().clear();
			teleportIsland(getServer().getPlayer(playername));
			createHome(getServer().getPlayer(playername), new Location(getServer().getWorld(worldname), getPlayerIsland(leader).x, ISLANDS_Y, getPlayerIsland(leader).z));
			if(isConnected(leader)) getServer().getPlayer(leader).sendMessage(ChatColor.GREEN + "Le joueur " + ChatColor.BLUE + playername + ChatColor.GREEN + " a �t� ajout� � votre groupe.");
			if(isConnected(playername)) getServer().getPlayer(playername).sendMessage(ChatColor.GREEN + "Vous avez rejoint le groupe de " + ChatColor.BLUE + leader + ChatColor.GREEN + " !");
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
			if(isConnected(leader)) getServer().getPlayer(leader).sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername + ChatColor.RED + " a �t� exclu de votre groupe.");
			if(isConnected(playername)) getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Vous avez �t� exclu du groupe par " + ChatColor.BLUE + leader + ChatColor.RED + ".");
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
			if(isConnected(playername)) getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Vous avez quitt� le groupe de " + ChatColor.BLUE + group.getLeader() + ChatColor.RED + ".");
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
			if(isConnected(leader)) getServer().getPlayer(leader).sendMessage(ChatColor.RED + "Votre groupe a �t� dissout");
		}
	}
	
	public void changeLeader(String oldleader, String newleader) {
		if(hasGroup(oldleader)) {
			getGroup(oldleader).setLeader(newleader);
			Island island = getPlayerIsland(oldleader);
			playerIslands.remove(oldleader);
			playerIslands.put(newleader, island);
			if(isConnected(oldleader)) getServer().getPlayer(oldleader).sendMessage(ChatColor.GREEN + "Le nouveau leader du groupe est maintenant " + ChatColor.BLUE + newleader + ChatColor.GREEN + " !");
			if(isConnected(newleader)) getServer().getPlayer(newleader).sendMessage(ChatColor.GREEN + "Vous �tes le nouveau leader du groupe !");
		}
	}
	
	public boolean isConnected(String playername) {
		for(int i = 0; i < getServer().getOnlinePlayers().length; i++) {
			if(getServer().getOnlinePlayers()[i].getName().equalsIgnoreCase(playername)) return true;
		}
		return false;
	}
	
    
}
