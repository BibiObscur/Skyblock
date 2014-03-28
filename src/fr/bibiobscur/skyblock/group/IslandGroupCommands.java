package fr.bibiobscur.skyblock.group;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.bibiobscur.skyblock.Plugin;

public class IslandGroupCommands implements CommandExecutor {

	public final Plugin plugin;
	private HashSet<Invite> inviteRequest = new HashSet<Invite>();
	
	public IslandGroupCommands(Plugin plugin) {
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
		
		/*if(!plugin.hasIsland(player.getName())) {
			player.sendMessage(ChatColor.RED + "Tu dois avoir une île pour effectuer cette commande.");
			return true;
		} else if(!plugin.isLeader(player.getName())) {
			player.sendMessage(ChatColor.RED + "Tu n'es pas le leader de l'île.");
			return true;
		}*/
		
		if(plugin.getworldname().isEmpty()){
			player.sendMessage(ChatColor.RED + "Le monde skyblock n'a pas encore été défini.");
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("isg"))
		{
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Commandes groupe : ");
				sender.sendMessage(ChatColor.RED + "/isg help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
				sender.sendMessage(ChatColor.RED + "/isg invite <player>" + ChatColor.WHITE + " : Invite un joueur à rejoindre ton île.");
				sender.sendMessage(ChatColor.RED + "/isg accept <player>" + ChatColor.WHITE + " : Accepter une invitation.");
				sender.sendMessage(ChatColor.RED + "/isg decline <player>" + ChatColor.WHITE + " : Refuser une invitation.");
				sender.sendMessage(ChatColor.RED + "/isg kick <player>" + ChatColor.WHITE + " : Expulser un joueur de votre île.");
				sender.sendMessage(ChatColor.RED + "/isg leave" + ChatColor.WHITE + " : Quitter un groupe.");
				sender.sendMessage(ChatColor.RED + "/isg givelead <player>" + ChatColor.WHITE + " : Donne le lead à un autre joueur.");
				sender.sendMessage(ChatColor.RED + "/isg info" + ChatColor.WHITE + " : Voir les infos sur votre groupe.");
				sender.sendMessage(ChatColor.RED + "/isg infohere" + ChatColor.WHITE + " : Voir les infos sur le groupe sur l'île à votre emplacement.");
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.RED + "Commandes groupe : ");
					sender.sendMessage(ChatColor.RED + "/isg help" + ChatColor.WHITE + " : Voir les commandes disponibles.");
					sender.sendMessage(ChatColor.RED + "/isg invite <player>" + ChatColor.WHITE + " : Invite un joueur à rejoindre ton île.");
					sender.sendMessage(ChatColor.RED + "/isg accept <player>" + ChatColor.WHITE + " : Accepter une invitation.");
					sender.sendMessage(ChatColor.RED + "/isg decline <player>" + ChatColor.WHITE + " : Refuser une invitation.");
					sender.sendMessage(ChatColor.RED + "/isg kick <player>" + ChatColor.WHITE + " : Expulser un joueur de votre île.");
					sender.sendMessage(ChatColor.RED + "/isg leave" + ChatColor.WHITE + " : Quitter un groupe.");
					sender.sendMessage(ChatColor.RED + "/isg givelead <player>" + ChatColor.WHITE + " : Donne le lead à un autre joueur.");
					sender.sendMessage(ChatColor.RED + "/isg info" + ChatColor.WHITE + " : Voir les infos sur votre groupe.");
					sender.sendMessage(ChatColor.RED + "/isg infohere" + ChatColor.WHITE + " : Voir les infos sur le groupe sur l'île à votre emplacement.");
				}
				
				if(args[0].equalsIgnoreCase("leave")) {
					if(plugin.getDatas().hasGroup(player.getName())) {
						if(!plugin.getDatas().isLeader(player.getName())) {
							plugin.getDatas().quitGroup(player.getName());
							player.getInventory().clear();
						} else
							player.sendMessage(ChatColor.RED + "Vous ne pouvez pas quitter votre propre groupe. faites /givelead <player> pour donner votre groupe à un autre joueur.");
					} else 
						player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans un groupe.");
				}
				
				if(args[0].equalsIgnoreCase("info")) {
					if(plugin.getDatas().hasGroup(player.getName())) {
						Group group = plugin.getDatas().getGroup(player.getName());
						player.sendMessage(ChatColor.GREEN + "Informations sur le groupe de " + ChatColor.BLUE + group.getLeader() + ChatColor.GREEN + " :");
						player.sendMessage(ChatColor.YELLOW + "Leader : " + ChatColor.WHITE + group.getLeader());
						String membres = "";
						Iterator<String> it = group.getMembers().iterator();
						while(it.hasNext())
							membres += "" + ChatColor.WHITE + it.next() + ChatColor.YELLOW + ", ";
						player.sendMessage(ChatColor.YELLOW + "Membres : " + ChatColor.WHITE + membres);
						} else
						player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans un groupe.");
				}
				
				if(args[0].equalsIgnoreCase("infohere")) {
					String hostname = plugin.getDatas().getHostHere(player.getLocation());
					if(plugin.getDatas().hasGroup(hostname)) {
						Group group = plugin.getDatas().getGroup(hostname);
						player.sendMessage(ChatColor.GREEN + "Informations sur le groupe de " + ChatColor.BLUE + group.getLeader() + ChatColor.GREEN + " :");
						player.sendMessage(ChatColor.YELLOW + "Leader : " + ChatColor.WHITE + group.getLeader());
						String membres = "";
						Iterator<String> it = group.getMembers().iterator();
						while(it.hasNext())
							membres += "" + ChatColor.WHITE + it.next() + ChatColor.YELLOW + ", ";
						player.sendMessage(ChatColor.YELLOW + "Membres : " + ChatColor.WHITE + membres);
					} else {
						player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + hostname + ChatColor.RED + " n'a pas de groupe.");
					}
				}
				
				if(args[0].equalsIgnoreCase("invite"))
					player.sendMessage(ChatColor.RED + "Commande : /isg invite <player>");
				if(args[0].equalsIgnoreCase("accept"))
					player.sendMessage(ChatColor.RED + "Commande : /isg accept <player>");
				if(args[0].equalsIgnoreCase("decline"))
					player.sendMessage(ChatColor.RED + "Commande : /isg decline <player>");
				if(args[0].equalsIgnoreCase("kick"))
					player.sendMessage(ChatColor.RED + "Commande : /isg kick <player>");
				if(args[0].equalsIgnoreCase("givelead"))
					player.sendMessage(ChatColor.RED + "Commande : /isg givelead <player>");
				
			} else if(args.length == 2) {
				String playername2 = args[1];
				
				if(args[0].equalsIgnoreCase("invite")) {
					if(plugin.isConnected(playername2)) {
						if(plugin.getDatas().isLeader(player.getName())) {
							if(!plugin.getDatas().hasIsland(playername2)) {
								inviteRequest.add(new Invite(player.getName(), playername2));
								player.sendMessage("Demande envoyée à " + ChatColor.BLUE + playername2 + ChatColor.WHITE + ".");
								plugin.getServer().getPlayer(playername2).sendMessage(ChatColor.BLUE + player.getName() + ChatColor.GREEN + " vous invite à rejoindre son île. Pour accepter, faites /isg accept " + player.getName() + ". Sinon, faites /isg decline " + player.getName());
							} else
								player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " a déjà une île.");
						} else
							player.sendMessage(ChatColor.RED + "Vous devez être leader d'un groupe pour faire ceci.");
					} else
						player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas connecté.");
				}
				
				if(args[0].equalsIgnoreCase("accept")) {
					if(isInvited(playername2, player.getName())) {
						if(plugin.isConnected(playername2)) {
							if(!plugin.getDatas().hasIsland(player.getName())) {
								if(plugin.getDatas().isLeader(playername2)) {
									plugin.getDatas().addPlayerToGroup(playername2, player.getName());
									removeRequest(playername2, player.getName());
								} else
									player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas leader d'un groupe ou ne possède pas d'île.");
							} else
								player.sendMessage(ChatColor.RED + "Vous avez déjà une île. Quittez votre île pour aller dans un groupe.");
						} else
							player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est plus connecté.");
					} else
						player.sendMessage(ChatColor.RED + "L'invitation n'a pas été trouvée.");
				}
				
				if(args[0].equalsIgnoreCase("decline")) {
					if(isInvited(playername2, player.getName())) {
						if(plugin.isConnected(playername2)) {
							if(plugin.getDatas().isLeader(playername2)) {
								player.sendMessage("Vous avez refusé la demande de " + ChatColor.BLUE + playername2);
								plugin.getServer().getPlayer(playername2).sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " a refusé votre demande.");
								removeRequest(playername2, player.getName());
							} else
								player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas leader d'un groupe ou ne possède pas d'île.");
						} else
							player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est plus connecté.");
					} else
						player.sendMessage(ChatColor.RED + "L'invitation n'a pas été trouvée.");
				}
				
				if(args[0].equalsIgnoreCase("kick")) {
					if(plugin.getDatas().isLeader(player.getName())) {
						if(plugin.getDatas().hasIsland(playername2)) {
							if(plugin.getDatas().hasGroup(playername2)) {
								if(plugin.getDatas().getGroup(player.getName()).getMembers().contains(playername2)) {
									plugin.getDatas().removePlayerFromGroup(player.getName(), playername2);
								} else
									player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas dans votre groupe.");
							} else
								player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'a pas de groupe.");
						} else
							player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas sur une île.");
					} else
						player.sendMessage(ChatColor.RED + "Vous devez être leader d'un groupe pour faire ceci.");
				}
				
				if(args[0].equalsIgnoreCase("givelead")) {
					if(plugin.getDatas().isLeader(player.getName())) {
						if(plugin.getDatas().hasIsland(playername2)) {
							if(plugin.getDatas().hasGroup(playername2)) {
								if(plugin.getDatas().getGroup(player.getName()).getMembers().contains(playername2)) {
									plugin.getDatas().changeLeader(player.getName(), playername2);
								} else
									player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas dans votre groupe.");
							} else
								player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'a pas de groupe.");
						} else
							player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BLUE + playername2 + ChatColor.RED + " n'est pas sur une île.");
					} else
						player.sendMessage(ChatColor.RED + "Vous devez être leader d'un groupe pour faire ceci.");
				}
			}
		}
		
		return false;
	}
	
	private boolean isInvited(String inviting, String invited) {
		Iterator<Invite> it = inviteRequest.iterator();
		Invite request;
		while(it.hasNext()) {
			request = it.next();
			if(request.getInvited().equalsIgnoreCase(invited) && request.getInviting().equalsIgnoreCase(inviting)) return true;
		}
		return false;
	}
	
	private boolean removeRequest(String inviting, String invited) {
		Iterator<Invite> it = inviteRequest.iterator();
		Invite request;
		while(it.hasNext()) {
			request = it.next();
			if(request.getInvited().equalsIgnoreCase(invited) && request.getInviting().equalsIgnoreCase(inviting)) {
				inviteRequest.remove(request);
				return true;
			}
		}
		return false;
	}
}
