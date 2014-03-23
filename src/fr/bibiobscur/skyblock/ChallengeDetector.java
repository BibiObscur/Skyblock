package fr.bibiobscur.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class ChallengeDetector implements Listener {

	public final Plugin plugin;

	public ChallengeDetector(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			if(plugin.isOnIsland(e.getPlayer())) {
				Island island = plugin.getPlayerIsland(e.getPlayer().getName());
				
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
				
				if(e.getItem().getItemStack().getType() == Material.PUMPKIN && e.getPlayer().getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
					island = challengeDone("PumpkinCollector", 80, e.getPlayer(), island, Material.DIRT, 32);
				if(e.getItem().getItemStack().getType() == Material.MELON && e.getPlayer().getInventory().contains(new ItemStack(Material.MELON, 64)))
					island = challengeDone("MelonCollector", 80, e.getPlayer(), island, Material.INK_SACK, 1);
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
				
				if(e.getItem().getItemStack().getType() == Material.LOG && e.getPlayer().getInventory().contains(new ItemStack(Material.LOG, 64)))
					island = challengeDone("Timber", 40, e.getPlayer(), island, Material.IRON_AXE, 1);
				
				if(e.getItem().getItemStack().getType() == Material.BOW)
					island = challengeDone("Bowman", 30, e.getPlayer(), island, Material.ARROW, 10);
				
				if(e.getItem().getItemStack().getType() == Material.REDSTONE)
					island = challengeDone("ApprenticeRedstoneMan", 30, e.getPlayer(), island);
				
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
			if(plugin.isOnIsland(player)) {
				
				Island island = plugin.getPlayerIsland(player.getName());
				
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
					if(plugin.isOnIsland(player)) {
	
						Island island = plugin.getPlayerIsland(player.getName());
						
						if(((e1.getEntityType().getTypeId() >= 50 && e1.getEntityType().getTypeId() <= 64) ||
								e1.getEntityType().getTypeId() == 66))
							island = challengeDone("MonsterSlayer", 50, player, island);
									
					}
				}
			}
		}
	}
	
	@EventHandler 
	public void creatureSpawn(CreatureSpawnEvent e) {
		if(e.getLocation().getWorld().getName().equals(plugin.getworldname()))
		{
			boolean hostConnected = false;
			for(int i = 0; i < plugin.getServer().getOnlinePlayers().length && !hostConnected; i++)
			{
				if(plugin.getServer().getOnlinePlayers()[i].getName().equals(plugin.getHostHere(e.getLocation()))) hostConnected = true;
			}
			
			if(hostConnected)
			{
				String playername = plugin.getHostHere(e.getLocation());
				Island island = plugin.getPlayerIsland(playername);
				if(e.getEntityType() == EntityType.CHICKEN || 
						e.getEntityType() == EntityType.COW || 
						e.getEntityType() == EntityType.HORSE || 
						e.getEntityType() == EntityType.MUSHROOM_COW || 
						e.getEntityType() == EntityType.OCELOT || 
						e.getEntityType() == EntityType.PIG || 
						e.getEntityType() == EntityType.SHEEP || 
						e.getEntityType() == EntityType.SQUID || 
						e.getEntityType() == EntityType.VILLAGER || 
						e.getEntityType() == EntityType.WOLF)
					island = challengeDone("FriendlyRegion", 50, plugin.getServer().getPlayer(playername), island, Material.DIRT, 16);
				
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
				
				if(e.getEntityType() == EntityType.IRON_GOLEM && island.challenges.contains("VillagerCity"))
					island = challengeDone("VillagerProtector", 200, plugin.getServer().getPlayer(playername), island, Material.RED_ROSE, 1);
				if(e.getEntityType() == EntityType.VILLAGER && island.challenges.contains("VillagerFamily"))
					island = challengeDone("VillagerCity", 100, plugin.getServer().getPlayer(playername), island, Material.IRON_ORE, 16);
				if(e.getEntityType() == EntityType.VILLAGER && island.challenges.contains("ANewFriend"))
					island = challengeDone("VillagerFamily", 60, plugin.getServer().getPlayer(playername), island, Material.DIRT, 16);
				if(e.getEntityType() == EntityType.VILLAGER)
					island = challengeDone("ANewFriend", 40, plugin.getServer().getPlayer(playername), island, Material.LEASH, 1);
				
			}
		}
	}
	
	@EventHandler
	public void playerBurnFurnance(FurnaceBurnEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getworldname())) {
			String playername = plugin.getHostHere(e.getBlock().getLocation());
			if(playername != null) {
				boolean online = false;
				for(int i = 0; i < plugin.getServer().getOnlinePlayers().length && !online; i++){
					if(plugin.getServer().getOnlinePlayers()[i].getName().equals(playername)) online = true;
				}
				if(online) {
					Player player = (Player) plugin.getServer().getPlayer(playername);
					Island island = plugin.getPlayerIsland(player.getName());
					island = challengeDone("FurnanceUser", 30, player, island);
				}
			}
		}
	}
	
	@EventHandler
	public void playerItemConsume(PlayerItemConsumeEvent e) {
		
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			
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
					item == Material.RAW_FISH) {
				Player player = e.getPlayer();
				Island island = plugin.getPlayerIsland(player.getName());
				island = challengeDone("EatToSurvive", 40, player, island, Material.IRON_ORE, 8);
				
				for(int i = 0; i < 2; i++)
					player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
				
				int foodGet = 0;
				
				if(item == Material.COOKIE ||
						item == Material.RAW_FISH ||
						item == Material.RAW_CHICKEN ||
						item == Material.MELON)
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
				
				player.giveExp(foodGet*2);
			}
		}
	}
	
	@EventHandler
	public void playerFish(PlayerFishEvent e) {
		if(plugin.isOnIsland(e.getPlayer()))
		{
			if(e.getExpToDrop() > 0)
			{
				for(int i = 0; i < 2; i++)
					e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.EXPERIENCE_ORB);
				e.getPlayer().giveExp(17);
				challengeDone("FisherMan", 30, e.getPlayer(), plugin.getPlayerIsland(e.getPlayer().getName()), Material.IRON_ORE, 16);
			}
		}
	}

	@EventHandler
	public void playerEnchant(EnchantItemEvent e) {
		if(plugin.isOnIsland(e.getEnchanter()))
		{
			if (e.getExpLevelCost() >= 1)
				challengeDone("ApprenticeEnchanter", 50, e.getEnchanter(), plugin.getPlayerIsland(e.getEnchanter().getName()), Material.BOOKSHELF, 10);
			if (e.getExpLevelCost() >= 15)
				challengeDone("Enchanter", 80, e.getEnchanter(), plugin.getPlayerIsland(e.getEnchanter().getName()), Material.IRON_ORE, 16);
			if (e.getExpLevelCost() == 30)
				challengeDone("Wizard", 170, e.getEnchanter(), plugin.getPlayerIsland(e.getEnchanter().getName()), Material.DIAMOND_ORE, 16);
		}
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e) {
		if(plugin.isOnIsland(plugin.getServer().getPlayer(e.getPlayer().getName())))
		{
			Island island = plugin.getPlayerIsland(e.getPlayer().getName());
			Player player = plugin.getServer().getPlayer(e.getPlayer().getName());
			
			if(e.getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
				island = challengeDone("PumpkinCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.MELON, 64)))
				island = challengeDone("MelonCollector", 80, player, island, Material.INK_SACK, 1);
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
			
			if(e.getInventory().contains(new ItemStack(Material.BONE, 64)) &&
					e.getInventory().contains(new ItemStack(Material.STRING, 64)) &&
					e.getInventory().contains(new ItemStack(Material.ROTTEN_FLESH, 64)) &&
					e.getInventory().contains(new ItemStack(Material.SULPHUR, 64)))
				island = challengeDone("MobHunter", 100, player, island, Material.OBSIDIAN, 10);
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if(plugin.isOnIsland(plugin.getServer().getPlayer(e.getPlayer().getName())))
		{
			Island island = plugin.getPlayerIsland(e.getPlayer().getName());
			Player player = plugin.getServer().getPlayer(e.getPlayer().getName());
			
			if(e.getInventory().contains(new ItemStack(Material.PUMPKIN, 64)))
				island = challengeDone("PumpkinCollector", 80, player, island, Material.DIRT, 32);
			if(e.getInventory().contains(new ItemStack(Material.MELON, 64)))
				island = challengeDone("MelonCollector", 80, player, island, Material.INK_SACK, 1);
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
			
			if(e.getInventory().contains(new ItemStack(Material.BONE, 64)) &&
					e.getInventory().contains(new ItemStack(Material.STRING, 64)) &&
					e.getInventory().contains(new ItemStack(Material.ROTTEN_FLESH, 64)) &&
					e.getInventory().contains(new ItemStack(Material.SULPHUR, 64)))
				island = challengeDone("MobHunter", 100, player, island, Material.OBSIDIAN, 10);
		}
	}
	
	@EventHandler
	public void playerBrew(BrewEvent e) {
		if(e.getBlock().getWorld().getName().equals(plugin.getworldname())) {
			String playername = plugin.getHostHere(e.getBlock().getLocation());
			if(playername != null) {
				boolean online = false;
				for(int i = 0; i < plugin.getServer().getOnlinePlayers().length && !online; i++){
					if(plugin.getServer().getOnlinePlayers()[i].getName().equals(playername)) online = true;
				}
				if(online) {
					Player player = (Player) plugin.getServer().getPlayer(playername);
					Island island = plugin.getPlayerIsland(player.getName());
					island = challengeDone("Brewing", 50, player, island);
				}
			}
		}
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island) {
		if(!island.challenges.contains(challengename)) {
			player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
			for(int i = 0; i < xp/10; i++)
				player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
			player.giveExp(xp);
			island.challenges.add(challengename);
		}
		return island;
	}
	
	public Island challengeDone(String challengename, int xp, Player player, Island island, Material bonus, int amount) {
		if(!island.challenges.contains(challengename)) {
			player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez accompli le challenge " + ChatColor.BLUE + challengename + ChatColor.GOLD + " !");
			player.sendMessage("[SkyChallenge] " + ChatColor.GOLD + "Vous avez gagné un bonus de " + ChatColor.GREEN + amount + " " + ChatColor.BLUE + bonus.toString() + ChatColor.GOLD + " !");
			for(int i = 0; i < xp/10; i++)
				player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
			player.giveExp(xp);
			island.challenges.add(challengename);
			player.getInventory().addItem(new ItemStack(bonus, amount));
		}
		return island;
	}
	
	
	@EventHandler
	public void respawnSky(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals(plugin.getworldname())) {
			Player player = e.getPlayer();
			Island island = plugin.getPlayerIsland(player.getName());
			island = challengeDone("SawTheDeath", 30, player, island);
		}
	}
	
}
