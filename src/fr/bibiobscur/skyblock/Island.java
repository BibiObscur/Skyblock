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
	
	public Island() {
	}
	
	public Island(int x, int z) {
		this.x = x;
		this.z = z;
		this.level = 0;
	}
	
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
	
	public void defineLevel(World world, int spacing) {
		Block block;
    	double xp;
    	double xpblock[] = new double[10];
    	int i, j, k;
    	
		for(i = 0; i < xpblock.length; i++)
			xpblock[i] = 0;
		xpblock[2] = - 50;
		xpblock[3] = - 27;
		
		for(i = this.x - spacing/2; i < this.x + spacing/2; i++) {
			for(j = 5; j < world.getMaxHeight()-5; j++) {
    			for(k = this.z - spacing/2; k < this.z + spacing/2; k++) {
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
        					xpblock[8] += 1 - ((xpblock[8] > 576)?1:0);
        				else if(block.getType() == Material.SANDSTONE || block.getType() == Material.SANDSTONE_STAIRS)
        					xpblock[9] += 1 - ((xpblock[9] > 576)?0.5:0) - ((xpblock[9] > 3584)?0.25:0);
        				else
        					xpblock[0] += 1 - ((xpblock[0] > 8192)?0.5:0);
        			}
        		}
			}
		}
		xp = 1 * xpblock[0] + 8 * xpblock[1] + 6 * xpblock[2] + 3 * xpblock[3] + 2 * xpblock[4] + 4 * xpblock[5] + 2 * xpblock[6] + 4 * xpblock[7] + 0.5 * xpblock[8] + 6 * xpblock[9] + 250 * challenges.size();

		if(xp <= 16383.875)
			this.level = (int)(xp/32.76775);
		else
			this.level = (int)(Math.pow((xp/(Math.pow(2, 17)-1)), 1.0/3) * 1000);
	}
}