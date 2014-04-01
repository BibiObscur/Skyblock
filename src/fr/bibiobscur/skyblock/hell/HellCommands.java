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

public class HellCommands implements CommandExecutor {
	
	private final Plugin plugin;
	
	public HellCommands(Plugin plugin) {
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
		
		if(plugin.getHellDatas().getworldname().isEmpty()){
			player.sendMessage(ChatColor.RED + "Le monde hellblock n'a pas encore été défini.");
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("ishell"))
		{

			if(!player.getWorld().getName().equalsIgnoreCase(plugin.getHellDatas().getworldname()))
			{
				player.sendMessage(ChatColor.RED + "Tu dois être sur la map " + ChatColor.BLUE + plugin.getHellDatas().getworldname() + ChatColor.RED + " pour utiliser cette commande.");
	        	return true;
			}
			
			if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("home")))
			{
				if(plugin.getHellDatas().hasHome(player.getName())) {
					sender.sendMessage("Téléportation à votre île.");
					plugin.getHellDatas().teleportHome(player);
					return true;
				} else if(plugin.getHellDatas().hasIsland(player.getName())) {
					sender.sendMessage("Téléportation à votre île.");
					plugin.getHellDatas().teleportIsland(player);
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Vous n'avez pas de home. Pour créer un home, tapez /ishell sethome.");
				}
			}

			if(args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
				player.teleport(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getSpawnLocation());
				return true;
			}
			
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					sender.sendMessage(ChatColor.RED + "Commandes Hellblock : ");
					sender.sendMessage(ChatColor.RED + "/ishell" + ChatColor.WHITE + " : Se téléporter à son île");
					sender.sendMessage(ChatColor.RED + "/ishell create" + ChatColor.WHITE + " : Créer une île.");
					sender.sendMessage(ChatColor.RED + "/ishell delete" + ChatColor.WHITE + " : Supprimer son île.");
					sender.sendMessage(ChatColor.RED + "/ishell restart" + ChatColor.WHITE + " : Détruire et recommencer son île.");
					sender.sendMessage(ChatColor.RED + "/ishell sethome" + ChatColor.WHITE + " : Définir un point de téléportation sur son île.");
					sender.sendMessage(ChatColor.RED + "/ishell info" + ChatColor.WHITE + " : Afficher des informations votre île.");
					sender.sendMessage(ChatColor.RED + "/ishell infohere" + ChatColor.WHITE + " : Afficher des informations sur l'île sur laquelle vous êtes.");
					sender.sendMessage(ChatColor.RED + "/ishell info <player>" + ChatColor.WHITE + " : Afficher des informations sur l'île du joueur.");
				}
				
				if(args[0].equalsIgnoreCase("create"))
				{
					if(createNewIsland(player)){
						plugin.getHellDatas().teleportIsland(player);
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
					if(plugin.getDatas().hasGroup(player.getName())) {
						if(!plugin.getDatas().isLeader(player.getName())) {
							player.sendMessage(ChatColor.RED + "Vous devez être le leader de votre groupe pour faire ceci.");
							return false;
						}
					}
					
					//Suppression de l'île
					plugin.getHellDatas().deleteIsland(player.getName(), plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
					
					//Création de l'île
					if(createNewIsland(player)){
						plugin.getHellDatas().teleportIsland(player);
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
					if(plugin.getDatas().hasGroup(player.getName())) {
						if(!plugin.getDatas().isLeader(player.getName())) {
							player.sendMessage(ChatColor.RED + "Vous devez être le leader de votre groupe pour faire ceci.");
							return false;
						}
					}
					
					//Suppression de l'île
					if(plugin.getHellDatas().hasIsland(player.getName())) {
						plugin.getHellDatas().deleteIsland(player.getName(), plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
						sender.sendMessage(ChatColor.RED + "Votre île a été supprimée définitivement.");
					} else {
						sender.sendMessage(ChatColor.RED + "Vous n'avez pas d'île.");
					}
				}
				
				if(args[0].equalsIgnoreCase("sethome"))
				{
					if(plugin.getHellDatas().createHome(player, player.getLocation())){
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						sender.sendMessage("Home set aux coordonnées : " + x + ", " + y + ", " + z);
					} else {
						sender.sendMessage("sethome annulé.");
					}
				}
				
				if(args[0].equalsIgnoreCase("infohere")) {
					if(plugin.getHellDatas().getHostHere(player.getLocation()) != null) {
						sender.sendMessage("Vous êtes sur l'île de " + ChatColor.BLUE + plugin.getHellDatas().getHostHere(player.getLocation()));
						sender.sendMessage(ChatColor.RED + "Coordonnées : x=" + ChatColor.WHITE + plugin.getHellDatas().getPlayerIsland(plugin.getHellDatas().getHostHere(player.getLocation())).getX() + ChatColor.RED + ", z=" + ChatColor.WHITE + plugin.getHellDatas().getPlayerIsland(plugin.getHellDatas().getHostHere(player.getLocation())).getZ() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Niveau : " + ChatColor.WHITE + plugin.getHellDatas().getPlayerIsland(plugin.getHellDatas().getHostHere(player.getLocation())).getLevel() + ChatColor.RED + ".");
						sender.sendMessage(ChatColor.RED + "Challenges accomplis : " + ChatColor.WHITE + plugin.getHellDatas().getPlayerIsland(plugin.getHellDatas().getHostHere(player.getLocation())).getChallenges().size() + ChatColor.RED + ".");
					} else {
						sender.sendMessage(ChatColor.RED + "Vous n'êtes pas sur une île.");
					}
				}
				
				if(args[0].equalsIgnoreCase("info")) {
					if(plugin.getHellDatas().hasIsland(player.getName())) {
						Island island = plugin.getHellDatas().getPlayerIsland(player.getName());
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
					if(plugin.getHellDatas().hasIsland(playername)) {
						Island island = plugin.getHellDatas().getPlayerIsland(playername);
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
		if(plugin.getHellDatas().hasIsland(player.getName()))
		{
			Island island = plugin.getHellDatas().getPlayerIsland(player.getName());
			sender.sendMessage(ChatColor.RED + "Vous avez déjà une île aux coordonnées : x=" + ChatColor.BLUE + island.getX() + ChatColor.RED + ", z=" + ChatColor.BLUE + island.getZ());
			sender.sendMessage(ChatColor.RED + "Pour recommencer une île, tapez /ishell restart");
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
		
		island.buildHell(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()));
		plugin.getHellDatas().addPlayerIsland(player.getName(), island);
		plugin.getHellDatas().teleportIsland(player);
		plugin.getHellDatas().createHome(player, new Location(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()), island.getX(), plugin.getISLANDS_Y(), island.getZ()));
		
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
	
	
}
