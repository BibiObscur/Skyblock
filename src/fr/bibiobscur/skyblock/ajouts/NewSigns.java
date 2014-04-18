package fr.bibiobscur.skyblock.ajouts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import fr.bibiobscur.skyblock.Plugin;

public class NewSigns implements Listener{
	
	private final Plugin plugin;
	
	private final Material sendLevels[] = {
			Material.STONE,
			Material.DIRT,
			Material.SAPLING,
			Material.SAND,
			Material.GRAVEL,
			Material.GOLD_ORE,
			Material.LOG,
			Material.SANDSTONE,
			Material.NOTE_BLOCK,
			Material.LONG_GRASS,
			Material.WOOL,
			Material.BROWN_MUSHROOM,
			Material.ICE,
			Material.CLAY,
			Material.NETHERRACK,
			Material.STAINED_GLASS,
			Material.STAINED_CLAY,
			Material.SNOW_BALL,
			Material.NETHER_STALK
	};
	
	private final Material rareItems[] = {
			Material.MONSTER_EGG,
			Material.MONSTER_EGG,
			Material.REDSTONE_BLOCK,
			Material.BREWING_STAND_ITEM,// + cauldron_item, anvil, hopper, + jukebox
			Material.IRON_PICKAXE,// + tools and sword
			Material.GOLDEN_APPLE,// + golden carrot + enchanted golden apple
			Material.IRON_HELMET, // + armor
			Material.GOLD_HELMET,
			Material.IRON_BARDING // + gold
	};
	
	private final Material bestItems[] = {
			//Material.MOB_SPAWNER, //every mob
			Material.DIAMOND_BLOCK,
			Material.ANVIL,
			Material.DIAMOND_PICKAXE,
			Material.DIAMOND_HELMET,
			Material.ENCHANTED_BOOK //random enchant
	};
	
	public NewSigns(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		
		Player player = e.getPlayer();
		
		if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			{
				Sign sign = (Sign) block.getState();

				if(sign.getLines()[0].equals("[SEND LEVELS]"))
				{
					if(plugin.getDatas().hasIsland(player.getName()))
					{
						if(plugin.getDatas().getPlayerIsland(player.getName()).getLevel() >= 150)
						{
							if(player.getLevel() > 0)
							{
								if(player.getInventory().getItemInHand().getType() == Material.AIR)
								{
									ItemStack item;
									//Random rand = new Random();
									//Material itemType = sendLevels[rand.nextInt(sendLevels.length)];
									do {
										Material itemType = sendLevels[(int) (Math.random() * sendLevels.length)];
										int amount = (int)(Math.random()*player.getLevel() + player.getLevel());
										if(player.getLevel()>32) amount = 64;
	
										item = getItemStack(itemType, amount);
									} while(canGet(item, player));
									
									if(player.getLevel() <= 32)
										player.setLevel(0);
									else
										player.setLevel(player.getLevel()-32);
									player.getInventory().setItemInHand(item);
									player.sendMessage(ChatColor.GOLD + "Vous avez obtenu " + ChatColor.BLUE + item.getAmount() + ChatColor.GOLD + " entités de " + ChatColor.BLUE + item.getType().name());
									e.setCancelled(true);
								} else 
									player.sendMessage(ChatColor.RED + "N'ayez rien dans la main pour recevoir les items.");
							} else 
								player.sendMessage(ChatColor.RED + "Vous avez 0 niveaux.");
						} else
							player.sendMessage(ChatColor.RED + "Votre île est niveau " + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(player.getName()).getLevel() + ChatColor.RED + ". Vous devez avoir une île niveau 150 pour faire ceci.");
					} else
						player.sendMessage(ChatColor.RED + "Vous devez avoir une île pour faire ceci. Pour créer une île, faites " + ChatColor.WHITE + "/is create" + ChatColor.RED + ".");
					
					
				}
				
				if(sign.getLines()[0].equals("[RARE ITEMS]"))
				{
					if(plugin.getDatas().hasIsland(player.getName()))
					{
						if(plugin.getDatas().getPlayerIsland(player.getName()).getLevel() >= 450)
						{
							if(player.getLevel() >= 13)
							{
								ItemStack item;
								Material itemType;
								
								for(int i = 0; i < 3; i++) {
									do {
										itemType = rareItems[(int)(Math.random() * rareItems.length)];
										item = getRareItem(itemType);
									} while(canGet(item, player));
									player.getInventory().addItem(item);
									player.sendMessage(ChatColor.GOLD + "Vous avez obtenu : " + ChatColor.BLUE + item.getType().name() + ChatColor.GOLD + " !");
								}
								
								player.setLevel(player.getLevel() - 13);
								player.updateInventory();
								e.setCancelled(true);
							} else
								player.sendMessage(ChatColor.RED + "Vous avez " + player.getLevel() + " niveaux, vous devez avoir 13 niveaux pour utiliser ce panneau.");
						} else
							player.sendMessage(ChatColor.RED + "Votre île est niveau " + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(player.getName()).getLevel() + ChatColor.RED + ". Vous devez avoir une île niveau 450 pour faire ceci.");
					} else
						player.sendMessage(ChatColor.RED + "Vous devez avoir une île pour faire ceci. Pour créer une île, faites " + ChatColor.WHITE + "/is create" + ChatColor.RED + ".");
					
					
				}
				
				if(sign.getLines()[0].equals("[BEST ITEMS]"))
				{
					if(plugin.getDatas().hasIsland(player.getName()))
					{
						if(plugin.getDatas().getPlayerIsland(player.getName()).getLevel() >= 500)
						{
							if(player.getLevel() >= 20)
							{
								if(player.getInventory().getItemInHand().getType() == Material.AIR)
								{
									ItemStack item;
									Material itemType;
									
									do {
										itemType = bestItems[(int) (Math.random() * bestItems.length)];
										item = getBestItem(itemType);
									} while(canGet(item, player));
									
									player.getInventory().setItemInHand(item);
									player.sendMessage(ChatColor.GOLD + "Vous avez obtenu : " + ChatColor.BLUE + item.getType().name() + ChatColor.GOLD + " !");

									player.setLevel(player.getLevel() - 20);
									e.setCancelled(true);
								} else
									player.sendMessage(ChatColor.RED + "N'ayez rien dans la main pour recevoir les items." + player.getItemInHand().toString() + player.getItemInHand().getType().name());
							} else
								player.sendMessage(ChatColor.RED + "Vous avez " + player.getLevel() + " niveaux, vous devez avoir 20 niveaux pour utiliser ce panneau.");
						} else
							player.sendMessage(ChatColor.RED + "Votre île est niveau " + ChatColor.WHITE + plugin.getDatas().getPlayerIsland(player.getName()).getLevel() + ChatColor.RED + ". Vous devez avoir une île niveau 500 pour faire ceci.");
					} else
						player.sendMessage(ChatColor.RED + "Vous devez avoir une île pour faire ceci. Pour créer une île, faites " + ChatColor.WHITE + "/is create" + ChatColor.RED + ".");
					
					
				}
			}
		}
	}
	
	private boolean canGet(ItemStack item, Player player) {
		if(plugin.getDatas().hasIsland(player.getName())) {
			if(item.getType() == Material.MONSTER_EGG) {
				if(item.getDurability() == 120 && !plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("VillagerFamily"))
					return false;
				else if(!plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("FriendlyRegion"))
					return false;
				else if(!plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("HorseAreAmazing") && item.getDurability() == 100)
					return false;
			} else
			if(item.getType() == Material.DIAMOND ||
				item.getType() == Material.DIAMOND_ORE ||
				item.getType() == Material.DIAMOND_BLOCK) {
					if(!plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("Wizard") &&
							!plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("Diamond!"))
						return false;
			} else
			if(item.getType() == Material.SOUL_SAND ||
				item.getType() == Material.NETHERRACK ||
				item.getType() == Material.NETHER_BRICK ||
				item.getType() == Material.NETHER_BRICK_ITEM ||
				item.getType() == Material.NETHER_BRICK_STAIRS ||
				item.getType() == Material.NETHER_FENCE ||
				item.getType() == Material.NETHER_STALK ||
				item.getType() == Material.NETHER_WARTS ||
				item.getType() == Material.GLOWSTONE ||
				item.getType() == Material.GLOWSTONE_DUST) {
					if(!plugin.getDatas().getPlayerIsland(player.getName()).getChallenges().contains("NetherPortal"))
						return false;
			}
		}
		
		return true;
	}

	private ItemStack getItemStack(Material itemType, int amount) {
		
		//Random rand = new Random();
		int data;
		ItemStack item;
		
		if(itemType == Material.DIRT) {
			
			//data = rand.nextInt(3);
			data = (int)(Math.random() * 3);
			if(data == 0)
				item = new ItemStack(itemType, amount);
			else if(data == 1)
				item = new ItemStack(Material.GRASS, amount);
			else
				item = new ItemStack(Material.MYCEL, amount);
			
		} else if(itemType == Material.STONE) {
			
			//data = rand.nextInt(7);
			data = (int)(Math.random() * 7);
			if(data == 0)
				item = new ItemStack(Material.STONE, amount);
			else if(data == 1)
				item = new ItemStack(Material.COBBLESTONE, amount);
			else if(data == 2)
				item = new ItemStack(Material.MOSSY_COBBLESTONE, amount);
			else if(data == 3)
				item = new ItemStack(Material.SMOOTH_BRICK, amount);
			else if(data == 4)
				item = new ItemStack(Material.SMOOTH_BRICK, amount, (byte) 1);
			else if(data == 5)
				item = new ItemStack(Material.SMOOTH_BRICK, amount, (byte) 2);
			else
				item = new ItemStack(Material.SMOOTH_BRICK, amount, (byte) 3);
			
		} else if(itemType == Material.SAPLING) {
			
			//data = rand.nextInt(6);
			data = (int)(Math.random() * 6);
			if(data == 0)
				item = new ItemStack(Material.SAPLING, amount);
			else if(data == 1)
				item = new ItemStack(Material.SAPLING, amount, (byte) 1);
			else if(data == 2)
				item = new ItemStack(Material.SAPLING, amount, (byte) 2);
			else if(data == 3)
				item = new ItemStack(Material.SAPLING, amount, (byte) 3);
			else if(data == 4)
				item = new ItemStack(Material.SAPLING, amount, (byte) 4);
			else
				item = new ItemStack(Material.SAPLING, amount, (byte) 5);
				
		} else if(itemType == Material.SAND) {
			
			//data = rand.nextInt(2);
			data = (int)(Math.random() * 2);
			if(data == 0)
				item = new ItemStack(Material.SAND, amount);
			else
				item = new ItemStack(Material.SAND, amount, (byte) 1);
			
		} else if(itemType == Material.GOLD_ORE) {
			
			//data = rand.nextInt(7);
			data = (int)(Math.random() * 7);
			if(data == 0)
				item = new ItemStack(Material.IRON_ORE, amount);
			else if(data == 1)
				item = new ItemStack(Material.GOLD_ORE, amount);
			else if(data == 2)
				item = new ItemStack(Material.COAL_ORE, amount);
			else if(data == 3)
				item = new ItemStack(Material.LAPIS_ORE, amount);
			else if(data == 4)
				item = new ItemStack(Material.REDSTONE_ORE, amount);
			else if(data == 5)
				item = new ItemStack(Material.DIAMOND_ORE, amount);
			else
				item = new ItemStack(Material.EMERALD_ORE, amount);
			
		} else if(itemType == Material.LOG) {
			
			//data = rand.nextInt(6);
			data = (int)(Math.random() * 6);
			if(data == 4)
				item = new ItemStack(Material.LOG_2, amount);
			else if(data == 5)
				item = new ItemStack(Material.LOG_2, amount, (byte) 1);
			else
				item = new ItemStack(Material.LOG, amount, (byte) data);
			
		} else if(itemType == Material.SANDSTONE) {
			
			//data = rand.nextInt(3);
			data = (int)(Math.random() * 4);
			if(data == 3)
				item = new ItemStack(Material.BOOKSHELF, amount);
			else
				item = new ItemStack(Material.SANDSTONE, amount, (byte) data);
		
		} else if(itemType == Material.LONG_GRASS || itemType == Material.RED_ROSE) {
			
			//data = rand.nextInt(3);
			if((int)(Math.random()*2) == 0) {
				data = (int)(Math.random() * 3);
				if(data == 0) {
					item = new ItemStack(Material.DEAD_BUSH, amount);
				} else
					item = new ItemStack(Material.LONG_GRASS, amount, (byte) data);
			} else {
				//data = rand.nextInt(10);
				data = (int)(Math.random() * 10);
				if(data == 9)
					item = new ItemStack(Material.YELLOW_FLOWER, amount);
				else
					item = new ItemStack(Material.RED_ROSE, amount, (byte) data);
			}
		
		} else if(itemType == Material.WOOL || itemType == Material.STAINED_GLASS || itemType == Material.STAINED_CLAY) {
			
			//data = rand.nextInt(16);
			data = (int)(Math.random() * 16);
			item = new ItemStack(itemType, amount, (byte) data);
			
		/*} else if(itemType == Material.RED_ROSE) {
			
			//data = rand.nextInt(10);
			data = (int)(Math.random() * 10);
			if(data == 9)
				item = new ItemStack(Material.YELLOW_FLOWER, amount);
			else
				item = new ItemStack(Material.RED_ROSE, amount, (byte) data);*/
			
		} else if(itemType == Material.NETHERRACK) {
			
			//data = rand.nextInt(5);
			data = (int)(Math.random() * 5);
			if(data == 0)
				item = new ItemStack(Material.NETHERRACK, amount);
			else if(data == 1)
				item = new ItemStack(Material.SOUL_SAND, amount);
			else if(data == 2)
				item = new ItemStack(Material.GLOWSTONE, amount);
			else if(data == 3)
				item = new ItemStack(Material.NETHER_BRICK, amount);
			else if(data == 4)
				item = new ItemStack(Material.QUARTZ_ORE, amount);
			else
				item = new ItemStack(Material.OBSIDIAN, amount);
			
		} else if(itemType == Material.ICE) {
			
			//data = rand.nextInt(2);
			data = (int)(Math.random() * 2);
			if(data == 0)
				item = new ItemStack(Material.ICE, amount);
			else
				item = new ItemStack(Material.PACKED_ICE, amount);
			
		} else if(itemType == Material.SNOW_BALL) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0) 
				item = new ItemStack(Material.SNOW_BALL, amount);
			else if(data == 1)
				item = new ItemStack(Material.SLIME_BALL, amount);
			else if(data == 2)
				item = new ItemStack(Material.EGG, amount);
			else
				item = new ItemStack(Material.ENDER_PEARL, amount);
			
		} else if(itemType == Material.NOTE_BLOCK) {
			
			//data = rand.nextInt(8);
			data = (int)(Math.random() * 8);
			if(data == 0)
				item = new ItemStack(Material.NOTE_BLOCK , amount);
			else if(data == 1)
				item = new ItemStack(Material.PISTON_BASE , amount);
			else if(data == 2)
				item = new ItemStack(Material.PISTON_STICKY_BASE , amount);
			else if(data == 3)
				item = new ItemStack(Material.RAILS , amount);
			else if(data == 4)
				item = new ItemStack(Material.TNT , amount);
			else if(data == 5)
				item = new ItemStack(Material.DIODE , amount);
			else if(data == 6)
				item = new ItemStack(Material.IRON_FENCE , amount);
			else
				item = new ItemStack(Material.REDSTONE_COMPARATOR , amount);
			
		} else if(itemType == Material.BROWN_MUSHROOM) {
			
			//data = rand.nextInt(9);
			data = (int)(Math.random() * 9);
			if(data == 0)
				item = new ItemStack(Material.BROWN_MUSHROOM , amount);
			else if(data == 1)
				item = new ItemStack(Material.RED_MUSHROOM , amount);
			else if(data == 2)
				item = new ItemStack(Material.CACTUS , amount);
			else if(data == 3)
				item = new ItemStack(Material.SUGAR_CANE , amount);
			else if(data == 4)
				item = new ItemStack(Material.PUMPKIN , amount);
			else if(data == 5)
				item = new ItemStack(Material.MELON , amount);
			else if(data == 6)
				item = new ItemStack(Material.WATER_LILY , amount);
			else if(data == 7)
				item = new ItemStack(Material.INK_SACK , amount, (byte) 3);
			else if(data == 7)
				item = new ItemStack(Material.CARROT_ITEM , amount);
			else
				item = new ItemStack(Material.POTATO_ITEM , amount);
			
		} else if(itemType == Material.NETHER_STALK) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0)
				item = new ItemStack(Material.NETHER_STALK , amount);
			else if(data == 1)
				item = new ItemStack(Material.BLAZE_ROD , amount);
			else if(data == 2)
				item = new ItemStack(Material.GHAST_TEAR , amount);
			else
				item = new ItemStack(Material.MAGMA_CREAM , amount);
			
		} else {
			item = new ItemStack(itemType, amount);
		}
		
		return item;
	}

	private ItemStack getRareItem(Material itemType) {
		ItemStack item;
		//Random rand = new Random();
		int data;
		
		if(itemType == Material.MONSTER_EGG) {
			
			//data = rand.nextInt(9);
			data = (int)(Math.random() * 9);
			if(data == 0)
				item = new ItemStack(itemType, 1, (byte) 90);
			else if(data == 1)
				item = new ItemStack(itemType, 1, (byte) 91);
			else if(data == 2)
				item = new ItemStack(itemType, 1, (byte) 92);
			else if(data == 3)
				item = new ItemStack(itemType, 1, (byte) 93);
			else if(data == 4)
				item = new ItemStack(itemType, 1, (byte) 95);
			else if(data == 5)
				item = new ItemStack(itemType, 1, (byte) 96);
			else if(data == 6)
				item = new ItemStack(itemType, 1, (byte) 98);
			else if(data == 7)
				item = new ItemStack(itemType, 1, (byte) 100);
			else
				item = new ItemStack(itemType, 1, (byte) 120);
			
		} else if(itemType == Material.BREWING_STAND_ITEM) {
			
			//data = rand.nextInt(6);
			data = (int)(Math.random() * 6);
			if(data == 0)
				item = new ItemStack(Material.BREWING_STAND_ITEM);
			else if(data == 1)
				item = new ItemStack(Material.CAULDRON_ITEM);
			else if(data == 2)
				item = new ItemStack(Material.ANVIL, 1, (byte) 2);
			else if(data == 3)
				item = new ItemStack(Material.HOPPER);
			else if(data == 4)
				item = new ItemStack(Material.JUKEBOX);
			else
				item = new ItemStack(Material.ENDER_CHEST);
				
		} else if(itemType == Material.GOLDEN_APPLE) {
			
			//data = rand.nextInt(3);
			data = (int)(Math.random() * 3);
			if(data == 0)
				item = new ItemStack(Material.GOLDEN_APPLE);
			else if(data == 1)
				item = new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1);
			else
				item = new ItemStack(Material.GOLDEN_CARROT);
			
		} else if(itemType == Material.REDSTONE_BLOCK) {
			
			//data = rand.nextInt(5);
			data = (int)(Math.random() * 5);
			if(data == 0)
				item = new ItemStack(Material.COMPASS);
			else if(data == 1)
				item = new ItemStack(Material.WATCH);
			else if(data == 2)
				item = new ItemStack(Material.NAME_TAG);
			else if(data == 3)
				item = new ItemStack(Material.LAVA_BUCKET);
			else
				item = new ItemStack(Material.REDSTONE_BLOCK);
			
		} else if(itemType == Material.IRON_BARDING) {
			
			//data = rand.nextInt(2);
			data = (int)(Math.random() * 2);
			if(data == 0)
				item = new ItemStack(Material.IRON_BARDING);
			else
				item = new ItemStack(Material.GOLD_BARDING);
			
		} else if(itemType == Material.IRON_PICKAXE) {
			
			//data = rand.nextInt(5);
			data = (int)(Math.random() * 5);
			if(data == 0)
				item = new ItemStack(Material.IRON_PICKAXE);
			else if(data == 1)
				item = new ItemStack(Material.IRON_AXE);
			else if(data == 2)
				item = new ItemStack(Material.IRON_SPADE);
			else
				item = new ItemStack(Material.IRON_SWORD);
			
		} else if(itemType == Material.IRON_HELMET) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0)
				item = new ItemStack(Material.IRON_HELMET);
			else if(data == 1)
				item = new ItemStack(Material.IRON_CHESTPLATE);
			else if(data == 2)
				item = new ItemStack(Material.IRON_LEGGINGS);
			else
				item = new ItemStack(Material.IRON_BOOTS);
			
		} else if(itemType == Material.GOLD_HELMET) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0)
				item = new ItemStack(Material.GOLD_HELMET);
			else if(data == 1)
				item = new ItemStack(Material.GOLD_CHESTPLATE);
			else if(data == 2)
				item = new ItemStack(Material.GOLD_LEGGINGS);
			else
				item = new ItemStack(Material.GOLD_BOOTS);
			
		} else {
			item = new ItemStack(itemType);
		}
		
		return item;
	}

	private ItemStack getBestItem(Material itemType) {
		ItemStack item;
		//Random rand = new Random();
		int data;
		
		if(itemType == Material.DIAMOND_BLOCK) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0)
				item = new ItemStack(Material.DIAMOND_BLOCK);
			else if(data == 1)
				item = new ItemStack(Material.IRON_BLOCK);
			else if(data == 2)
				item = new ItemStack(Material.GOLD_BLOCK);
			else
				item = new ItemStack(Material.EMERALD_BLOCK);
			
		}else if(itemType == Material.ANVIL) {
			
			//data = rand.nextInt(4);
			data = (int)(Math.random() * 4);
			if(data == 0)
				item = new ItemStack(Material.ANVIL);
			else if(data == 1)
				item = new ItemStack(Material.ANVIL, 1, (byte) 1);
			else if(data == 2)
				item = new ItemStack(Material.ENCHANTMENT_TABLE);
			else
				item = new ItemStack(Material.MOB_SPAWNER);
			
		} else if(itemType == Material.DIAMOND_PICKAXE) {
			
			//data = rand.nextInt(5);
			data = (int)(Math.random() * 5);
			if(data == 0)
				item = new ItemStack(Material.DIAMOND_PICKAXE);
			else if(data == 1)
				item = new ItemStack(Material.DIAMOND_AXE);
			else if(data == 2)
				item = new ItemStack(Material.DIAMOND_SPADE);
			else
				item = new ItemStack(Material.DIAMOND_SWORD);
			
		} else if(itemType == Material.DIAMOND_HELMET) {
			
			//data = rand.nextInt(5);
			data = (int)(Math.random() * 5);
			if(data == 0)
				item = new ItemStack(Material.DIAMOND_HELMET);
			else if(data == 1)
				item = new ItemStack(Material.DIAMOND_CHESTPLATE);
			else if(data == 2)
				item = new ItemStack(Material.DIAMOND_LEGGINGS);
			else if(data == 3)
				item = new ItemStack(Material.DIAMOND_BOOTS);
			else
				item = new ItemStack(Material.DIAMOND_BARDING);
			
		} else if(itemType == Material.ENCHANTED_BOOK) {
			
			item = new ItemStack(Material.ENCHANTED_BOOK);
			//data = rand.nextInt(3);
			data = (int)(Math.random() * 3);
			EnchantmentStorageMeta enchantStorage = (EnchantmentStorageMeta) item.getItemMeta();
			for(int i = 0; i < data + 1; i++) {
				enchantStorage.addStoredEnchant(Enchantment.values()[/*rand.nextInt(Enchantment.values().length)*/(int)(Math.random()*Enchantment.values().length)], /*rand.nextInt(3) + 3*/(int)(Math.random()*3+3), true);
				item.setItemMeta(enchantStorage);
			}
			
		} else if(itemType == Material.MOB_SPAWNER) {
			item = new ItemStack(Material.MOB_SPAWNER);
		} else {
			item = new ItemStack(itemType);
		}
		
		return item;
	}



}
