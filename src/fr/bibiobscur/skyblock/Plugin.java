package fr.bibiobscur.skyblock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import fr.bibiobscur.skyblock.ajouts.ChallengeDetector;
import fr.bibiobscur.skyblock.ajouts.MobsProperties;
import fr.bibiobscur.skyblock.ajouts.NewSigns;
import fr.bibiobscur.skyblock.ajouts.PegaseProperties;
import fr.bibiobscur.skyblock.group.IslandGroupCommands;
import fr.bibiobscur.skyblock.hell.HellCommands;
import fr.bibiobscur.skyblock.hell.HellDatas;
import fr.bibiobscur.skyblock.hell.HellProperties;


public class Plugin extends JavaPlugin{
	
	private final String pluginName = "Skyblock v1.0";
	private final String path = new File("").getAbsolutePath();
	private String directory;
	
	private final int ISLAND_SPACING = 200;
	private final int ISLANDS_Y = 64;
	private String worldname = "";
	private SkyDatas datas;
	private HellDatas helldatas;
	
	//private int secondsBeforeSave;
	private Logger logger = Logger.getLogger("Minecraft");
	
//---------------------------------------------------------------------------------------------------------
	//Méthodes onEnable, onDisable
	public void onEnable() {

		//Enable Skyblock commands
		getCommand("is").setExecutor(new IslandCommands(this));
		getCommand("isg").setExecutor(new IslandGroupCommands(this));
		getCommand("sky").setExecutor(new IslandOpCommands(this));
		getCommand("createskyworld").setExecutor(new CreateWorldCommand(this));
		getCommand("createhellworld").setExecutor(new CreateWorldCommand(this));
		getCommand("ishell").setExecutor(new HellCommands(this));
		
		//Enable Skyblock listeners
		new IslandProtect(this);
		new ChallengeDetector(this);
		new NewSigns(this);
		new HellProperties(this);
		new MobsProperties(this);
		new PegaseProperties(this);
		
		//Create new Skyblock datas
		datas = new SkyDatas(this);
		helldatas = new HellDatas(this);
		
		//Define directory to load and save datas
		new File(path + "/plugins/SkyblockDatas").mkdirs();
		directory = path + "/plugins/SkyblockDatas/";
		logger.info("[" + pluginName + "] Dossier de stockage des données défini a l'emplacement '" + directory + "'.");
		new File(directory + "HellDatas/").mkdirs();
		
		//Load datas
		datas.load(directory);
		helldatas.load(directory + "HellDatas/");
		loadWorldName();
		helldatas.loadworldname(directory + "HellDatas/");
        
		/*logger.info("[" + pluginName + "] Determination des level des iles. Cette operation peut prendre un peu de temps");
        datas.defineLevel();*/
        
        //Autosave activation
        autosave();
        //autodefinelevels();
        
        ShapedRecipe soulsand = new ShapedRecipe(new ItemStack(Material.SOUL_SAND, 1)).shape("aaa", "aba", "aaa").setIngredient('a', Material.GRAVEL).setIngredient('b', Material.FLINT);
        ShapedRecipe icepacked = new ShapedRecipe(new ItemStack(Material.PACKED_ICE, 1)).shape("aa", "aa").setIngredient('a', Material.ICE);
        getServer().addRecipe(soulsand);
        getServer().addRecipe(icepacked);
	}
	
	public void onDisable() {
		saveDatas();
		getServer().getWorld(worldname).save();
		getServer().getWorld(helldatas.getworldname()).save();
		getServer().clearRecipes();
	}

	public void saveDatas() {
		datas.save(directory);
		helldatas.save(directory + "HellDatas/");
	}
	
	public void autosave() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
    	    public void run(){
    	    	saveDatas();
				Bukkit.broadcastMessage(ChatColor.GOLD + " -- " + ChatColor.RED + "Donnees du skyblock sauvegardees." + ChatColor.GOLD + " -- ");
    	    }
        }, 0L, 20 * 60 * 15L);
	}
	
	public void autodefinelevels() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
    	    public void run(){
    	    	datas.defineLevel();
    	    }
        }, 0L, 20 * 60 * 30L);
	}

//---------------------------------------------------------------------------------------------------------
	//WorldName
	public void loadWorldName() {
		try {
			if(new File(directory + "SkyblockWorldName.txt").exists()){
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(directory + "SkyblockWorldName.txt")));
				worldname = br.readLine();
				br.close();
				/*WorldCreator wc = new WorldCreator(worldname);
				wc.generator(new NullChunkGenerator());
				wc.seed(-1168696288156746053L);
				wc.type(WorldType.NORMAL);
				wc.createWorld();
		        World world = getServer().getWorld(worldname);
				world.setDifficulty(Difficulty.HARD);
				world.setPVP(true);
				world.setMonsterSpawnLimit(110);*/
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
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public void changeHellWorldName(String newWorldName) {
		helldatas.setworldname(newWorldName);
		new File(directory + "HellDatas/HellWorldName.txt").delete();
		
		try {
			new File(directory + "HellDatas/HellWorldName.txt").createNewFile();
			FileWriter w = new FileWriter(directory + "HellDatas/HellWorldName.txt");
			w.write(newWorldName);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


//---------------------------------------------------------------------------------------------------------
	//Méthodes get
	public String getPluginName() {
		return pluginName;
	}
	
	public int getISLAND_SPACING(){
		return ISLAND_SPACING;
	}
	
	public SkyDatas getDatas() {
		return datas;
	}
	
	public HellDatas getHellDatas() {
		return helldatas;
	}
	
    public int getISLANDS_Y() {
    	return ISLANDS_Y;
    }
    
    public String getworldname() {
    	return worldname;
    }
    
//---------------------------------------------------------------------------------------------------------
    //Méthode sur les joueurs
	public boolean isOnSpawn(Location location) {
		if(location.getBlockX() <= ISLAND_SPACING/2 && location.getBlockX() >= -ISLAND_SPACING/2 &&
				location.getBlockZ() <= ISLAND_SPACING/2 && location.getBlockZ() >= -ISLAND_SPACING/2)
			return true;
		return false;
	}
	
	public boolean isConnected(String playername) {
		for(int i = 0; i < getServer().getOnlinePlayers().length; i++) {
			if(getServer().getOnlinePlayers()[i].getName().equalsIgnoreCase(playername)) return true;
		}
		return false;
	}
	
    
}
