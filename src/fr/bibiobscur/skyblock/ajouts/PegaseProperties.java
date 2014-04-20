package fr.bibiobscur.skyblock.ajouts;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import fr.bibiobscur.skyblock.Plugin;

public class PegaseProperties implements Listener{
	
	private HashMap<String, Integer> fallingList = new HashMap<String, Integer>();
	
	public PegaseProperties(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void fallDamage(EntityDamageEvent e) {
		if(isPegase(e.getEntity())) {
			if(e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}
	}
	
	private boolean isPegase(Entity entity) {
		if(entity instanceof Horse) {
			Horse horse = (Horse) entity;
			if(horse.getCustomName() != null) {
				if(horse.getColor() == Color.WHITE && horse.getCustomName().equalsIgnoreCase("Pegase")) {
					return true;
				} else
					return false;
			} else 
				return false;
		} else
			return false;
	}

	@EventHandler
	public void sneak(VehicleExitEvent e){
		if(e.getVehicle() instanceof Entity) {
			Entity entity = (Entity) e.getVehicle();
			if(isPegase(entity)) {
				Player player = (Player) e.getVehicle().getPassenger();
				
				if(isPegaseBlock(new Location(e.getVehicle().getLocation().getWorld(),e.getVehicle().getLocation().getBlockX(), e.getVehicle().getLocation().getBlockY()-1, e.getVehicle().getLocation().getBlockZ())) ||
						player.getWorld().getBlockAt(new Location(e.getVehicle().getLocation().getWorld(),e.getVehicle().getLocation().getBlockX(), e.getVehicle().getLocation().getBlockY()-1, e.getVehicle().getLocation().getBlockZ())).getType() == Material.AIR)
				{
					e.setCancelled(true);
					fallingList.put(player.getName(), 3);
					if(isPegaseBlock(new Location(e.getVehicle().getLocation().getWorld(),e.getVehicle().getLocation().getBlockX(), e.getVehicle().getLocation().getBlockY()-1, e.getVehicle().getLocation().getBlockZ())))
						player.getWorld().getBlockAt(new Location(e.getVehicle().getLocation().getWorld(),e.getVehicle().getLocation().getBlockX(), e.getVehicle().getLocation().getBlockY()-1, e.getVehicle().getLocation().getBlockZ())).setType(Material.AIR);
				}
			}
		}
	}
	
	@EventHandler
	public void jump(HorseJumpEvent e) {
		if(isPegase(e.getEntity()))
			e.setPower(e.getPower()*3);
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e) {
		if(e.getPlayer().isInsideVehicle()) {
			if(e.getPlayer().getVehicle() instanceof Entity) {
				Entity entity = (Entity) e.getPlayer().getVehicle();
				if(isPegase(entity)) {
					if(isPegaseBlock(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ())))
						e.getFrom().getWorld().getBlockAt(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ())).setType(Material.AIR);
					
					if(!fallingList.containsKey(e.getPlayer().getName())) {
						setPegaseBlock(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ()));
					} else {
						int delay = fallingList.get(e.getPlayer().getName());
						delay -= (e.getFrom().getBlockY() - e.getTo().getBlockY());
						fallingList.remove(e.getPlayer().getName());
						if(delay > 0 && 
								(e.getPlayer().getWorld().getBlockAt(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ())).getType() == Material.AIR || 
								isPegaseBlock(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ()))))
							fallingList.put(e.getPlayer().getName(), delay);
					}
				}
			}
		}
	}
	
	private boolean isPegaseBlock(Location location) {
		Block block = location.getBlock();
		if(block.getType() == Material.SNOW_BLOCK) {
			if(block.getData() == (byte)3) {
				return true;
			} else
				return false;
		} else
			return false;
	}
	
	private void setPegaseBlock(Location location) {
		if(location.getBlock().getType() == Material.AIR) {
			location.getBlock().setType(Material.SNOW_BLOCK);
			location.getBlock().setData((byte)3);
		}
	}
}
