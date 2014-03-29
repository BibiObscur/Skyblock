package fr.bibiobscur.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateWorldCommand implements CommandExecutor {
	
	public final Plugin plugin;
	public final int SPAWN_Y;
	
    public CreateWorldCommand(Plugin plugin) {
        this.plugin = plugin;
        this.SPAWN_Y = plugin.getISLANDS_Y()+5;
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
            return false;
        }
		
		Player player = (Player) sender;

		if(!sender.hasPermission("skyblock.devcommand")) {
        	sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
        	return true;
        }
		
		if(commandLabel.equalsIgnoreCase("createskyworld")) {
			if(args.length == 1) {
				
				sender.sendMessage("Début de la génération du monde \"" + ChatColor.BLUE + args[0] + ChatColor.WHITE + "\"...");
				WorldCreator wc = new WorldCreator(args[0]);
    			wc.generator(new NullChunkGenerator());
    			wc.createWorld();
    			
    			sender.sendMessage("Détermination des coordonnées du spawn...");
    			World world = plugin.getServer().getWorld(args[0]);
    			world.setSpawnLocation(0, SPAWN_Y, 0);
    			world.setDifficulty(Difficulty.HARD);
    			world.setPVP(true);
    			
    			sender.sendMessage("Construction de la plateforme de spawn...");
    			for(int x = -2; x < 2; x++) {
    				for(int z = -2; z < 2; z++) {
    	                Block blockToChange = plugin.getServer().getWorld(args[0]).getBlockAt(x, SPAWN_Y-1, z);
    	                blockToChange.setType(Material.SMOOTH_BRICK);
    				}
    			}
    			
    			sender.sendMessage("Sauvegarde de SkyblockWorldName.txt");
    			plugin.changeWorldName(args[0]);
    			
    			sender.sendMessage("Téléportation au monde");
    			world.loadChunk(0, 0);
    			player.teleport(world.getSpawnLocation());
    			
    			sender.sendMessage(ChatColor.GOLD + "Le monde " + ChatColor.BLUE + args[0] + ChatColor.GOLD + " a été créé avec succès !");
			} else {
				sender.sendMessage(ChatColor.RED + "Utilisation de la commande : " + ChatColor.WHITE + "/createskyworld <worldname>");
			}
		}
		
		if(commandLabel.equalsIgnoreCase("createhellworld")) {
			if(args.length == 1) {
				
				sender.sendMessage("Début de la génération du monde \"" + ChatColor.BLUE + args[0] + ChatColor.WHITE + "\"...");
				WorldCreator wc = new WorldCreator(args[0]);
    			wc.generator(new NullChunkGenerator());
    			wc.environment(Environment.NETHER);
    			wc.createWorld();
    			
    			sender.sendMessage("Détermination des coordonnées du spawn...");
    			World world = plugin.getServer().getWorld(args[0]);
    			world.setSpawnLocation(0, SPAWN_Y, 0);
    			world.setDifficulty(Difficulty.HARD);
    			world.setPVP(true);
    			
    			sender.sendMessage("Construction de la plateforme de spawn...");
    			for(int x = -2; x < 2; x++) {
    				for(int z = -2; z < 2; z++) {
    	                Block blockToChange = plugin.getServer().getWorld(args[0]).getBlockAt(x, SPAWN_Y-1, z);
    	                blockToChange.setType(Material.NETHERRACK);
    				}
    			}
    			
    			sender.sendMessage("Sauvegarde de HellWorldName.txt");
    			plugin.changeHellWorldName(args[0]);
    			
    			sender.sendMessage("Téléportation au monde");
    			world.loadChunk(0, 0);
    			player.teleport(world.getSpawnLocation());
    			
    			sender.sendMessage(ChatColor.GOLD + "Le monde " + ChatColor.BLUE + args[0] + ChatColor.GOLD + " a été créé avec succès !");
			} else {
				sender.sendMessage(ChatColor.RED + "Utilisation de la commande : " + ChatColor.WHITE + "/createskyworld <worldname>");
			}
		}
		
		return false;
	}

}
