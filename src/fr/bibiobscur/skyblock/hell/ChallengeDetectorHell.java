package fr.bibiobscur.skyblock.hell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.bibiobscur.skyblock.Island;
import fr.bibiobscur.skyblock.Plugin;

public class ChallengeDetectorHell implements Listener{
	public final Plugin plugin;

	public ChallengeDetectorHell(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(plugin.getDatas().isOnIsland(e.getPlayer())) {
				Island island = plugin.getHellDatas().getPlayerIsland(e.getPlayer().getName());
				
				if(e.getItem().getItemStack().getType() == Material.NETHERRACK)
					island = challengeDone("NetherrackGenerator", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.NETHERRACK && e.getPlayer().getInventory().contains(new ItemStack(Material.NETHERRACK, 63)))
					island = challengeDone("NetherrackCollector", 50, e.getPlayer(), island, Material.SOUL_SAND, 16);
				
				
			
			}
		}
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island) {
		if(plugin.getHellDatas().hasIsland(player.getName())) {
			if(!island.getChallenges().contains(challengename)) {
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
				giveExp(player, xp);
				island.getChallenges().add(challengename);
			}
		}
		return island;
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island, Material bonus, int amount) {
		if(plugin.getHellDatas().hasIsland(player.getName())) {
			if(!island.getChallenges().contains(challengename)) {
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez gagné un bonus de " + ChatColor.GREEN + amount + " " + ChatColor.BLUE + bonus.toString() + ChatColor.GOLD + " !");
				giveExp(player, xp);
				island.getChallenges().add(challengename);
				player.getInventory().addItem(new ItemStack(bonus, amount));
				player.updateInventory();
			}
		}
		return island;
	}
	
	private void giveExp(Player player, int xp) {
		for(int i = 0; i < xp/10 + 1; i++)
			player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
		player.giveExp(xp);
	}
}
