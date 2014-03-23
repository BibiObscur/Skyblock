package fr.bibiobscur.skyblock;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IslandOpCommands implements CommandExecutor {
	
	public final Plugin plugin;
	
    public IslandOpCommands(Plugin plugin) {
        this.plugin = plugin;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
            return false;
        }
		
		//Player player = (Player) sender;
		
		if(!sender.hasPermission("skyblock.opcommands")) {
        	sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
        	return true;
        }
		
		if(commandLabel.equalsIgnoreCase("sky"))
		{
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Commandes op Skyblock : ");
				sender.sendMessage(ChatColor.RED + "/sky help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
				sender.sendMessage(ChatColor.RED + "/sky lastisland" + ChatColor.WHITE + " : Voir la dernière île créée.");
				sender.sendMessage(ChatColor.RED + "/sky world" + ChatColor.WHITE + " : Obtenir le nom du monde skyblock.");
				sender.sendMessage(ChatColor.RED + "/sky createcommunisland" + ChatColor.WHITE + " : Crée une île commune.");
				sender.sendMessage(ChatColor.RED + "/sky definelevels" + ChatColor.WHITE + " : Calcule le niveau de chaque île.");
				sender.sendMessage(ChatColor.RED + "/sky <player> info" + ChatColor.WHITE + " : Obtenir des informations sur un joueur.");
				sender.sendMessage(ChatColor.RED + "/sky <player> tp" + ChatColor.WHITE + " : Se téléporter à l'île du joueur.");
				sender.sendMessage(ChatColor.RED + "/sky <player> delete" + ChatColor.WHITE + " : Supprimer définitivement l'île du joueur.");
				sender.sendMessage(ChatColor.RED + "/sky <player> create" + ChatColor.WHITE + " : Créer un île au joueur1.");
				sender.sendMessage(ChatColor.RED + "/sky <player> restart" + ChatColor.WHITE + " : Supprimer et recréer un île au joueur.");				
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.RED + "Commandes op Skyblock : ");
					sender.sendMessage(ChatColor.RED + "/sky help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
					sender.sendMessage(ChatColor.RED + "/sky lastisland" + ChatColor.WHITE + " : Voir la dernière île créée.");
					sender.sendMessage(ChatColor.RED + "/sky world" + ChatColor.WHITE + " : Obtenir le nom du monde skyblock.");
					sender.sendMessage(ChatColor.RED + "/sky createcommunisland" + ChatColor.WHITE + " : Crée une île commune.");
					sender.sendMessage(ChatColor.RED + "/sky definelevels" + ChatColor.WHITE + " : Calcule le niveau de chaque île.");
					sender.sendMessage(ChatColor.RED + "/sky <player> info" + ChatColor.WHITE + " : Obtenir des informations sur un joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> tp" + ChatColor.WHITE + " : Se téléporter à l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> delete" + ChatColor.WHITE + " : Supprimer définitivement l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> create" + ChatColor.WHITE + " : Créer un île au joueur1.");
					sender.sendMessage(ChatColor.RED + "/sky <player> restart" + ChatColor.WHITE + " : Supprimer et recréer un île au joueur.");
				}
				if(args[0].equalsIgnoreCase("lastisland")) {
					Island last = plugin.getLastIsland();
					sender.sendMessage("Coordonnées de la dernière île créée : x=" + ChatColor.BLUE + last.x + ChatColor.WHITE + ", z=" + ChatColor.BLUE + last.z);
				}
				if(args[0].equalsIgnoreCase("world")) {
					sender.sendMessage("Nom du monde skyblock : " + ChatColor.BLUE + plugin.getworldname());
				}
				if(args[0].equalsIgnoreCase("createcommunisland")) {
					String name = "Commune";
					int number = 0;
					while(plugin.hasIsland(name + number))
						number ++;
					createNewCommunIsland(name + number);
					
					Island island = plugin.getPlayerIsland(name + number);
					Player player = (Player) sender;
					plugin.teleportIsland(player, name + number);
					List<Entity> Entities = player.getNearbyEntities(15,15,15);
		    		Iterator<Entity> ent = Entities.iterator();
		    		while (ent.hasNext())
		    			ent.next().remove();
					sender.sendMessage("Ile " + ChatColor.BLUE + name + number + ChatColor.WHITE + " créée aux coordonnées x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.z + ChatColor.WHITE + ".");
					
				}
				if(args[0].equalsIgnoreCase("definelevels")) {
					sender.sendMessage("Calcul du niveau des îles...");
					plugin.defineLevel();
					sender.sendMessage("Le niveau des îles a bien été mis à jour.");
				}
				
			} else if(args.length == 2) {
				
				String playername = args[0];
				
				if(args[1].equalsIgnoreCase("info")) {
					if(plugin.hasIsland(playername)) {
						Island island = plugin.getPlayerIsland(playername);
						sender.sendMessage("Le joueur " + ChatColor.BLUE + playername + ChatColor.WHITE + " a une île aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.z);
					} else {
						islandNotFound(sender, playername);
					}
				}
				if(args[1].equalsIgnoreCase("tp")) {
					if(plugin.hasIsland(playername)) {
						Island island = plugin.getPlayerIsland(playername);
						sender.sendMessage("Téléportation à l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.z);
						Player player = (Player) sender;
						plugin.teleportIsland(player, playername);
					} else {
						islandNotFound(sender, playername);
					}
				}
				if(args[1].equalsIgnoreCase("delete") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.hasIsland(playername)) {
						Island island = plugin.getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.z);
						plugin.deleteIsland(playername, plugin.getServer().getWorld(plugin.getworldname()));
						if(plugin.getServer().getPlayer(playername) != null) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.BLUE + sender.getName() + ChatColor.RED + ".");
					} else {
						islandNotFound(sender, playername);
					}
				}
				if(args[1].equalsIgnoreCase("create")) {
					if(plugin.hasIsland(playername)) {
						Island island = plugin.getPlayerIsland(playername);
						sender.sendMessage(ChatColor.BLUE + playername + ChatColor.RED + " a déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.RED + ", z=" + ChatColor.BLUE + island.z + ChatColor.RED + ".");
					} else {
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							Island island = plugin.getPlayerIsland(playername);
							plugin.teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.z + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					}
				}
				if(args[1].equalsIgnoreCase("restart") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.hasIsland(playername)) {
						Island island = plugin.getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.z);
						plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.BLUE + sender.getName() + ChatColor.RED + ".");
						plugin.deleteIsland(playername, plugin.getServer().getWorld(plugin.getworldname()));
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							island = plugin.getPlayerIsland(playername);
							plugin.teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.BLUE + island.x + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.z + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					} else {
						islandNotFound(sender, playername);
					}
				}
			}
		}
		
		return false;
	}


	private void islandNotFound(CommandSender sender, String playername) {
		sender.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername + ChatColor.RED + " n'a pas d'île.");
	}
	
	//Création de l'île
private boolean createNewIsland(Player player) {
		
		//Si le joueur a déjà commencé une île
		if(plugin.hasIsland(player.getName()))
		{
			Island island = plugin.getPlayerIsland(player.getName());
			player.sendMessage(ChatColor.RED + "Vous avez déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.RED + ", z=" + ChatColor.BLUE + island.z);
			player.sendMessage(ChatColor.RED + "Pour recommencer une île, tapez /is restart");
			return false;
		}
		
		Island island;
		Island last = plugin.getLastIsland();
		//if we have space because of a deleted Island, create one there
		if(plugin.hasOrphanedIsland()) {
			island = plugin.getOrphanedIsland();
			//sender.sendMessage(ChatColor.RED + "Une île est libre aux coorodonnées : x=" + ChatColor.BLUE + island.x + ChatColor.RED + ", z=" + ChatColor.BLUE + island.z);
		} else {
            island = nextIslandLocation(last);
            plugin.setLastIsland(island);
            while (plugin.getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.setLastIsland(island);
            }
		}
		
		createIslandBlocks(island, plugin.getServer().getWorld(plugin.getworldname()));
		plugin.registerPlayerIsland(player.getName(), island);
		plugin.teleportIsland(player);
		plugin.createHome(player, new Location(plugin.getServer().getWorld(plugin.getworldname()), island.x, plugin.getISLANDS_Y(), island.z));
		player.getInventory().clear();
		
		player.sendMessage(ChatColor.GOLD + "Une île vous a été créée aux coordonnées : x=" + ChatColor.BLUE + island.x + ChatColor.GOLD + ", z=" + ChatColor.BLUE + island.z + ChatColor.GOLD + " !");

		return true;
	}
	
	//Détermine les coordonnées de l'île par rapport aux coordonnées de la précédente
	private Island nextIslandLocation(Island lastIsland) {
		
		int x = lastIsland.x;
		int z = lastIsland.z;
		Island nextPos = new Island();
		nextPos.x = x;
		nextPos.z = z;
		if ( x < z )
		{
	        if ( ((-1) * x) < z)
	        {
	           nextPos.x = nextPos.x + plugin.getISLAND_SPACING();
	           return nextPos;
	        }
	        nextPos.z = nextPos.z + plugin.getISLAND_SPACING();
	        return nextPos;
	    }
	    if( x > z )
	    {
	        if ( ((-1) * x) >= z)
	        {
	            nextPos.x = nextPos.x - plugin.getISLAND_SPACING();
	            return nextPos;
	        }
	            nextPos.z = nextPos.z - plugin.getISLAND_SPACING();
	            return nextPos;
	    }
	    if ( x <= 0 )
	    {
	    	nextPos.z = nextPos.z + plugin.getISLAND_SPACING();
	        return nextPos;
	    }
	    nextPos.z = nextPos.z - plugin.getISLAND_SPACING();
	    return nextPos;
	}
	
private void createNewCommunIsland(String name) {
		
		Island island;
		Island last = plugin.getLastIsland();

		if(plugin.hasOrphanedIsland()) {
			island = plugin.getOrphanedIsland();
		} else {
            island = nextIslandLocation(last);
            plugin.setLastIsland(island);
            while (plugin.getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.setLastIsland(island);
            }
		}
		
		createIslandBlocks(island, plugin.getServer().getWorld(plugin.getworldname()));
		plugin.registerPlayerIsland(name, island);
	}
	
	//Création des blocs de l'île
	private void createIslandBlocks(Island island, World world) {
		
		int x = island.x;
		int y = 64;
		int z = island.z;
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

}
