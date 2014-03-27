package fr.bibiobscur.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
			if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && !e.getPlayer().isOp()){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
			} else if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
		}
    }
    
    @EventHandler
    public void blockBreakProtect(BlockBreakEvent e) {
    	if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
	    	if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && !e.getPlayer().isOp()){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
			} else if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
    	}
    }
    
    @EventHandler
    public void interactProtect(PlayerInteractEvent e){
    	if(!e.getPlayer().isOp() && e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
	    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		    	if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getClickedBlock().getLocation()) &&
		    			(e.getClickedBlock().getType() == Material.CHEST ||
		    			e.getClickedBlock().getType() == Material.FENCE_GATE ||
		    			e.getClickedBlock().getType() == Material.WOODEN_DOOR ||
		    			e.getClickedBlock().getType() == Material.NOTE_BLOCK ||
		    			e.getClickedBlock().getType() == Material.FURNACE ||
		    			e.getClickedBlock().getType() == Material.BURNING_FURNACE ||
		    			e.getClickedBlock().getType() == Material.BREWING_STAND ||
		    			e.getClickedBlock().getType() == Material.ANVIL ||
		    			e.getClickedBlock().getType() == Material.TRAP_DOOR ||
		    			e.getClickedBlock().getType() == Material.HOPPER ||
				    	e.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR ||
						e.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_ON ||
						e.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_OFF ||
		    			e.getClickedBlock().getType() == Material.DROPPER ||
		    			e.getClickedBlock().getType() == Material.DISPENSER ||
		    			e.getClickedBlock().getType() == Material.JUKEBOX ||
		    			e.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET ||
	    				e.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET ||
	    				e.getPlayer().getItemInHand().getType() == Material.BUCKET)){
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
				}
	    	}
	    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
	    	{
	    		if(!plugin.getDatas().isOnIsland(e.getPlayer()) &&
	    				(e.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET ||
	    				e.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET ||
	    				e.getPlayer().getItemInHand().getType() == Material.BUCKET ||
	    				e.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL ||
	    	    		e.getPlayer().getItemInHand().getType() == Material.MINECART ||
	    				e.getPlayer().getItemInHand().getType() == Material.STORAGE_MINECART ||
	    				e.getPlayer().getItemInHand().getType() == Material.POWERED_MINECART ||
	    				e.getPlayer().getItemInHand().getType() == Material.HOPPER_MINECART)) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
	    		}
	    	}
	    	
    	}
    }
    
    @EventHandler
    public void interactEntityProtect(PlayerInteractEntityEvent e) {
    	if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
    		if(!e.getPlayer().isOp() && !plugin.getDatas().isOnIsland(e.getPlayer(), e.getRightClicked().getLocation())) {
    			if(e.getRightClicked().getType() == EntityType.MINECART_CHEST || 
    					e.getRightClicked().getType() == EntityType.MINECART_FURNACE ||
    					e.getRightClicked().getType() == EntityType.MINECART_HOPPER ||
    					e.getRightClicked().getType() == EntityType.ITEM_FRAME ||
    					e.getRightClicked().getType() == EntityType.LEASH_HITCH ||
    					e.getRightClicked().getType() == EntityType.HORSE) {
    				e.setCancelled(true);
    				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
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
		    	
		    	if(!plugin.getDatas().isOnIsland(player, e.getEntity().getLocation())) {

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
			    	
		    	}
			}
		}
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void respawnSky(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			//e.getPlayer().setLevel(0);
			e.setRespawnLocation(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation());
		}
	}
}
