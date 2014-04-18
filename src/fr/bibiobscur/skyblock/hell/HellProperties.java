package fr.bibiobscur.skyblock.hell;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.ItemStack;

import fr.bibiobscur.skyblock.Plugin;

public class HellProperties implements Listener{
	
	private final Plugin plugin;
	
	public HellProperties(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/*@EventHandler
	public void portalTravelEntity(EntityPortalEvent e) {
		System.out.println(e.toString());
		if(e.getFrom().getWorld().getBlockAt(e.getFrom()).getType() == Material.PORTAL) {
			System.out.println("portal");
			if(e.getFrom().getWorld().getName().equals(plugin.getworldname())) {
				String hosthere = plugin.getDatas().getHostHere(e.getFrom());
				if(hosthere == null)
					e.setCancelled(true);
				else if (!plugin.getHellDatas().hasIsland(hosthere)) 
					e.setCancelled(true);
				else {
					e.getEntity().teleport(new Location(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()), plugin.getHellDatas().getPlayerIsland(hosthere).getX(), plugin.getISLANDS_Y() + 1, plugin.getHellDatas().getPlayerIsland(hosthere).getZ()));
					System.out.println("Teleported");
				}
				//System.out.println(e.getTo().toString());
				System.out.println(e.getEntity().getLocation());
				e.setCancelled(true);
			} else if(e.getFrom().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
				String hosthere = plugin.getHellDatas().getHostHere(e.getFrom());
				
			}
		}
	}*/
	
	@EventHandler
	public void portalTravel(PlayerPortalEvent e) {
		if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			Player player = e.getPlayer();
			if(e.getFrom().getWorld().getName().equals(plugin.getworldname())) {
				String hosthere = plugin.getDatas().getHostHere(e.getFrom());
				if(hosthere == null)
					player.teleport(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getSpawnLocation());
				else if (!plugin.getHellDatas().hasIsland(hosthere))
					player.teleport(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getSpawnLocation());
				else {
					if(/*hosthere.equals(player.getName())*/plugin.getDatas().isOnIsland(player) && plugin.getHellDatas().hasHome(player.getName()))
						plugin.getHellDatas().teleportHome(player);
					else
						plugin.getHellDatas().teleportIsland(player, hosthere);
				}
				e.setCancelled(true);
			} else if(e.getFrom().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
				String hosthere = plugin.getHellDatas().getHostHere(e.getFrom());
				if(hosthere == null)
					player.teleport(plugin.getServer().getWorld(plugin.getworldname()).getSpawnLocation());
				else {
					if(/*hosthere.equals(player.getName())*/plugin.getHellDatas().isOnIsland(player) && plugin.getDatas().hasHome(player.getName()))
						plugin.getDatas().teleportHome(player);
					else
						plugin.getDatas().teleportIsland(player, hosthere);
				}
				e.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void netherrackGenerator(BlockFromToEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(e.getToBlock().getData() == 1 && e.getToBlock().getType() == Material.STATIONARY_LAVA) {
				int compteur = 0;
				if(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX()+1, e.getToBlock().getY(), e.getToBlock().getZ()).getType() == Material.STATIONARY_LAVA && plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX()+1, e.getToBlock().getY(), e.getToBlock().getZ()).getData() == 0)
					compteur ++;
				if(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX(), e.getToBlock().getY(), e.getToBlock().getZ()+1).getType() == Material.STATIONARY_LAVA && plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX(), e.getToBlock().getY(), e.getToBlock().getZ()+1).getData() == 0)
					compteur ++;
				if(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX()-1, e.getToBlock().getY(), e.getToBlock().getZ()).getType() == Material.STATIONARY_LAVA && plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX()-1, e.getToBlock().getY(), e.getToBlock().getZ()).getData() == 0)
					compteur ++;
				if(plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX(), e.getToBlock().getY(), e.getToBlock().getZ()-1).getType() == Material.STATIONARY_LAVA && plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX(), e.getToBlock().getY(), e.getToBlock().getZ()-1).getData() == 0)
					compteur ++;
				if(compteur >= 2) plugin.getServer().getWorld(plugin.getHellDatas().getworldname()).getBlockAt(e.getToBlock().getX(), e.getToBlock().getY(), e.getToBlock().getZ()).setType(Material.NETHERRACK);
			}
		}
	}
	
	@EventHandler
	public void mobSpawner(BlockPlaceEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(e.getBlock().getType() == Material.MOB_SPAWNER) {
				CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
				spawner.setCreatureTypeByName("BLAZE");
			}
		}
	}
	
	@EventHandler
	public void glowBroken(BlockBreakEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(e.getBlock().getType() == Material.GLOWSTONE) {
				if(!e.getPlayer().getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH) && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
					Random rand = new Random();
					if(rand.nextInt(5) == 0)
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.QUARTZ));
				}
			}
		}
	}
	
	
	@EventHandler
	public void createSapling(PlayerInteractEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getType() == Material.SOUL_SAND && e.getPlayer().getItemInHand().getType() == Material.GLOWSTONE_DUST && e.getPlayer().getWorld().getBlockAt(e.getClickedBlock().getX(), e.getClickedBlock().getY()+1, e.getClickedBlock().getZ()).getType() == Material.AIR) {
					if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
						if(e.getPlayer().getItemInHand().getAmount() <= 1)
							e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
						else
							e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
					}
					Random rand = new Random();
					if(rand.nextInt(5) == 0)
						e.getPlayer().getWorld().getBlockAt(e.getClickedBlock().getX(), e.getClickedBlock().getY()+1, e.getClickedBlock().getZ()).setType(Material.SAPLING);
				}
				if(e.getClickedBlock().getType() == Material.SAPLING && e.getPlayer().getItemInHand().getType() == Material.FLINT && e.getPlayer().getWorld().getBlockAt(e.getClickedBlock().getX(), e.getClickedBlock().getY()-1, e.getClickedBlock().getZ()).getType() == Material.SOUL_SAND) {
					if(generateTree(e.getClickedBlock().getLocation()) && e.getPlayer().getGameMode() != GameMode.CREATIVE)
					{
						if(e.getPlayer().getItemInHand().getAmount() <= 1)
							e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
						else
							e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
						giveExp(e.getPlayer(), 8);
					}
				}
			}
		}
	}
	
	public boolean generateTree(Location location) {
		
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		Random rand = new Random();
		int taille = rand.nextInt(3) + 4;
		
		int i, j, k;
		
		//On vérifie qu'il n'y a pas de blocs autour du tronc
		for(i = 0; i < taille + 1; i++)
			for(j = 0; j < 3; j++)
				for(k = 0; k < 3; k++)
					if(world.getBlockAt(x -1 + j, y + i, z - 1 + k).getType() != Material.AIR && world.getBlockAt(x -1 + j, y + i, z - 1 + k).getType() != Material.SAPLING) return false;
				
		
		//Tronc de gravier
		for(i = 0; i < taille; i++) {
			if(world.getBlockAt(x, y + i, z).getType() == Material.AIR || world.getBlockAt(x, y + i, z).getType() == Material.SAPLING)
				world.getBlockAt(x, y + i, z).setType(Material.GRAVEL);
		}
		
		//Feuilles
		if(world.getBlockAt(x, y + taille, z).getType() == Material.AIR)
			world.getBlockAt(x, y + taille, z).setType(Material.GLOWSTONE);
		
		for(j = 0; j < 3; j++) {
			for(k = 0; k < 3; k++) {
				if(world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).getType() == Material.AIR)
					world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).setType(Material.GLOWSTONE);
			}
		}
		
		for(j = -2; j < 3; j++) {
			for(k = -2; k < 3; k++) {
				if(world.getBlockAt(x + j, y + taille - 2, z + k).getType() == Material.AIR &&
						Math.abs(j) != Math.abs(k) && (Math.abs(j) == 2 || Math.abs(k) == 2))
					world.getBlockAt(x + j, y + taille - 2, z + k).setType(Material.GLOWSTONE);
			}
		}
		
		world.playEffect(location, Effect.SMOKE, 5);
		world.playEffect(location, Effect.POTION_BREAK, 3);
		world.playSound(location, Sound.BLAZE_HIT, 20, 1);
		
		return true;
	}
	
	public boolean generateTree(World world, int x, int y, int z) {
		Random rand = new Random();
		int taille = rand.nextInt(3) + 4;
		
		int i, j, k;
		
		//On vérifie qu'il n'y a pas de blocs autour du tronc
		for(i = 0; i < taille + 1; i++)
			for(j = 0; j < 3; j++)
				for(k = 0; k < 3; k++)
					if(world.getBlockAt(x -1 + j, y + i, z - 1 + k).getType() != Material.AIR) return false;
				
		
		//Tronc de gravier
		for(i = 0; i < taille; i++) {
			if(world.getBlockAt(x, y + i, z).getType() == Material.AIR)
				world.getBlockAt(x, y + i, z).setType(Material.GRAVEL);
		}
		
		//Feuilles
		if(world.getBlockAt(x, y + taille, z).getType() == Material.AIR)
			world.getBlockAt(x, y + taille, z).setType(Material.GLOWSTONE);
		
		for(j = 0; j < 3; j++) {
			for(k = 0; k < 3; k++) {
				if(world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).getType() == Material.AIR)
					world.getBlockAt(x - 1 + j, y + taille - 1, z - 1 + k).setType(Material.GLOWSTONE);
			}
		}
		
		for(j = -2; j < 3; j++) {
			for(k = -2; k < 3; k++) {
				if(world.getBlockAt(x + j, y + taille - 1, z + k).getType() == Material.AIR &&
						Math.abs(j) != Math.abs(k))
					world.getBlockAt(x + j, y + taille - 1, z + k).setType(Material.GLOWSTONE);
			}
		}
		
		return true;
	}
	
	private void giveExp(Player player, int xp) {
		for(int i = 0; i < xp/10 + 1; i++)
			player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
		player.giveExp(xp);
	}
}
