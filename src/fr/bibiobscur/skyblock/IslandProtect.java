package fr.bibiobscur.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.inventory.HorseInventory;

import fr.bibiobscur.skyblock.ajouts.PegaseProperties;

public class IslandProtect implements Listener {

	public final Plugin plugin;

	public IslandProtect(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private boolean isOnSkyworld(Player player) {
		if(player.getWorld().getName().equals(plugin.getworldname()))
			return true;
		else if(player.getWorld().getName().equals(plugin.getHellDatas().getworldname()))
			return true;
		else
			return false;
	}
	
	private boolean isOnSkyworld(Location location) {
		if(location.getWorld().getName().equals(plugin.getworldname()))
			return true;
		else if(location.getWorld().getName().equals(plugin.getHellDatas().getworldname()))
			return true;
		else 
			return false;
	}
	
	@EventHandler
	public void tntProtect(EntityExplodeEvent e) {
		if(e.getEntity().getWorld().getName().equals(plugin.getworldname())) {
			if(e.getEntity() instanceof TNTPrimed) {
				//e.blockList().clear();
				TNTPrimed tnt = (TNTPrimed) e.getEntity();
				if(tnt.isIncendiary()) {
					tnt.setIsIncendiary(false);
					e.blockList().clear();
				}
			}
		}
	}
	
	@EventHandler
	public void antiTntCannon(EntityDamageEvent e) {
		if(e.getEntity().getWorld().getName().equals(plugin.getworldname())) {
			if(e.getEntityType() == EntityType.PRIMED_TNT) {
				TNTPrimed tnt = (TNTPrimed) e.getEntity();
				tnt.setIsIncendiary(true);
				//e.getEntity().remove();
			}
		}
	}
	
	@EventHandler
	public void mobExplosionsProtect(ExplosionPrimeEvent e) {
		if(e.getEntity().getLocation().getWorld().getName().equals(plugin.getworldname())) {
			if(e.getEntityType() == EntityType.FIREBALL || e.getEntityType() == EntityType.WITHER_SKULL)
				e.setCancelled(true);
		}
		
		if(plugin.isOnSpawn(e.getEntity().getLocation()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void itemFrameProtect(HangingBreakByEntityEvent e) {
		if(e.getEntity().getWorld().getName().equals(plugin.getworldname()) ||
				e.getEntity().getWorld().getName().equals(plugin.getHellDatas().getworldname()))
		{
			if(e.getRemover() instanceof Player)
			{
				Player player = (Player) e.getRemover();
				
				if(!plugin.getDatas().isOnIsland(player, e.getEntity().getLocation()))
				{
					if(!player.getName().equals("BibiObscur")) e.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île !");
				}
			}
		}
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent e) {
		if(!e.getPlayer().isOp() && isOnSkyworld(e.getPlayer())) {
			if(plugin.isOnSpawn(e.getPlayer().getLocation())){
				e.setCancelled(true);
				//e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas ramasser d'item au spawn.");
			}
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e) {
		if(!e.getPlayer().isOp() && isOnSkyworld(e.getPlayer())) {
			if(plugin.isOnSpawn(e.getPlayer().getLocation())){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas drop d'item au spawn.");
			}
		}
	}

	@EventHandler
    public void blockPlaceProtect(BlockPlaceEvent e){
		if(isOnSkyworld(e.getPlayer())) {
			if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation())/* && !e.getPlayer().getName().equals("BibiObscur")*/ && (!e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.CREATIVE)){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
			} else if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
		}
    }
    
    @EventHandler
    public void blockBreakProtect(BlockBreakEvent e) {
    	if(isOnSkyworld(e.getPlayer())) {
	    	if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation())/* && !e.getPlayer().getName().equals("BibiObscur")*/ && (!e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.CREATIVE)){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "Vous devez être sur votre île pour faire ceci !");
			} else if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getBlock().getLocation()) && e.getPlayer().isOp())
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
    	}
    }
    
    @EventHandler
    public void blockPushedEvent(BlockPistonExtendEvent e) {
    	if(isOnSkyworld(e.getBlock().getLocation())) {
    		for(int i = 0; i < e.getBlocks().size(); i++) {
	    		if(((e.getBlocks().get(i).getX()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && e.getDirection() == BlockFace.WEST) ||
						((e.getBlocks().get(i).getZ()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && e.getDirection() == BlockFace.NORTH) ||
						((e.getBlocks().get(i).getX()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && e.getDirection() == BlockFace.EAST) ||
						((e.getBlocks().get(i).getZ()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && e.getDirection() == BlockFace.SOUTH)){
					e.setCancelled(true);
	    		}
    		}
    		
    	}
    }
    
    @EventHandler
    public void interactProtect(PlayerInteractEvent e){
    	if(!e.getPlayer().getName().equals("BibiObscur") && isOnSkyworld(e.getPlayer())) {
	    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		    	if(!plugin.getDatas().isOnIsland(e.getPlayer(), e.getClickedBlock().getLocation()) &&
		    			(e.getClickedBlock().getType() == Material.CHEST ||
		    			e.getClickedBlock().getType() == Material.TRAPPED_CHEST ||
				    	e.getClickedBlock().getType() == Material.FENCE_GATE ||
		    			e.getClickedBlock().getType() == Material.WOODEN_DOOR ||
		    			e.getClickedBlock().getType() == Material.NOTE_BLOCK ||
		    			e.getClickedBlock().getType() == Material.FURNACE ||
		    			e.getClickedBlock().getType() == Material.BURNING_FURNACE ||
		    			e.getClickedBlock().getType() == Material.BREWING_STAND ||
		    			e.getClickedBlock().getType() == Material.ANVIL ||
		    			e.getClickedBlock().getType() == Material.TRAP_DOOR ||
				    	e.getClickedBlock().getType() == Material.HOPPER ||
						e.getClickedBlock().getType() == Material.DIODE ||
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
    public void horseInventoryProtect(InventoryOpenEvent e) {
		Player player = (Player) e.getPlayer();
    	if(isOnSkyworld(player)) {
    		if(!plugin.getDatas().isOnIsland(player)) {
    			if(e.getInventory() instanceof HorseInventory) {
    				player.sendMessage("Vous ne pouvez pas ouvrir l'inventaire d'un cheval ici.");
    				e.setCancelled(true);
    			}
    		}
    	}
    }

    @EventHandler
    public void interactEntityProtect(PlayerInteractEntityEvent e) {
    	if(isOnSkyworld(e.getPlayer())) {
    		if(!e.getPlayer().getName().equals("BibiObscur") && !plugin.getDatas().isOnIsland(e.getPlayer(), e.getRightClicked().getLocation())) {
    			if(e.getRightClicked().getType() == EntityType.MINECART_CHEST || 
    					e.getRightClicked().getType() == EntityType.MINECART_FURNACE ||
    					e.getRightClicked().getType() == EntityType.MINECART_HOPPER ||
    					e.getRightClicked().getType() == EntityType.ITEM_FRAME ||
    					e.getRightClicked().getType() == EntityType.LEASH_HITCH) {
    				e.setCancelled(true);
    				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
    			}
    		}
    		
			if(e.getRightClicked().getType() == EntityType.HORSE) {
				if(plugin.getDatas().hasIsland(e.getPlayer().getName())) {
					if(!plugin.getDatas().getPlayerIsland(e.getPlayer().getName()).getChallenges().contains("Riding") && !plugin.getDatas().isOnIsland(e.getPlayer(), e.getRightClicked().getLocation())) {
						e.setCancelled(true);
	    				e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas monter sur un cheval.");
					} else if(PegaseProperties.isPegase(e.getRightClicked()) && !plugin.getDatas().getPlayerIsland(e.getPlayer().getName()).getChallenges().contains("Pegase")) {
						e.setCancelled(true);
	    				e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas monter sur un pegase.");
					}
				} else
					e.setCancelled(true);
			}
    		
    	}
    }
    
	@EventHandler
    public void mobProtect(EntityDamageByEntityEvent e)
    {
		if(e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
			
			Player player;
			if(e.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getDamager();
				if(projectile.getShooter() instanceof Player) {
					player = (Player) projectile.getShooter();
				} else
					return;
			} else
				player = (Player) e.getDamager();
		    
		    if(/*!player.isOp() &&*/ isOnSkyworld(player)) {
		    	
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
				    	/*if(e.getDamager() instanceof Projectile)
				    		e.setCancelled(true);*/
				    	e.setDamage(0);
				    }
				    
				    if(e.getEntityType() == EntityType.ITEM_FRAME) {
				    	e.setCancelled(true);
				    	player.sendMessage(ChatColor.RED + "Vous n'êtes pas sur votre île.");
				    }
		    	}
			}
		}
    }
	
	@EventHandler
    public void mobBurnProtect(EntityCombustByEntityEvent e)
    {
		if(e.getCombuster() instanceof Player || e.getCombuster() instanceof Projectile) {
			
			Player player;
			if(e.getCombuster() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getCombuster();
				if(projectile.getShooter() instanceof Player) {
					player = (Player) projectile.getShooter();
				} else
					return;
			} else
				player = (Player) e.getCombuster();
		    
		    if(/*!player.isOp() &&*/ isOnSkyworld(player)) {
		    	
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
				    	LivingEntity healmob = (LivingEntity) e.getEntity();
				    	healmob.setHealth(10);
				    	e.setCancelled(true);
				    }
		    	}
			}
		}
    }
	
	@EventHandler
	public void spawnMobProtect(CreatureSpawnEvent e) {
		if(plugin.isOnSpawn(e.getLocation()))
			e.setCancelled(true);
		/*if(plugin.getDatas().getHostHere(e.getLocation()) != null) {
			String playername = plugin.getDatas().getHostHere(e.getLocation());
			if(plugin.isConnected(playername)) {
				Player player = plugin.getServer().getPlayer(playername);
				if(e.getLocation().getBlockX() <= player.getLocation().getBlockX() + 16 &&
						e.getLocation().getBlockX() >= player.getLocation().getBlockX() - 16 &&
						e.getLocation().getBlockZ() <= player.getLocation().getBlockZ() + 16 &&
						e.getLocation().getBlockZ() >= player.getLocation().getBlockZ() - 16)
					player.sendMessage("Un " + ChatColor.BLUE + e.getEntityType().name() + ChatColor.WHITE + " est apparu aux coordonnées : " + e.getLocation().getBlockX() + ", " + e.getLocation().getBlockY() + ", " + e.getLocation().getBlockZ() + ".");
			}
		}*/
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void respawnSky(PlayerRespawnEvent e) {
		if(isOnSkyworld(e.getPlayer())) {
			e.setRespawnLocation(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation());
		}
	}

	@EventHandler
	public void leaveEnterIsland(PlayerMoveEvent e) {
		if(e.getFrom().getWorld().getName().equals(plugin.getworldname())) {
			//Si le joueur quitte une île
			if(((e.getFrom().getBlockX()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockX()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockZ()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockZ()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockX()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockX()-plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockZ()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockZ()-plugin.getISLAND_SPACING()/2)%200) == 0)){
				if(plugin.getDatas().getHostHere(e.getFrom()) != null) {
					e.getPlayer().sendMessage(ChatColor.GOLD + "Vous quittez l'île de " + ChatColor.BLUE + plugin.getDatas().getHostHere(e.getFrom()) + ChatColor.GOLD + ".");
				}
			} else
			//Si le joueur rejoint une île
			if(((e.getTo().getBlockX()+1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockX()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockZ()+1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockZ()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockX()-1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockX()-plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockZ()-1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockZ()-plugin.getISLAND_SPACING()/2)%200) == 0)){
				if(plugin.getDatas().getHostHere(e.getTo()) != null) {
					e.getPlayer().sendMessage(ChatColor.GOLD + "Vous entrez sur l'île de " + ChatColor.BLUE + plugin.getDatas().getHostHere(e.getTo()) + ChatColor.GOLD + ".");
				}
			}
		} else if(e.getFrom().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			//Si le joueur quitte une île
			if(((e.getFrom().getBlockX()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockX()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockZ()+1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockZ()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockX()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockX()-plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getFrom().getBlockZ()-1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getTo().getBlockZ()-plugin.getISLAND_SPACING()/2)%200) == 0)){
				if(plugin.getHellDatas().getHostHere(e.getFrom()) != null) {
					e.getPlayer().sendMessage(ChatColor.GOLD + "Vous quittez l'île de " + ChatColor.RED + plugin.getHellDatas().getHostHere(e.getFrom()) + ChatColor.GOLD + ".");
				}
			} else
			//Si le joueur rejoint une île
			if(((e.getTo().getBlockX()+1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockX()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockZ()+1-plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockZ()+plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockX()-1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockX()-plugin.getISLAND_SPACING()/2)%200) == 0) ||
					((e.getTo().getBlockZ()-1+plugin.getISLAND_SPACING()/2)%200 == 0 && ((e.getFrom().getBlockZ()-plugin.getISLAND_SPACING()/2)%200) == 0)){
				if(plugin.getHellDatas().getHostHere(e.getTo()) != null) {
					e.getPlayer().sendMessage(ChatColor.GOLD + "Vous entrez sur l'île de " + ChatColor.RED + plugin.getHellDatas().getHostHere(e.getTo()) + ChatColor.GOLD + ".");
				}
			}
		}
	}


}
