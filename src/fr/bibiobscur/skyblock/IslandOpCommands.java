package fr.bibiobscur.skyblock;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
				sender.sendMessage(ChatColor.RED + "/sky save" + ChatColor.WHITE + " : Sauvegarde des données du skyblock.");
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
					sender.sendMessage(ChatColor.RED + "/sky save" + ChatColor.WHITE + " : Sauvegarde des données du skyblock.");
					sender.sendMessage(ChatColor.RED + "/sky <player> info" + ChatColor.WHITE + " : Obtenir des informations sur un joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> tp" + ChatColor.WHITE + " : Se téléporter à l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> delete" + ChatColor.WHITE + " : Supprimer définitivement l'île du joueur.");
					sender.sendMessage(ChatColor.RED + "/sky <player> create" + ChatColor.WHITE + " : Créer un île au joueur1.");
					sender.sendMessage(ChatColor.RED + "/sky <player> restart" + ChatColor.WHITE + " : Supprimer et recréer un île au joueur.");
				}
				if(args[0].equalsIgnoreCase("lastisland")) {
					Island last = plugin.getDatas().getLastIsland();
					sender.sendMessage("Coordonnées de la dernière île créée : x=" + ChatColor.BLUE + last.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + last.getZ());
				}
				if(args[0].equalsIgnoreCase("world")) {
					sender.sendMessage("Nom du monde skyblock : " + ChatColor.BLUE + plugin.getworldname());
				}
				if(args[0].equalsIgnoreCase("createcommunisland")) {
					String name = "Commune";
					int number = 0;
					while(plugin.getDatas().hasIsland(name + number))
						number ++;
					createNewCommunIsland(name + number);
					
					Island island = plugin.getDatas().getPlayerIsland(name + number);
					Player player = (Player) sender;
					plugin.getDatas().teleportIsland(player, name + number);
					List<Entity> Entities = player.getNearbyEntities(15,15,15);
		    		Iterator<Entity> ent = Entities.iterator();
		    		while (ent.hasNext())
		    			ent.next().remove();
					sender.sendMessage("Ile " + ChatColor.BLUE + name + number + ChatColor.WHITE + " créée aux coordonnées x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.getZ() + ChatColor.WHITE + ".");
					
				}
				if(args[0].equalsIgnoreCase("definelevels")) {
					sender.sendMessage("Calcul du niveau des îles...");
					plugin.getDatas().defineLevel();
					sender.sendMessage("Le niveau des îles a bien été mis à jour.");
				}
				
				if(args[0].equalsIgnoreCase("save")) {
					sender.sendMessage("Sauvegarde...");
					plugin.saveDatas();
					sender.sendMessage("Sauvegarde terminée.");
				}
				
				if(args[0].equalsIgnoreCase("mlimit")) {
					sender.sendMessage("Monster spawn limit : " + plugin.getServer().getWorld(plugin.getworldname()).getMonsterSpawnLimit());
				}
				
				if(args[0].equalsIgnoreCase("islist")) {
					Player player = (Player) sender;
					plugin.getDatas().afficherIslandList(player);
				}
				
				if(args[0].equalsIgnoreCase("grouplist")) {
					Player player = (Player) sender;
					plugin.getDatas().afficherGroupList(player);
				}
				
			} else if(args.length == 2) {
				
				String playername = args[0];
				
				if(args[1].equalsIgnoreCase("info")) {
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage(ChatColor.WHITE + "Informations sur l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " :");
						sender.sendMessage(ChatColor.WHITE + "Coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.getZ() + ChatColor.WHITE + ".");
						sender.sendMessage(ChatColor.WHITE + "Niveau : " + ChatColor.BLUE + island.getLevel() + ChatColor.WHITE + ".");
						sender.sendMessage(ChatColor.WHITE + "Challenges accomplis : " + ChatColor.WHITE + "(" + island.getChallenges().size() + ") " + island.getChallengeList());
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("tp")) {
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage("Téléportation à l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.getZ());
						Player player = (Player) sender;
						plugin.getDatas().teleportIsland(player, playername);
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("delete") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.getZ());
						plugin.getDatas().deleteIsland(playername, plugin.getServer().getWorld(plugin.getworldname()));
						if(plugin.getServer().getPlayer(playername) != null) plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.BLUE + sender.getName() + ChatColor.RED + ".");
					} else {
						islandNotFound(sender, playername);
					}
				}
				
				if(args[1].equalsIgnoreCase("create")) {
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage(ChatColor.BLUE + playername + ChatColor.RED + " a déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ() + ChatColor.RED + ".");
					} else {
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							Island island = plugin.getDatas().getPlayerIsland(playername);
							plugin.getDatas().teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.getZ() + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					}
				}
				
				if(args[1].equalsIgnoreCase("restart") && sender.hasPermission("skyblock.devcommand")) {
					if(plugin.getDatas().hasIsland(playername)) {
						Island island = plugin.getDatas().getPlayerIsland(playername);
						sender.sendMessage("Suppression de l'île de " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", z=" + ChatColor.BLUE + island.getZ());
						plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement par " + ChatColor.BLUE + sender.getName() + ChatColor.RED + ".");
						plugin.getDatas().deleteIsland(playername, plugin.getServer().getWorld(plugin.getworldname()));
						if(createNewIsland(plugin.getServer().getPlayer(playername))){
							island = plugin.getDatas().getPlayerIsland(playername);
							plugin.getDatas().teleportIsland(plugin.getServer().getPlayer(playername));
				    		List<Entity> Entities = plugin.getServer().getPlayer(playername).getNearbyEntities(15,15,15);
				    		Iterator<Entity> ent = Entities.iterator();
				    		while (ent.hasNext())
				    			ent.next().remove();
							sender.sendMessage("Ile créée pour " + ChatColor.BLUE + playername + ChatColor.WHITE + " aux coordonnées x=" + ChatColor.BLUE + island.getX() + ChatColor.WHITE + ", y=" + ChatColor.BLUE + island.getZ() + ChatColor.WHITE + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "Erreur lors de la création de l'île.");
						}
					} else {
						islandNotFound(sender, playername);
					}
				}
			}/* else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("group")) {
					String playername = args[1];
					if(args[2].equalsIgnoreCase("delete")) {
						if(plugin.getDatas().hasGroup(playername)) {
							plugin.getDatas().removeGroup(playername);
							sender.sendMessage("Ile supprime.");
						} else
							sender.sendMessage(playername + " n'a pas d'île.");
					}
				}
			} else if(args.length == 4) {
				if(args[0].equalsIgnoreCase("group")) {
					String playername = args[1];
					if(args[2].equalsIgnoreCase("addMember")) {
						String playername2 = args[3];
						plugin.getDatas().getGroup(playername).getMembers().add(playername2);
						//plugin.getDatas().addPlayerToGroup(playername, playername2);
					}
				}
			}*/
		}
		
		return false;
	}


	private void islandNotFound(CommandSender sender, String playername) {
		sender.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername + ChatColor.RED + " n'a pas d'île.");
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
		
		island.build(plugin.getServer().getWorld(plugin.getworldname()));
		plugin.getDatas().addPlayerIsland(player.getName(), island);
		plugin.getDatas().teleportIsland(player);
		plugin.getDatas().createHome(player, new Location(plugin.getServer().getWorld(plugin.getworldname()), island.getX(), plugin.getISLANDS_Y(), island.getZ()));
		player.getInventory().clear();
		
		player.sendMessage(ChatColor.GOLD + "Une île vous a été créée aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.GOLD + ", z=" + ChatColor.BLUE + island.getZ() + ChatColor.GOLD + " !");

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
	
	private void createNewCommunIsland(String name) {
		
		Island island;
		Island last = plugin.getDatas().getLastIsland();

		if(plugin.getDatas().hasOrphanedIsland()) {
			island = plugin.getDatas().getOrphanedIsland();
		} else {
            island = nextIslandLocation(last);
            plugin.getDatas().setLastIsland(island);
            while (plugin.getDatas().getPlayers().containsValue(island))
            {
            	island = nextIslandLocation(island);
                plugin.getDatas().setLastIsland(island);
            }
		}
		
		island.build(plugin.getServer().getWorld(plugin.getworldname()));
		plugin.getDatas().addPlayerIsland(name, island);
	}
}
