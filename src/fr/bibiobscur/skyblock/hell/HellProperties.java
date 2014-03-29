package fr.bibiobscur.skyblock.hell;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;

import fr.bibiobscur.skyblock.Plugin;

public class HellProperties implements Listener{
	
	private final Plugin plugin;
	
	public HellProperties(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean generateTree(Location location) {
		
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		Random rand = new Random();
		int taille = rand.nextInt(3) + 4;
		
		int i, j, k;
		
		//On vérifie qu'il n'y a pas de blocs autour du tronc
		for(i = 0; i < taille + 1; i++)
			for(j = 0; j < 3; j++)
				for(k = 0; k < 3; k++)
					if(world.getBlockAt(x -1 + j, y + i, z - 1 + k).getType() != Material.AIR) return false;
				
		
		//Tronc de gravier
		for(i = 0; i < taille; i++) {
			if(world.getBlockAt(x, y + i, z).getType() == Material.AIR)
				world.getBlockAt(x, y + i, z).setType(Material.GRAVEL);
		}
		
		//Feuilles
		if(world.getBlockAt(x, y + taille, z).getType() == Material.AIR)
			world.getBlockAt(x, y + taille, z).setType(Material.GLOWSTONE);
		
		for(j = 0; j < 3; j++) {
			for(k = 0; k < 3; k++) {
				if(world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).getType() == Material.AIR)
					world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).setType(Material.GLOWSTONE);
			}
		}
		
		for(j = -2; j < 3; j++) {
			for(k = -2; k < 3; k++) {
				if(world.getBlockAt(x + j, y + taille - 2, z + k).getType() == Material.AIR &&
						Math.abs(j) != Math.abs(k) && (Math.abs(j) == 2 || Math.abs(k) == 2))
					world.getBlockAt(x + j, y + taille - 2, z + k).setType(Material.GLOWSTONE);
			}
		}
		
		return true;
	}
	
	public boolean generateTree(World world, int x, int y, int z) {
		Random rand = new Random();
		int taille = rand.nextInt(3) + 4;
		
		int i, j, k;
		
		//On vérifie qu'il n'y a pas de blocs autour du tronc
		for(i = 0; i < taille + 1; i++)
			for(j = 0; j < 3; j++)
				for(k = 0; k < 3; k++)
					if(world.getBlockAt(x -1 + j, y + i, z - 1 + k).getType() != Material.AIR) return false;
				
		
		//Tronc de gravier
		for(i = 0; i < taille; i++) {
			if(world.getBlockAt(x, y + i, z).getType() == Material.AIR)
				world.getBlockAt(x, y + i, z).setType(Material.GRAVEL);
		}
		
		//Feuilles
		if(world.getBlockAt(x, y + taille, z).getType() == Material.AIR)
			world.getBlockAt(x, y + taille, z).setType(Material.GLOWSTONE);
		
		for(j = 0; j < 3; j++) {
			for(k = 0; k < 3; k++) {
				if(world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).getType() == Material.AIR)
					world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).setType(Material.GLOWSTONE);
			}
		}
		
		for(j = -2; j < 3; j++) {
			for(k = -2; k < 3; k++) {
				if(world.getBlockAt(x + j, y + taille - 1, z + k).getType() == Material.AIR &&
						Math.abs(j) != Math.abs(k))
					world.getBlockAt(x + j, y + taille - 1, z + k).setType(Material.GLOWSTONE);
			}
		}
		
		return true;
	}
}
