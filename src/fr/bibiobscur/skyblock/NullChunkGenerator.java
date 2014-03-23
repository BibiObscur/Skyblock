package fr.bibiobscur.skyblock;

import java.util.Random;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.World;

public class NullChunkGenerator extends ChunkGenerator{
	
	public byte[] generate(World world, Random random, int cx, int cz) {
		return new byte[65536];
	}
}
