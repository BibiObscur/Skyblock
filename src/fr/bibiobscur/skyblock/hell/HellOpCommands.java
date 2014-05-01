package fr.bibiobscur.skyblock.hell;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.bibiobscur.skyblock.Island;
import fr.bibiobscur.skyblock.Plugin;

public class HellOpCommands implements CommandExecutor {
	
	public final Plugin plugin;
	
    public HellOpCommands(Plugin plugin) {
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
		
		if(commandLabel.equalsIgnoreCase("skyhell"))
		{
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Commandes op Skyblock : ");
				sender.sendMessage(ChatColor.RED + "/skyhell help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
				sender.sendMessage(ChatColor.RED + "/skyhell lastisland" + ChatColor.WHITE + " : Voir la dernière île créée.");
				sender.sendMessage(ChatColor.RED + "/skyhell world" + ChatColor.WHITE + " : Obtenir le nom du monde skyblock.");
				sender.sendMessage(ChatColor.RED + "/skyhell addOrphaned <x> <z>" + ChatColor.WHITE + " : Rajoute une parcelle disponible.");
				sender.sendMessage(ChatColor.RED + "/skyhell <player> info" + ChatColor.WHITE + " : Obtenir des informations sur un joueur.");
				sender.sendMessage(ChatColor.RED + "/skyhell <player> tp" + ChatColor.WHITE + " : Se téléporter à l'île du joueur.");
				sender.sendMessage(ChatColor.RED + "/skyhell <player> delete" + ChatColor.WHITE + " : Supprimer définitivement l'île du joueur.");
				sender.sendMessage(ChatColor.RED + "/skyhell <player> create" + ChatColor.WHITE + " : Créer un île au joueur1.");
				sender.sendMessage(ChatColor.RED + "/skyhell <player> restart" + ChatColor.WHITE + " : Supprimer et recréer un île au joueur.");				
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.RED + "Commandes op Skyblock : ");
					sender.sendMessage(ChatColor.RED + "/skyhell help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
					sender.sendMessage(ChatColor.RED + "/skyhell lastisland" + ChatColor.WHITE + " : Voir la dernière île créée.");
					sender.sendMessage(ChatColor.RED + "/skyhell world" + ChatColor.WHITE + " : Obtenir le nom du monde skyblock.");
					sender.sendMessage(ChatColor.RED + "/skyhell addOrphaned <x> <z>" + ChatColor.WHITE + " : Rajoute une parcelle disponible.");
					sender.sendMessage(ChatColor.RED + "/skyhell <player> info" + ChatColor.WHITE + " : Obtenir des informations sur un joueur.");
					sender.sendMessage(ChatColor.RED + "/skyhell <player> tp" + ChatColor.WHITE + " : Se téléporter à l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/skyhell <player> delete" + ChatColor.WHITE + " : Supprimer définitivement l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/skyhell <player> create" + ChatColor.WHITE + " : Créer un île au joueur1.");
					sender.sendMessage(ChatColor.RED + "/skyhell <player> restart" + ChatColor.WHITE + " : Supprimer et recréer un île au joueur.");
				}
				if(args[0].equalsIgnoreCase("lastisland")) {
					Island last = plugin.getHellDatas().getLastIsland();
					sender.sendMessage("Coordonnées de la dernière île créée : x=" + ChatColor.BLUE + last.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + last.getZ());
				}
				if(args[0].equalsIgnoreCase("world")) {
					sender.sendMessage("Nom du monde skyblock : " + ChatColor.BLUE + plugin.getHellDatas().getworldname());
				}
				
				/*if(args[0].equalsIgnoreCase("islist")) {
					Player player = (Player) sender;
					plugin.getDatas().afficherIslandList(player);
				}*/
				
			} else if(args.length == 2) {
				
				String playername = args[0];
				
				if(args[1].equalsIgnoreCase("info")) {
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
						sender.sendMessage(ChatColor.WHITE + "Informations sur l'île de " + ChatColor.RED + playername + ChatColor.WHITE + " :");
						sender.sendMessage(ChatColor.WHITE + "Coordonnées : x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.RED + island.getZ() + ChatColor.WHITE + ".");
						sender.sendMessage(ChatColor.WHITE + "Niveau : " + ChatColor.RED + island.getLevel() + ChatColor.WHITE + ".");
						sender.sendMessage(ChatColor.WHITE + "Challenges accomplis : " + ChatColor.WHITE + "(" + island.getChallenges().size() + ") " + island.getChallengeList());
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("tp")) {
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
						sender.sendMessage("Téléportation à l'île de " + ChatColor.RED + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.RED + island.getZ());
						Player player = (Player) sender;
						plugin.getDatas().teleportIsland(player, playername);
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("delete") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.RED + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.RED + island.getZ());
						plugin.getHellDatas().deleteIsland(playername, plugin.getServer().getWorld(plugin.getworldname()));
						if(plugin.getServer().getPlayer(playername) != null) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.BLUE + sender.getName() + ChatColor.RED + ".");
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("create")) {
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
						sender.sendMessage(ChatColor.BLUE + playername + ChatColor.RED + " a déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ() + ChatColor.RED + ".");
					} else {
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							Island island = plugin.getHellDatas().getPlayerIsland(playername);
							plugin.getHellDatas().teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.RED + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.RED + island.getZ() + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					}
				}
				
				if(args[1].equalsIgnoreCase("restart") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.RED + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.RED + island.getZ());
						plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.RED + sender.getName() + ChatColor.RED + ".");
						plugin.getHellDatas().deleteIsland(playername, plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							island = plugin.getHellDatas().getPlayerIsland(playername);
							plugin.getHellDatas().teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.RED + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.RED + island.getX() + ChatColor.WHITE + ", y=" + ChatColor.RED + island.getZ() + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					} else {
						islandNotFound(sender, playername);
					}
				}
			} else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("addOrphaned")) {
					int x = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);
					plugin.getHellDatas().addOrphanedIsland(x, z);
					sender.sendMessage("Parcelle ajoutées aux coordonnées " + x + " " + z + ".");
				}
			}
		}
		
		return false;
	}


	private void islandNotFound(CommandSender sender, String playername) {
		sender.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.RED + playername + ChatColor.RED + " n'a pas d'île.");
	}
	
	//Création de l'île
private boolean createNewIsland(Player player) {
		
		//Si le joueur a déjà commencé une île
		if(plugin.getDatas().hasIsland(player.getName()))
		{
			Island island = plugin.getDatas().getPlayerIsland(player.getName());
			player.sendMessage(ChatColor.RED + "Vous avez déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ());
			player.sendMessage(ChatColor.RED + "Pour recommencer une île, tapez /is restart");
			return false;
		}
		
		Island island;
		Island last = plugin.getHellDatas().getLastIsland();
		//if we have space because of a deleted Island, create one there
		if(plugin.getHellDatas().hasOrphanedIsland()) {
			island = plugin.getHellDatas().getOrphanedIsland();
			//sender.sendMessage(ChatColor.RED + "Une île est libre aux coorodonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ());
		} else {
            island = nextIslandLocation(last);
            plugin.getHellDatas().setLastIsland(island);
            while (plugin.getHellDatas().getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.getHellDatas().setLastIsland(island);
            }
		}
		
		island.build(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
		
		if(plugin.getDatas().hasGroup(player.getName())) {
			if(plugin.getDatas().isLeader(player.getName())) {
				plugin.getHellDatas().addPlayerIsland(player.getName(), island);
			} else
				plugin.getHellDatas().addPlayerIsland(plugin.getDatas().getGroup(player.getName()).getLeader(), island);
		} else
			plugin.getHellDatas().addPlayerIsland(player.getName(), island);
		
		plugin.getHellDatas().teleportIsland(player);
		plugin.getHellDatas().createHome(player, new Location(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()), island.getX(), plugin.getISLANDS_Y(), island.getZ()));
		player.getInventory().clear();
		
		player.sendMessage(ChatColor.GREEN + "Une île vous a été créée aux coordonnées : x=" + ChatColor.RED + island.getX() + ChatColor.GREEN + ", z=" + ChatColor.RED + island.getZ() + ChatColor.GREEN + " !");

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
	
	/*private void createNewCommunIsland(String name) {
		
		Island island;
		Island last = plugin.getHellDatas().getLastIsland();

		if(plugin.getHellDatas().hasOrphanedIsland()) {
			island = plugin.getHellDatas().getOrphanedIsland();
		} else {
            island = nextIslandLocation(last);
            plugin.getHellDatas().setLastIsland(island);
            while (plugin.getHellDatas().getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.getHellDatas().setLastIsland(island);
            }
		}
		
		island.build(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
		plugin.getHellDatas().addPlayerIsland(name, island);
	}*/
}
