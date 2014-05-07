package fr.bibiobscur.skyblock.ajouts;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.bibiobscur.skyblock.Plugin;

public class PegaseProperties implements Listener{
	
	private HashMap<String, Integer> fallingList = new HashMap<String, Integer>();
	
	public PegaseProperties(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void regen(EntityRegainHealthEvent e) {
		e.setAmount(e.getAmount()*2);
	}
	
	@EventHandler
	public void fallDamage(EntityDamageEvent e) {
		if(isPegase(e.getEntity())) {
			if(e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if(player.isInsideVehicle()) {
				if(player.getVehicle() instanceof Entity) {
					Entity entity = (Entity) player.getVehicle();
					if(isPegase(entity)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void sneak(VehicleExitEvent e){
		if(e.getVehicle() instanceof Entity && e.getVehicle().getPassenger() instanceof Player) {
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
				} else {
					if(fallingList.containsKey(player.getName()))
						fallingList.remove(player.getName());
				}
			}
		}
	}
	
	@EventHandler
	public void enter(VehicleEnterEvent e) {
		if(e.getVehicle() instanceof Entity && e.getEntered() instanceof Player) {
			Entity entity = (Entity) e.getVehicle();
			if(isPegase(entity)) {
				LivingEntity livingentity = (LivingEntity) entity;
				if(livingentity.getHealth() == livingentity.getMaxHealth()) {
					livingentity.setMaxHealth(60);
					livingentity.setHealth(60);
				} else {
					double quotient = livingentity.getHealth()/livingentity.getMaxHealth();
					livingentity.setMaxHealth(60);
					livingentity.setHealth(livingentity.getMaxHealth()*quotient);
				}
				livingentity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 2));
				Horse horse = (Horse) entity;
				horse.setJumpStrength(1.5);
				Player player = (Player) e.getEntered();
				if(fallingList.containsKey(player.getName()))
					fallingList.remove(player.getName());
				fallingList.put(player.getName(), 10);
			}
		}
	}
	
	@EventHandler
	public void jump(HorseJumpEvent e) {
		if(isPegase(e.getEntity())) {
			if(!isPegaseBlock(new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getBlockX(), e.getEntity().getLocation().getBlockY()-1, e.getEntity().getLocation().getBlockZ())) &&
					new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getBlockX(), e.getEntity().getLocation().getBlockY()-1, e.getEntity().getLocation().getBlockZ()).getBlock().getType() != Material.AIR) {
				if(e.getPower() == 1) {
					//System.out.println("good");
					e.setPower(e.getPower()*2);
					e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.GHAST_FIREBALL, 20, 1);
					Player player = (Player) e.getEntity().getPassenger();
					if(fallingList.containsKey(player.getName()))
						fallingList.remove(player.getName());
				} else {
					//System.out.println("fail");
					//e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound., 20, 1);
				}
			} else
				e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.GHAST_FIREBALL, 15, 1);
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e) {
		if(e.getPlayer().isInsideVehicle()) {
			if(e.getPlayer().getVehicle() instanceof Entity) {
				Entity entity = (Entity) e.getPlayer().getVehicle();
				if(isPegase(entity)) {
					if(isPegaseBlock(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ())))
						e.getFrom().getWorld().getBlockAt(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ())).setType(Material.AIR);
					
					/*if(!isPegaseBlock(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ()))
							&& new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(), e.getFrom().getBlockY()-1, e.getFrom().getBlockZ()).getBlock().getType() == Material.AIR) {
						if(e.getFrom().getBlockY() == e.getTo().getBlockY()) {
							if(fallingList.containsKey(e.getPlayer().getName()))
								fallingList.remove(e.getPlayer().getName());
							fallingList.put(e.getPlayer().getName(), 10);
						}
					}*/
					
					if(!fallingList.containsKey(e.getPlayer().getName())) {
						//System.out.println("notfalling");
						setPegaseBlock(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ()));
						if(!(e.getPlayer().getWorld().getBlockAt(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ())).getType() == Material.AIR || 
								isPegaseBlock(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ()))))
							fallingList.put(e.getPlayer().getName(), 10);
					} else {
						int delay = fallingList.get(e.getPlayer().getName());
						//System.out.println("falling : " + delay);
						if(delay - (e.getFrom().getBlockY() - e.getTo().getBlockY()) <= 10)
							delay -= (e.getFrom().getBlockY() - e.getTo().getBlockY());
						else
							delay = 10;
						fallingList.remove(e.getPlayer().getName());
						if(delay > 0/* && 
								(e.getPlayer().getWorld().getBlockAt(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ())).getType() == Material.AIR || 
								isPegaseBlock(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY()-1, e.getPlayer().getLocation().getBlockZ())))*/)
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
	
	public static boolean isPegase(Entity entity) {
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
}
