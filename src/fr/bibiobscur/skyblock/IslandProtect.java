package fr.bibiobscur.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class IslandProtect implements Listener {

	public final Plugin plugin;

	public IslandProtect(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent e) {
		if(!e.getPlayer().isOp() && e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.isOnSpawn(e.getPlayer().getLocation())){
				e.setCancelled(true);
				//e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas ramasser d'item au spawn.");
			}
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e) {
		if(!e.getPlayer().isOp() && e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.isOnSpawn(e.getPlayer().getLocation())){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas drop d'item au spawn.");
			}
		}
	}

	@EventHandler
    public void blockPlaceProtect(BlockPlaceEvent e){
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			if(!plugin.isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && !e.getPlayer().isOp()){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez �tre sur votre �le pour faire ceci !");
			} else if(!plugin.isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'�tes pas sur votre �le.");
		}
    }
    
    @EventHandler
    public void blockBreakProtect(BlockBreakEvent e) {
    	if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
	    	if(!plugin.isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && !e.getPlayer().isOp()){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez �tre sur votre �le pour faire ceci !");
			} else if(!plugin.isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'�tes pas sur votre �le.");
    	}
    }
    
    @EventHandler
    public void interactProtect(PlayerInteractEvent e){
    	if(!e.getPlayer().isOp() && e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
	    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		    	if(!plugin.isOnIsland(e.getPlayer(), e.getClickedBlock().getLocation()) &&
		    			(e.getClickedBlock().getType() == Material.CHEST ||
		    			e.getClickedBlock().getType() == Material.FENCE_GATE ||
		    			e.getClickedBlock().getType() == Material.WOODEN_DOOR ||
		    			e.getClickedBlock().getType() == Material.NOTE_BLOCK ||
		    			e.getClickedBlock().getType() == Material.FURNACE)){
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "Vous devez �tre sur votre �le pour faire ceci !");
				}
	    	}
    	}
    }
    
	@EventHandler
    public void mobProtect(EntityDamageByEntityEvent e)
    {
		if(e.getDamager() instanceof Player) {
			
		    Player player = (Player) e.getDamager();
		    
		    if(!player.isOp() && player.getWorld().getName().equals(plugin.getworldname())) {
		    	
		    	String mob = e.getEntityType().name();
		    	
		    	if(!plugin.isOnIsland(player, e.getEntity().getLocation())) {

				    	if(mob.equalsIgnoreCase("Chicken") ||
				    			mob.equalsIgnoreCase("pig") ||
				    			mob.equalsIgnoreCase("Cow") ||
				    			mob.equalsIgnoreCase("Sheep") ||
				    			mob.equalsIgnoreCase("Wolf") ||
				    			mob.equalsIgnoreCase("Mushroom_Cow") ||
				    			mob.equalsIgnoreCase("Ocelot") ||
				    			mob.equalsIgnoreCase("Villager") ||
				    			mob.equalsIgnoreCase("Horse")){
				    		e.setDamage(0);
				    	}
				    	
				    	/*if(e.getEntity() instanceof Player) {
				    		Player player2 = (Player) e.getEntity();
				    		player2.sendMessage(player.getName() + "a pris 20 d�gats.");
				    		//player.damage(20);
				    	}
				    	player.damage(10);*/
			    	
		    	}
			}
		}
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void respawnSky(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			e.getPlayer().setLevel(0);
			e.setRespawnLocation(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation());
		}
	}
}
