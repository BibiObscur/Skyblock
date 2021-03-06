package fr.bibiobscur.skyblock.ajouts;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.world.PortalCreateEvent;

import fr.bibiobscur.skyblock.Island;
import fr.bibiobscur.skyblock.Plugin;
import fr.bibiobscur.skyblock.group.Group;

public class ChallengeDetector implements Listener {

	public final Plugin plugin;

	public ChallengeDetector(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerFarm(BlockBreakEvent e) {
		if(e.getBlock().getLocation().getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.getDatas().isOnIsland(e.getPlayer())) {
				if((e.getBlock().getType() == Material.CROPS ||
						e.getBlock().getType() == Material.CARROT ||
						e.getBlock().getType() == Material.POTATO ||
						e.getBlock().getType() == Material.NETHER_WARTS) &&
						e.getBlock().getData() == (byte) 7)
					giveExp(e.getPlayer(), 1);
			}
		}
		
		if(e.getBlock().getLocation().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(plugin.getDatas().isOnIsland(e.getPlayer())) {
				if(e.getBlock().getType() == Material.NETHER_WARTS && e.getBlock().getData() == (byte) 3)
					giveExp(e.getPlayer(), 1);
			}
		}
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.getDatas().isOnIsland(e.getPlayer())) {
				Island island = plugin.getDatas().getPlayerIsland(e.getPlayer().getName());
				
				if(e.getItem().getItemStack().getType() == Material.COBBLESTONE)
					island = challengeDone("CobblestoneGenerator", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.COBBLESTONE && e.getPlayer().getInventory().contains(new ItemStack(Material.COBBLESTONE, 63)))
					island = challengeDone("CobblestoneCollector", 50, e.getPlayer(), island, Material.IRON_PICKAXE, 1);
				
				if(e.getItem().getItemStack().getType() == Material.IRON_INGOT && e.getPlayer().getInventory().contains(new ItemStack(Material.IRON_INGOT)))
					island = challengeDone("IronGenerator", 50, e.getPlayer(), island, Material.REDSTONE_ORE, 8);
				if(e.getItem().getItemStack().getType() == Material.IRON_INGOT && e.getPlayer().getInventory().contains(new ItemStack(Material.IRON_INGOT, 63)))
					island = challengeDone("IronCollector", 500, e.getPlayer(), island, Material.DIAMOND_ORE, 16);
				
				if(e.getItem().getItemStack().getType() == Material.SAPLING)
					island = challengeDone("TreePicker", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.SEEDS)
					island = challengeDone("GetSeeds", 30, e.getPlayer(), island);
				
				if(e.getItem().getItemStack().getType() == Material.APPLE)
					island = challengeDone("AppleFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.PUMPKIN)
					island = challengeDone("PumpkinFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.MELON)
					island = challengeDone("MelonFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.POTATO_ITEM)
					island = challengeDone("PotatoFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.CARROT_ITEM)
					island = challengeDone("CarrotFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.CACTUS)
					island = challengeDone("CactusFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.WHEAT)
					island = challengeDone("WheatFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.SUGAR_CANE)
					island = challengeDone("SugarCaneFarmer", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.SLIME_BALL)
					island = challengeDone("SlimeBallFarmer", 30, e.getPlayer(), island);
				
				if(e.getItem().getItemStack().getType() == Material.APPLE && e.getPlayer().getInventory().contains(new ItemStack(Material.APPLE, 64)))
					island = challengeDone("AppleCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.PUMPKIN && e.getPlayer().getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
					island = challengeDone("PumpkinCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.MELON && e.getPlayer().getInventory().contains(new ItemStack(Material.MELON, 64)))
					island = challengeDone("MelonCollector", 80, e.getPlayer(), island, Material.INK_SACK, 1, (short) 3);
				if(e.getItem().getItemStack().getType() == Material.POTATO_ITEM && e.getPlayer().getInventory().contains(new ItemStack(Material.POTATO_ITEM, 64)))
					island = challengeDone("PotatoCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.CARROT_ITEM && e.getPlayer().getInventory().contains(new ItemStack(Material.CARROT_ITEM, 64)))
					island = challengeDone("CarrotCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.CACTUS && e.getPlayer().getInventory().contains(new ItemStack(Material.CACTUS, 64)))
					island = challengeDone("CactusCollector", 80, e.getPlayer(), island, Material.SAND, 32);
				if(e.getItem().getItemStack().getType() == Material.WHEAT && e.getPlayer().getInventory().contains(new ItemStack(Material.WHEAT, 64)))
					island = challengeDone("WheatCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.SUGAR_CANE && e.getPlayer().getInventory().contains(new ItemStack(Material.SUGAR_CANE, 64)))
					island = challengeDone("SugarCaneCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.SLIME_BALL && e.getPlayer().getInventory().contains(new ItemStack(Material.SLIME_BALL, 64)))
					island = challengeDone("SlimeBallCollector", 80, e.getPlayer(), island, Material.GRASS, 32);
				
				if(e.getItem().getItemStack().getType() == Material.RAW_CHICKEN || e.getItem().getItemStack().getType() == Material.FEATHER)
					island = challengeDone("Chicken", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.RAW_BEEF || e.getItem().getItemStack().getType() == Material.LEATHER)
					island = challengeDone("Cow", 30, e.getPlayer(), island);
				if(e.getItem().getItemStack().getType() == Material.PORK)
					island = challengeDone("Pig", 30, e.getPlayer(), island);
				
				if(e.getItem().getItemStack().getType() == Material.LOG && e.getPlayer().getInventory().contains(new ItemStack(Material.LOG, 64)))
					island = challengeDone("Timber", 40, e.getPlayer(), island, Material.IRON_AXE, 1);
				
				if(e.getItem().getItemStack().getType() == Material.BOW)
					island = challengeDone("Bowman", 30, e.getPlayer(), island, Material.ARROW, 10);
				
				if(e.getItem().getItemStack().getType() == Material.REDSTONE)
					island = challengeDone("ApprenticeRedstoneMan", 30, e.getPlayer(), island, Material.REDSTONE_TORCH_OFF, 1);
				
				if(e.getItem().getItemStack().getType() == Material.SNOW_BALL)
					island = challengeDone("DoYouWantToBuildASnowMan", 50, e.getPlayer(), island, Material.ICE, 5);
				
				if((e.getItem().getItemStack().getType() == Material.BONE ||
						e.getItem().getItemStack().getType() == Material.STRING ||
						e.getItem().getItemStack().getType() == Material.ROTTEN_FLESH ||
						e.getItem().getItemStack().getType() == Material.ARROW ||
						e.getItem().getItemStack().getType() == Material.SPIDER_EYE ||
						e.getItem().getItemStack().getType() == Material.SULPHUR) && 
						e.getPlayer().getInventory().contains(new ItemStack(Material.BONE, 64)) &&
						e.getPlayer().getInventory().contains(new ItemStack(Material.STRING, 64)) &&
						e.getPlayer().getInventory().contains(new ItemStack(Material.ROTTEN_FLESH, 64)) &&
						e.getPlayer().getInventory().contains(new ItemStack(Material.SPIDER_EYE)) &&
						e.getPlayer().getInventory().contains(new ItemStack(Material.ARROW)) &&
						e.getPlayer().getInventory().contains(new ItemStack(Material.SULPHUR, 64)))
					island = challengeDone("MobHunter", 100, e.getPlayer(), island, Material.OBSIDIAN, 10);
				
				if(e.getItem().getItemStack().getType() == Material.DIAMOND)
					island = challengeDone("Diamond!", 300, e.getPlayer(), island);
			}
		}
	}
	
	@EventHandler
	public void playerCraftItem(CraftItemEvent e) {
		
		Player player = plugin.getServer().getPlayer(e.getWhoClicked().getName());
		
		if(player.getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.getDatas().isOnIsland(player)) {
				
				Island island = plugin.getDatas().getPlayerIsland(player.getName());
				
				if(e.getCurrentItem().getType() == Material.STONE_PICKAXE ||
						e.getCurrentItem().getType() == Material.STONE_AXE ||
						e.getCurrentItem().getType() == Material.STONE_HOE ||
						e.getCurrentItem().getType() == Material.STONE_SPADE)
					island = challengeDone("StoneTools", 30, player, island);
				
				if(e.getCurrentItem().getType() == Material.IRON_PICKAXE ||
						e.getCurrentItem().getType() == Material.IRON_AXE ||
						e.getCurrentItem().getType() == Material.IRON_HOE ||
						e.getCurrentItem().getType() == Material.IRON_SPADE)
					island = challengeDone("IronTools", 80, player, island, Material.REDSTONE_ORE, 16);
				
				if(e.getCurrentItem().getType() == Material.DIAMOND_PICKAXE ||
						e.getCurrentItem().getType() == Material.DIAMOND_AXE ||
						e.getCurrentItem().getType() == Material.DIAMOND_HOE ||
						e.getCurrentItem().getType() == Material.DIAMOND_SPADE)
					island = challengeDone("DiamondTools", 170, player, island, Material.ANVIL, 1);
				
				if(e.getCurrentItem().getType() == Material.STONE_SWORD)
					island = challengeDone("StoneSword", 30, player, island, Material.LEATHER_HELMET, 1);
				if(e.getCurrentItem().getType() == Material.IRON_SWORD)
					island = challengeDone("IronSword", 80, player, island, Material.IRON_HELMET, 1);
				if(e.getCurrentItem().getType() == Material.DIAMOND_SWORD)
					island = challengeDone("DiamondSword", 170, player, island, Material.REDSTONE_ORE, 16);
				
				if(e.getCurrentItem().getType() == Material.BOW)
					island = challengeDone("Bowman", 30, player, island, Material.ARROW, 10);
				
				if(e.getCurrentItem().getType() == Material.TORCH)
					island = challengeDone("Lighting", 30, player, island);
				if(e.getCurrentItem().getType() == Material.FENCE)
					island = challengeDone("Safety", 30, player, island);
				if(e.getCurrentItem().getType() == Material.COBBLE_WALL)
					island = challengeDone("AdvancedSafety", 40, player, island);
				
				if(e.getCurrentItem().getType() == Material.SMOOTH_BRICK)
					island = challengeDone("Builder", 30, player, island);
				
				if(e.getCurrentItem().getType() == Material.BREAD)
					island = challengeDone("ApprenticeCooker", 40, player, island);
				if(e.getCurrentItem().getType() == Material.PUMPKIN_PIE)
					island = challengeDone("AdvancedCooker", 70, player, island);
				if(e.getCurrentItem().getType() == Material.CAKE)
					island = challengeDone("ProCooker", 100, player, island, Material.DIAMOND_ORE, 5);
				if(e.getCurrentItem().getType() == Material.COOKIE)
					island = challengeDone("CookieCooker", 50, player, island);
				
				if(e.getCurrentItem().getType() == Material.MUSHROOM_SOUP)
					island = challengeDone("MushroomSoup", 30, player, island, Material.MYCEL, 10);
				
				if(e.getCurrentItem().getType() == Material.FLINT_AND_STEEL)
					island = challengeDone("Pyromane", 30, player, island);
				
				if(e.getCurrentItem().getType() == Material.BOOK)
					island = challengeDone("YouCanRead", 30, player, island);
				
				if(e.getCurrentItem().getType() == Material.ENCHANTMENT_TABLE)
					island = challengeDone("BeAWizard", 50, player, island);
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerMobKill(EntityDeathEvent e) {

		if(e.getEntity().getLastDamageCause().getEventName().equals("EntityDamageByEntityEvent")) {
			EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
			
			if(e1.getDamager() instanceof Player) {
				Player player = (Player) e1.getDamager();
		
				if(player.getWorld().getName().equals(plugin.getworldname())) {
					if(plugin.getDatas().isOnIsland(player)) {
	
						Island island = plugin.getDatas().getPlayerIsland(player.getName());
						
						if(((e1.getEntityType().getTypeId() >= 50 && e1.getEntityType().getTypeId() <= 64) ||
								e1.getEntityType().getTypeId() == 66))
							island = challengeDone("MonsterSlayer", 50, player, island);
						if(e1.getEntityType() == EntityType.IRON_GOLEM)
							island = challengeDone("GolemDestroyer", 80, player, island, Material.GOLD_ORE, 16);
									
					}
				}
			}
		}
	}
	
	@EventHandler 
	public void creatureSpawn(CreatureSpawnEvent e) {
		if(e.getLocation().getWorld().getName().equals(plugin.getworldname()))
		{
			String playername = "";
			String currentname;
			boolean hostConnected = false;
			
			String hostName = plugin.getDatas().getHostHere(e.getLocation());
			if(plugin.isConnected(hostName)) {
				playername = hostName;
				hostConnected = true;
			} else if(plugin.getDatas().hasGroup(hostName)) {
				Group group = plugin.getDatas().getGroup(hostName);
				Iterator<String> it = group.getMembers().iterator();
				while(it.hasNext() && !hostConnected) {
					currentname = it.next();
					if(plugin.isConnected(currentname)) {
						playername = currentname;
						hostConnected = true;
					}
				}
			} else
				hostConnected = false;
			
			if(hostConnected)
			{
				//Bukkit.broadcastMessage("Un " + e.getEntityType().name() + " a spawn sur l'�le de " + playername);
				Island island = plugin.getDatas().getPlayerIsland(playername);
				if(e.getEntityType() == EntityType.CHICKEN || 
						e.getEntityType() == EntityType.COW || 
						e.getEntityType() == EntityType.HORSE || 
						e.getEntityType() == EntityType.MUSHROOM_COW || 
						e.getEntityType() == EntityType.OCELOT || 
						e.getEntityType() == EntityType.PIG || 
						e.getEntityType() == EntityType.SHEEP || 
						e.getEntityType() == EntityType.SQUID ||
						e.getEntityType() == EntityType.WOLF) {
					island = challengeDone("FriendlyRegion", 50, plugin.getServer().getPlayer(playername), island, Material.DIRT, 16);
					Animals mob = (Animals) e.getEntity();
					if(mob.getAge() < 0)
						island = challengeDone("SoCute", 100, plugin.getServer().getPlayer(playername), island, Material.GOLD_ORE, 8);
				}
				if(e.getEntityType() == EntityType.HORSE)
					island = challengeDone("HorseAreAmazing", 170, plugin.getServer().getPlayer(playername), island, Material.NAME_TAG, 2);
				
				if(e.getEntityType() == EntityType.BLAZE || 
						e.getEntityType() == EntityType.CAVE_SPIDER || 
						e.getEntityType() == EntityType.CREEPER || 
						e.getEntityType() == EntityType.ENDER_DRAGON || 
						e.getEntityType() == EntityType.ENDERMAN || 
						e.getEntityType() == EntityType.GHAST || 
						e.getEntityType() == EntityType.GIANT || 
						e.getEntityType() == EntityType.MAGMA_CUBE || 
						e.getEntityType() == EntityType.PIG_ZOMBIE || 
						e.getEntityType() == EntityType.SILVERFISH || 
						e.getEntityType() == EntityType.SKELETON || 
						e.getEntityType() == EntityType.SLIME || 
						e.getEntityType() == EntityType.SPIDER || 
						e.getEntityType() == EntityType.ZOMBIE)
					island = challengeDone("HostileRegion", 30, plugin.getServer().getPlayer(playername), island, Material.STONE_SWORD, 1);
				
				if(e.getEntityType() == EntityType.SNOWMAN)
					island = challengeDone("LetItGo", 120, plugin.getServer().getPlayer(playername), island, Material.CARROT_ITEM, 1);
				
				if(e.getEntityType() == EntityType.IRON_GOLEM && island.getChallenges().contains("VillagerCity"))
					island = challengeDone("VillagerProtector", 200, plugin.getServer().getPlayer(playername), island, Material.RED_ROSE, 1);
				if(e.getEntityType() == EntityType.VILLAGER && island.getChallenges().contains("VillagerFamily"))
					island = challengeDone("VillagerCity", 100, plugin.getServer().getPlayer(playername), island, Material.IRON_ORE, 16);
				if(e.getEntityType() == EntityType.VILLAGER && island.getChallenges().contains("ANewFriend"))
					island = challengeDone("VillagerFamily", 60, plugin.getServer().getPlayer(playername), island, Material.DIRT, 16);
				if(e.getEntityType() == EntityType.VILLAGER)
					island = challengeDone("ANewFriend", 40, plugin.getServer().getPlayer(playername), island, Material.LEASH, 1);
				
			}
		}
	}
	
	@EventHandler
	public void playerBurnFurnance(FurnaceBurnEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getworldname())) {
			String playername = plugin.getDatas().getHostHere(e.getBlock().getLocation());
			if(playername != null) {
				boolean online = false;
				for(int i = 0; i < plugin.getServer().getOnlinePlayers().length && !online; i++){
					if(plugin.getServer().getOnlinePlayers()[i].getName().equals(playername)) online = true;
				}
				if(online) {
					Player player = (Player) plugin.getServer().getPlayer(playername);
					Island island = plugin.getDatas().getPlayerIsland(player.getName());
					island = challengeDone("FurnanceUser", 30, player, island);
				}
			}
		}
	}
	
	@EventHandler
	public void playerItemConsume(PlayerItemConsumeEvent e) {
		
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname()) || e.getPlayer().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			if(plugin.getDatas().hasIsland(e.getPlayer().getName())) {
				Material item = e.getItem().getType();
			
				if(item == Material.CAKE ||
						item == Material.COOKED_BEEF ||
						item == Material.COOKED_CHICKEN ||
						item == Material.RAW_CHICKEN ||
						item == Material.COOKED_FISH ||
						item == Material.COOKIE ||
						item == Material.GRILLED_PORK ||
						item == Material.PUMPKIN_PIE ||
						item == Material.GOLDEN_CARROT ||
						item == Material.GOLDEN_APPLE ||
						item == Material.APPLE ||
						item == Material.BAKED_POTATO ||
						item == Material.BREAD ||
						item == Material.CARROT_ITEM ||
						item == Material.MELON ||
						item == Material.MUSHROOM_SOUP ||
						item == Material.RAW_FISH ||
						item == Material.ROTTEN_FLESH) {
					Player player = e.getPlayer();
					Island island = plugin.getDatas().getPlayerIsland(player.getName());
					island = challengeDone("EatToSurvive", 40, player, island, Material.IRON_ORE, 5);
					
					for(int i = 0; i < 2; i++)
						player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
					
					int foodGet = 0;
					
					if(item == Material.COOKIE ||
							item == Material.RAW_FISH ||
							item == Material.RAW_CHICKEN ||
							item == Material.MELON ||
							item == Material.ROTTEN_FLESH)
						foodGet = 2;
					if(item == Material.RAW_BEEF ||
							item == Material.PORK)
						foodGet = 3;
					if(item == Material.APPLE ||
							item == Material.CARROT_ITEM ||
							item == Material.GOLDEN_APPLE)
						foodGet = 4;
					if(item == Material.COOKED_FISH ||
							item == Material.BREAD)
						foodGet = 5;
					if(item == Material.COOKED_CHICKEN ||
							item == Material.BAKED_POTATO ||
							item == Material.MUSHROOM_SOUP ||
							item == Material.GOLDEN_CARROT)
						foodGet = 6;
					if(item == Material.PUMPKIN_PIE ||
							item == Material.GRILLED_PORK ||
							item == Material.COOKED_BEEF ||
							item == Material.CAKE)
						foodGet = 8;
					
					if(player.getFoodLevel() + foodGet > 20) foodGet = 20 - player.getFoodLevel();
					
					giveExp(player, foodGet*2);
				}
			}
		}
	}
	
	@EventHandler
	public void playerFish(PlayerFishEvent e) {
		if(plugin.getDatas().isOnIsland(e.getPlayer()))
		{
			if(e.getExpToDrop() > 0)
			{
				giveExp(e.getPlayer(), 10);
				challengeDone("FisherMan", 30, e.getPlayer(), plugin.getDatas().getPlayerIsland(e.getPlayer().getName()), Material.IRON_ORE, 5);
			}
		}
	}

	@EventHandler
	public void playerEnchant(EnchantItemEvent e) {
		if(plugin.getDatas().isOnIsland(e.getEnchanter()))
		{
			if (e.getExpLevelCost() >= 1)
				challengeDone("ApprenticeEnchanter", 50, e.getEnchanter(), plugin.getDatas().getPlayerIsland(e.getEnchanter().getName()), Material.BOOKSHELF, 10);
			if (e.getExpLevelCost() >= 15)
				challengeDone("Enchanter", 80, e.getEnchanter(), plugin.getDatas().getPlayerIsland(e.getEnchanter().getName()), Material.IRON_ORE, 16);
			if (e.getExpLevelCost() == 30)
				challengeDone("Wizard", 170, e.getEnchanter(), plugin.getDatas().getPlayerIsland(e.getEnchanter().getName()), Material.DIAMOND_ORE, 16);
		}
	}
	
	@EventHandler
	public void pegaseDetector(PlayerInteractEntityEvent e) {
		if(plugin.getDatas().isOnIsland(e.getPlayer())) {
			if(e.getRightClicked() instanceof Horse) {
				Horse horse = (Horse) e.getRightClicked();
				if(horse.getColor() == Color.WHITE && horse.getCustomName() == null) {
					if(e.getPlayer().getItemInHand().getType() == Material.NAME_TAG && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
						if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Pegase")) {
							Island island = plugin.getDatas().getPlayerIsland(e.getPlayer().getName());
							island = challengeDone("Pegase", 300, e.getPlayer(), island, Material.DIAMOND_SWORD, 1);
						}
					}
				}
			}
			/*if(PegaseProperties.isPegase(e.getRightClicked())) {
				Island island = plugin.getDatas().getPlayerIsland(e.getPlayer().getName());
				island = challengeDone("Pegase", 300, e.getPlayer(), island, Material.DIAMOND_SWORD, 1);
			}*/
		}
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e) {
		if(plugin.getDatas().isOnIsland(plugin.getServer().getPlayer(e.getPlayer().getName())))
		{
			Island island = plugin.getDatas().getPlayerIsland(e.getPlayer().getName());
			Player player = plugin.getServer().getPlayer(e.getPlayer().getName());
			
			if(e.getInventory().contains(new ItemStack(Material.APPLE, 64)))
				island = challengeDone("AppleCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
				island = challengeDone("PumpkinCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.MELON, 64)))
				island = challengeDone("MelonCollector", 80, player, island, Material.INK_SACK, 1, (short) 3);
			if(e.getInventory().contains(new ItemStack(Material.POTATO_ITEM, 64)))
				island = challengeDone("PotatoCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.CARROT_ITEM, 64)))
				island = challengeDone("CarrotCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.CACTUS, 64)))
				island = challengeDone("CactusCollector", 80, player, island, Material.SAND, 32);
			if(e.getInventory().contains(new ItemStack(Material.WHEAT, 64)))
				island = challengeDone("WheatCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.SUGAR_CANE, 64)))
				island = challengeDone("SugarCaneCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.SLIME_BALL, 64)))
				island = challengeDone("SlimeBallCollector", 80, player, island, Material.GRASS, 32);
			
			if(e.getInventory().contains(new ItemStack(Material.BONE, 64)) &&
					e.getInventory().contains(new ItemStack(Material.STRING, 64)) &&
					e.getInventory().contains(new ItemStack(Material.ROTTEN_FLESH, 64)) &&
					e.getInventory().contains(new ItemStack(Material.SULPHUR, 64)))
				island = challengeDone("MobHunter", 100, player, island, Material.OBSIDIAN, 10);
			
			if(e.getInventory() instanceof HorseInventory) {
				island = challengeDone("Riding", 150, player, island, Material.SADDLE, 1);
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if(plugin.getDatas().isOnIsland(plugin.getServer().getPlayer(e.getPlayer().getName())))
		{
			Island island = plugin.getDatas().getPlayerIsland(e.getPlayer().getName());
			Player player = plugin.getServer().getPlayer(e.getPlayer().getName());
			
			if(e.getInventory().contains(new ItemStack(Material.APPLE, 64)))
				island = challengeDone("AppleCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
				island = challengeDone("PumpkinCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.MELON, 64)))
				island = challengeDone("MelonCollector", 80, player, island, Material.INK_SACK, 1, (short)3);
			if(e.getInventory().contains(new ItemStack(Material.POTATO_ITEM, 64)))
				island = challengeDone("PotatoCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.CARROT_ITEM, 64)))
				island = challengeDone("CarrotCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.CACTUS, 64)))
				island = challengeDone("CactusCollector", 80, player, island, Material.SAND, 32);
			if(e.getInventory().contains(new ItemStack(Material.WHEAT, 64)))
				island = challengeDone("WheatCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.SUGAR_CANE, 64)))
				island = challengeDone("SugarCaneCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.SLIME_BALL, 64)))
				island = challengeDone("SlimeBallCollector", 80, player, island, Material.GRASS, 32);
			
			if(e.getInventory().contains(new ItemStack(Material.BONE, 64)) &&
					e.getInventory().contains(new ItemStack(Material.STRING, 64)) &&
					e.getInventory().contains(new ItemStack(Material.ROTTEN_FLESH, 64)) &&
					e.getInventory().contains(new ItemStack(Material.SULPHUR, 64)))
				island = challengeDone("MobHunter", 100, player, island, Material.OBSIDIAN, 10);
		}
	}
	
	@EventHandler
	public void playerBrew(BrewEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getworldname()) || e.getBlock().getWorld().getName().equals(plugin.getHellDatas().getworldname())) {
			String playername = plugin.getDatas().getHostHere(e.getBlock().getLocation());
			if(playername != null) {
				if(plugin.isConnected(playername)) {
					Player player = (Player) plugin.getServer().getPlayer(playername);
					Island island = plugin.getDatas().getPlayerIsland(player.getName());
					island = challengeDone("Brewing", 50, player, island);
					giveExp(player, 4);
				} else {
					for(int i = 0; i < 4; i++) {
						ExperienceOrb orb = (ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.EXPERIENCE_ORB);
						orb.setExperience(1);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void portalCreate(PortalCreateEvent e) {
		if(e.getWorld().getName().equals(plugin.getworldname()))
		{
			if(plugin.getDatas().getHostHere(e.getBlocks().get(0).getLocation()) != null)
			{
				String playername = "";
				String currentname;
				boolean hostConnected = false;
				
				String hostName = plugin.getDatas().getHostHere(e.getBlocks().get(0).getLocation());
				if(plugin.isConnected(hostName)) {
					playername = hostName;
					hostConnected = true;
				} else if(plugin.getDatas().hasGroup(hostName)) {
					Group group = plugin.getDatas().getGroup(hostName);
					Iterator<String> it = group.getMembers().iterator();
					while(it.hasNext() && !hostConnected) {
						currentname = it.next();
						if(plugin.isConnected(currentname)) {
							playername = currentname;
							hostConnected = true;
						}
					}
				} else
					hostConnected = false;
				
				if(hostConnected)
				{
					challengeDone("NetherPortal", 68, plugin.getServer().getPlayer(playername), plugin.getDatas().getPlayerIsland(playername), Material.GOLD_SWORD, 1);
				}
			}
		}
	}
	
	
	public Island challengeDone(String challengename, int xp, Player player, Island island) {
		if(plugin.getDatas().hasIsland(player.getName())) {
			if(!island.getChallenges().contains(challengename)) {
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
				giveExp(player, xp);
				island.getChallenges().add(challengename);
			}
		}
		return island;
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island, Material bonus, int amount) {
		if(plugin.getDatas().hasIsland(player.getName())) {
			if(!island.getChallenges().contains(challengename)) {
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez gagn� un bonus de " + ChatColor.GREEN + amount + " " + ChatColor.BLUE + bonus.toString() + ChatColor.GOLD + " !");
				giveExp(player, xp);
				island.getChallenges().add(challengename);
				player.getInventory().addItem(new ItemStack(bonus, amount));
				player.updateInventory();
			}
		}
		return island;
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island, Material bonus, int amount, short data) {
		if(plugin.getDatas().hasIsland(player.getName())) {
			if(!island.getChallenges().contains(challengename)) {
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
				player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez gagn� un bonus de " + ChatColor.GREEN + amount + " " + ChatColor.BLUE + bonus.toString() + ChatColor.GOLD + " !");
				giveExp(player, xp);
				island.getChallenges().add(challengename);
				player.getInventory().addItem(new ItemStack(bonus, amount, data));
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
	
	
	@EventHandler
	public void respawnSky(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			Player player = e.getPlayer();
			Island island = plugin.getDatas().getPlayerIsland(player.getName());
			island = challengeDone("SawTheDeath", 30, player, island);
		}
	}
	
}
