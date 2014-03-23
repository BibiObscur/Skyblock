package fr.bibiobscur.skyblock.hellextension;

import java.util.HashMap;
import java.util.Stack;

import fr.bibiobscur.skyblock.Island;
import fr.bibiobscur.skyblock.Plugin;

public class Hell {
	private final Plugin plugin;
	private final String hellworldname;
	private final String directory;
	private HashMap<String, Island> playerHellIslands = new HashMap<String, Island>();
	private Stack<Island> hellOrphaned = new Stack<>();
	private Island lastHellIsland;
	
	public Hell(Plugin plugin,
			HashMap<String, Island> playerHellIslands,
			Stack<Island> hellOrphaned,
			Island lastHellIsland,
			String hellworldname,
			String directory) {
		this.plugin = plugin;
		this.playerHellIslands = playerHellIslands;
		this.hellOrphaned = hellOrphaned;
		this.lastHellIsland = lastHellIsland;
		this.hellworldname = hellworldname;
		this.directory = directory;
	}
}
