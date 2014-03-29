package fr.bibiobscur.skyblock;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Island implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int x;
	private int z;
	private int level;
	private HashSet<String> challenges = new HashSet<String>();
	
	public int getLevel() { return level; }
	public int getX() { return x; }
	public int getZ() { return z; }
	
	
	public void setLevel(int level) { this.level = level; }
	public void setX(int x) { this.x = x; }
	public void setZ(int z) { this.z = z; }
	
	public HashSet<String> getChallenges() { return challenges; }
	
	public String getChallengeList() {
		
		String tab[] = new String[challenges.size()];
		int i = 0;
		
		Iterator<String> it = challenges.iterator();
		while(it.hasNext()) {
			tab[i] = it.next();
			i ++;
		}
		
		Arrays.sort(tab);
		
		String challengelist = "";
		for(i = 0; i < tab.length; i++) {
			challengelist += "§a" + tab[i] + "§f, ";
		}
		
		return challengelist;
	}

	public void build(World world) {

		int y = 64;
		int x_operate;
		int y_operate;
		int z_operate;
		
		for(x_operate = x; x_operate < x+3; x_operate++){
			for(z_operate = z; z_operate < z+6; z_operate++){
				Block blockToChange = world.getBlockAt(x_operate,y+2,z_operate);
				blockToChange.setType(Material.GRASS);  //chest area
			}
	    }
		for(x_operate = x; x_operate < x+3; x_operate++){
			for(y_operate = y; y_operate < y+2; y_operate++){
				for(z_operate = z; z_operate < z+6; z_operate++){
					Block blockToChange = world.getBlockAt(x_operate,y_operate,z_operate);
					blockToChange.setType(Material.DIRT);  //chest area
				}
	    	}
	    }
	
	    for(x_operate = x+3; x_operate < x+6; x_operate++){
	    	for(y_operate = y; y_operate < y+3; y_operate++){
	    		for(z_operate = z+3; z_operate < z+6; z_operate++){
	            	Block blockToChange = world.getBlockAt(x_operate,y+2,z_operate);
	                blockToChange.setType(Material.GRASS);    // 3x3 corner
	            }
	        }
	    }
	    for(x_operate = x+3; x_operate < x+6; x_operate++){
	    	for(y_operate = y; y_operate < y+2; y_operate++){
	    		for(z_operate = z+3; z_operate < z+6; z_operate++){
	            	Block blockToChange = world.getBlockAt(x_operate,y_operate,z_operate);
	                blockToChange.setType(Material.DIRT);    // 3x3 corner
	            }
	        }
	    }
	    
	    world.generateTree(new Location(world,x+4 , y+3, z+4), TreeType.TREE);

	
	    // chest
	    Block blockToChange = world.getBlockAt(x+1,y+3,z+1);
	    blockToChange.setType(Material.CHEST);
	    Chest chest = (Chest) blockToChange.getState();
        Inventory inventory = chest.getInventory();
        inventory.clear();
        ItemStack item = new ItemStack(Material.LAVA_BUCKET);
        inventory.addItem(item);
        item.setType(Material.SUGAR_CANE);
        inventory.addItem(item);
        item.setType(Material.RED_MUSHROOM);
        inventory.addItem(item);
        item.setType(Material.ICE);
        item.setAmount(2);
        inventory.addItem(item);
        item.setType(Material.PUMPKIN_SEEDS);
        item.setAmount(1);
        inventory.addItem(item);
        item.setType(Material.BROWN_MUSHROOM);
        inventory.addItem(item);
        item.setType(Material.MELON_SEEDS);
        inventory.addItem(item);
        item.setType(Material.CACTUS);
        inventory.addItem(item);
        item.setType(Material.SIGN);
        inventory.addItem(item);
		
		
        blockToChange = world.getBlockAt(x,y,z);
        blockToChange.setType(Material.BEDROCK);
        
        blockToChange = world.getBlockAt(x+1,y+1,z+1);
        blockToChange.setType(Material.SAND);
        blockToChange = world.getBlockAt(x+1,y+1,z+2);
        blockToChange.setType(Material.SAND);
        blockToChange = world.getBlockAt(x+1,y+1,z+3);
        blockToChange.setType(Material.SAND);
	}

	public void buildHell(World world) {

		int y = 64;
		int x_operate;
		int y_operate;
		int z_operate;
		
		for(x_operate = x; x_operate < x+3; x_operate++){
			for(y_operate = y; y_operate < y+3; y_operate++){
				for(z_operate = z; z_operate < z+6; z_operate++){
					Block blockToChange = world.getBlockAt(x_operate,y_operate,z_operate);
					blockToChange.setType(Material.SOUL_SAND);  //chest area
				}
	    	}
	    }
		
	    for(x_operate = x+3; x_operate < x+6; x_operate++){
	    	for(y_operate = y; y_operate < y+3; y_operate++){
	    		for(z_operate = z+3; z_operate < z+6; z_operate++){
	            	Block blockToChange = world.getBlockAt(x_operate,y_operate,z_operate);
	                blockToChange.setType(Material.SOUL_SAND);
	            }
	        }
	    }
	    
	    world.getBlockAt(x + 2, y, z).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 2, y + 2, z).setType(Material.NETHERRACK);
	    world.getBlockAt(x, y + 2, z).setType(Material.NETHERRACK);
	    world.getBlockAt(x, y, z + 5).setType(Material.NETHERRACK);
	    world.getBlockAt(x, y + 2, z + 5).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 5, y, z + 3).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 5, y, z + 5).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 5, y + 2, z + 3).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 5, y + 2, z + 5).setType(Material.NETHERRACK);
	    world.getBlockAt(x + 1, y + 2, z + 4).setType(Material.LAVA);
	    generateTree(world, x + 4, y + 3, z + 4);

	    // chest
	    Block blockToChange = world.getBlockAt(x+1,y+3,z+1);
	    blockToChange.setType(Material.CHEST);
	    Chest chest = (Chest) blockToChange.getState();
        Inventory inventory = chest.getInventory();
        inventory.clear();
        ItemStack item = new ItemStack(Material.LAVA_BUCKET);
        inventory.addItem(item);
        item.setType(Material.STONE_PICKAXE);
        inventory.addItem(item);
        item.setType(Material.RED_MUSHROOM);
        inventory.addItem(item);
        item.setType(Material.FLINT);
        inventory.addItem(item);
        item.setType(Material.BROWN_MUSHROOM);
        inventory.addItem(item);
        item.setType(Material.SIGN);
        inventory.addItem(item);
		
        blockToChange = world.getBlockAt(x,y,z);
        blockToChange.setType(Material.BEDROCK);
        
        blockToChange = world.getBlockAt(x+1,y+1,z+1);
        blockToChange.setType(Material.QUARTZ_ORE);
        blockToChange = world.getBlockAt(x+1,y+1,z+2);
        blockToChange.setType(Material.QUARTZ_ORE);
        blockToChange = world.getBlockAt(x+1,y+1,z+3);
        blockToChange.setType(Material.QUARTZ_ORE);
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
				if(world.getBlockAt(x + j, y + taille - 2, z + k).getType() == Material.AIR &&
						Math.abs(j) != Math.abs(k) && (Math.abs(j) == 2 || Math.abs(k) == 2))
					world.getBlockAt(x + j, y + taille - 2, z + k).setType(Material.GLOWSTONE);
			}
		}
		
		return true;
	}
	
}