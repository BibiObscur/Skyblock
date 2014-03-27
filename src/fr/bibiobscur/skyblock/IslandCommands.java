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

public class IslandCommands implements CommandExecutor {

	public final Plugin plugin;
	
    public IslandCommands(Plugin plugin) {
        this.plugin = plugin;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
            return false;
        }

		Player player = (Player) sender;
		
		/*if(!player.hasPermission("skyblock.use")) {
        	player.sendMessage(ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
        	return true;
        }*/
		
		if(plugin.getworldname().isEmpty()){
			player.sendMessage(ChatColor.RED + "Le monde skyblock n'a pas encore été défini.");
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("is"))
		{
			if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("home")))
			{
				if(plugin.getDatas().hasHome(player.getName())) {
					sender.sendMessage("Téléportation à votre île.");
					plugin.getDatas().teleportHome(player);
					return true;
				} else if(plugin.getDatas().hasIsland(player.getName())) {
					sender.sendMessage("Téléportation à votre île.");
					plugin.getDatas().teleportIsland(player);
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Vous n'avez pas de home. Pour créer un home, tapez /is sethome.");
				}
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
				player.teleport(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation());
				return true;
			}

			if(!player.getWorld().getName().equalsIgnoreCase(plugin.getworldname()))
			{
				player.sendMessage(ChatColor.RED + "Tu dois être sur la map " + ChatColor.BLUE + plugin.getworldname() + ChatColor.RED + " pour utiliser cette commande.");
	        	return true;
			}
			
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					sender.sendMessage(ChatColor.RED + "Commandes Skyblock : ");
					sender.sendMessage(ChatColor.RED + "/is" + ChatColor.WHITE + " : Se téléporter à son île");
					sender.sendMessage(ChatColor.RED + "/is spawn" + ChatColor.WHITE + " : Se téléporter au spawn du monde skyblock");
					sender.sendMessage(ChatColor.RED + "/is create" + ChatColor.WHITE + " : Créer une île.");
					sender.sendMessage(ChatColor.RED + "/is delete" + ChatColor.WHITE + " : Supprimer son île.");
					sender.sendMessage(ChatColor.RED + "/is restart" + ChatColor.WHITE + " : Détruire et recommencer son île.");
					sender.sendMessage(ChatColor.RED + "/is sethome" + ChatColor.WHITE + " : Définir un point de téléportation sur son île.");
					sender.sendMessage(ChatColor.RED + "/is info" + ChatColor.WHITE + " : Afficher des informations votre île.");
					sender.sendMessage(ChatColor.RED + "/is infohere" + ChatColor.WHITE + " : Afficher des informations sur l'île sur laquelle vous êtes.");
					sender.sendMessage(ChatColor.RED + "/is info <player>" + ChatColor.WHITE + " : Afficher des informations sur l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/isg" + ChatColor.WHITE + " : Commandes pour créer une île en coopération.");
					}
				
				if(args[0].equalsIgnoreCase("create"))
				{
					//Création de l'île
					if(createNewIsland(player)){
						plugin.getDatas().teleportIsland(player);
			    		List<Entity> Entities = player.getNearbyEntities(15,15,15);
			    		Iterator<Entity> ent = Entities.iterator();
			    		while (ent.hasNext())
			    			ent.next().remove();
					} else {
						sender.sendMessage(ChatColor.RED + "Votre île n'a pas été créée.");
					}
				}
				
				if(args[0].equalsIgnoreCase("restart") || args[0].equalsIgnoreCase("reset"))
				{
					//Suppression de l'île
					plugin.getDatas().deleteIsland(player.getName(), plugin.getServer().getWorld(plugin.getworldname()));
					
					//Création de l'île
					if(createNewIsland(player)){
						plugin.getDatas().teleportIsland(player);
			    		List<Entity> Entities = player.getNearbyEntities(15,15,15);
			    		Iterator<Entity> ent = Entities.iterator();
			    		while (ent.hasNext())
			    			ent.next().remove();
					} else {
						sender.sendMessage(ChatColor.RED + "Votre île n'a pas été créée.");
					}
				}
				
				if(args[0].equalsIgnoreCase("delete"))
				{
					//Suppression de l'île
					if(plugin.getDatas().hasIsland(player.getName())) {
						plugin.getDatas().deleteIsland(player.getName(), plugin.getServer().getWorld(plugin.getworldname()));
						sender.sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement.");
					} else {
						sender.sendMessage(ChatColor.RED + "Vous n'avez pas d'île.");
					}
				}
				
				if(args[0].equalsIgnoreCase("sethome"))
				{
					if(plugin.getDatas().createHome(player, player.getLocation())){
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						sender.sendMessage("Home set aux coordonnées : " + x + ", " + y + ", " + z);
					} else {
						sender.sendMessage("sethome annulé.");
					}
				}
				
				if(args[0].equalsIgnoreCase("infohere")) {
					if(plugin.getDatas().getHostHere(player.getLocation()) != null) {
						sender.sendMessage("Vous êtes sur l'île de " + ChatColor.BLUE + plugin.getDatas().getHostHere(player.getLocation()));
						sender.sendMessage(ChatColor.RED + "Coordonnées : x=" + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(plugin.getDatas().getHostHere(player.getLocation())).getX() + ChatColor.RED + ", z=" + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(plugin.getDatas().getHostHere(player.getLocation())).getZ() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Niveau : " + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(plugin.getDatas().getHostHere(player.getLocation())).getLevel() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Challenges accomplis : " + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(plugin.getDatas().getHostHere(player.getLocation())).getChallenges().size() + ChatColor.RED + ".");
					} else {
						sender.sendMessage(ChatColor.RED + "Vous n'êtes pas sur une île.");
					}
				}
				
				if(args[0].equalsIgnoreCase("info")) {
					if(plugin.getDatas().hasIsland(player.getName())) {
						Island island = plugin.getDatas().getPlayerIsland(player.getName());
						sender.sendMessage(ChatColor.RED + "Informations sur l'île de " + ChatColor.BLUE + player.getName() + ChatColor.RED + " :");
						sender.sendMessage(ChatColor.RED + "Coordonnées : x=" + ChatColor.WHITE + island.getX() + ChatColor.RED + ", z=" + ChatColor.WHITE + island.getZ() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Niveau : " + ChatColor.WHITE + island.getLevel() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Challenges accomplis : " + ChatColor.WHITE + "(" + island.getChallenges().size() + ") " + island.getChallengeList());
						
					} else {
						sender.sendMessage(ChatColor.RED + "Vous n'avez pas d'île.");
					}
				}
			}
			
			if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("info")) {
					String playername = args[1];
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage("Informations sur l'île de " + ChatColor.BLUE + playername + ChatColor.RED + " :");
						sender.sendMessage(ChatColor.RED + "Coordonnées : x=" + ChatColor.WHITE + island.getX() + ChatColor.RED + ", z=" + ChatColor.WHITE + island.getZ() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Niveau : " + ChatColor.WHITE + island.getLevel() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Challenges accomplis : " + ChatColor.WHITE + island.getChallenges().size() + ChatColor.RED + ".");
					} else {
						sender.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername + ChatColor.RED + " n'a pas d'île.");
					}
				}
			}
		}
		
		
		return false;
	}
	
	//Création de l'île
	private boolean createNewIsland(CommandSender sender) {
		
		Player player = (Player) sender;
		
		//Si le joueur a déjà commencé une île
		if(plugin.getDatas().hasIsland(player.getName()))
		{
			Island island = plugin.getDatas().getPlayerIsland(player.getName());
			sender.sendMessage(ChatColor.RED + "Vous avez déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ());
			sender.sendMessage(ChatColor.RED + "Pour recommencer une île, tapez /is restart");
			return false;
		}
		
		Island island;
		Island last = plugin.getDatas().getLastIsland();
		//if we have space because of a deleted Island, create one there
		if(plugin.getDatas().hasOrphanedIsland()) {
			island = plugin.getDatas().getOrphanedIsland();
			//sender.sendMessage(ChatColor.RED + "Une île est libre aux coorodonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ());
		} else {
            island = nextIslandLocation(last);
            plugin.getDatas().setLastIsland(island);
            while (plugin.getDatas().getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.getDatas().setLastIsland(island);
            }
		}
		
		createIslandBlocks(island, player, plugin.getServer().getWorld(plugin.getworldname()));
		plugin.getDatas().addPlayerIsland(player.getName(), island);
		plugin.getDatas().teleportIsland(player);
		plugin.getDatas().createHome(player, new Location(plugin.getServer().getWorld(plugin.getworldname()), island.getX(), plugin.getISLANDS_Y(), island.getZ()));
		player.getInventory().clear();
		
		sender.sendMessage(ChatColor.GOLD + "Ile créée avec succès aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.GOLD + ", z=" + ChatColor.BLUE + island.getZ() + ChatColor.GOLD + " !");

		return true;
	}
	
	//Détermine les coordonnées de l'île par rapport aux coordonnées de la précédente
	private Island nextIslandLocation(Island lastIsland) {
		
		int x = lastIsland.getX();
		int z = lastIsland.getZ();
		Island nextPos = new Island();
		nextPos.setX(x);
		nextPos.setZ(z);
		if ( x < z )
		{
	        if ( ((-1) * x) < z)
	        {
	           nextPos.setX(nextPos.getX() + plugin.getISLAND_SPACING());
	           return nextPos;
	        }
	        nextPos.setZ(nextPos.getZ() + plugin.getISLAND_SPACING());
	        return nextPos;
	    }
	    if( x > z )
	    {
	        if ( ((-1) * x) >= z)
	        {
	            nextPos.setX(nextPos.getX() - plugin.getISLAND_SPACING());
	            return nextPos;
	        }
	            nextPos.setZ(nextPos.getZ() - plugin.getISLAND_SPACING());
	            return nextPos;
	    }
	    if ( x <= 0 )
	    {
	    	nextPos.setZ(nextPos.getZ() + plugin.getISLAND_SPACING());
	        return nextPos;
	    }
	    nextPos.setZ(nextPos.getZ() - plugin.getISLAND_SPACING());
	    return nextPos;
	}
	
	//Création des blocs de l'île
	private void createIslandBlocks(Island island, Player player, World world) {
		
		int x = island.getX();
		int y = 64;
		int z = island.getZ();
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
