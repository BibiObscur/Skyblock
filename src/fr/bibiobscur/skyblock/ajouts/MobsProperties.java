package fr.bibiobscur.skyblock.ajouts;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import fr.bibiobscur.skyblock.Plugin;

public class MobsProperties implements Listener {
	
	private final Plugin plugin;
	
	public MobsProperties(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void endermanDeath(EntityDeathEvent e) {
		if(e.getEntity().getLocation().getWorld().getName().equals(plugin.getworldname())) {
			if(e.getEntityType() == EntityType.ENDERMAN) {
				Enderman enderman = (Enderman) e.getEntity();
				if(enderman.getCarriedMaterial() != null && enderman.getLocation().getBlockY() > 0)
				{
					enderman.getWorld().dropItem(enderman.getLocation(), enderman.getCarriedMaterial().toItemStack(1));
				}
			}
		}
	}
	
}
