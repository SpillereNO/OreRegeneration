package no.spillere.oreregen.handlers;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import com.google.common.collect.Maps;
import no.spillere.oreregen.OreRegeneration;

public class OreRegenHandler {

	public Map<Material, Integer> minedOres = Maps.newConcurrentMap();

	private OreRegeneration plugin;

	private World world;

	public OreRegenHandler(OreRegeneration worldKeeperPlugin, World world){
		plugin = worldKeeperPlugin;
		this.world = world;
	}

	public void startOreRegenerator() {

		plugin.ConfigHandler.getBlockList().forEach((m) -> {
			minedOres.put(m, 0);
		});


		// Start task
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

			// Generate ore veins
			minedOres.forEach((material,amount) -> {

				// Fill out ores
				if (amount > 0 && plugin.ConfigHandler.debug()) {
					System.out.println(material.toString() + ": " + amount + " ores in generating queue.");
				}

				while (amount > 0) {
					int veinSize = getVeinSize(material);
					int[] randomChunk = getRandomChunk();
					plugin.ChunkHandler.generateOreVein(world, randomChunk[0], randomChunk[1], material, veinSize);
					amount -= veinSize;
				}

			});


		}, 200, 200);
	}

	// Notice: Needs to be executed via ChunkHandler
	public void generateOreVein(Chunk chunk, Material m, int veinSize) {

		// Find core block of ore vein
		Block block = null;
		int tries = 3;
		while (tries > 0) {
			block = getRandomBlock(chunk, getRandomY(m));
			if (isFillable(block.getType())) break;
			else if (tries == 0) return;
			tries--;
		}

		// Create the actual ore vein
		final Block core = block;
		int i = 0;
		tries = veinSize*3;
		while (i < veinSize && tries > 0) {
			tries--;

			// Fill blocks
			if (isFillable(block.getType())) {
				block.setType(m);
				i++;
			}

			// Vein logic
			Block randomBlock  = getRandomDir(block);
			if (randomBlock == null)  block = core;
			else block = randomBlock;

		}

		// Finish
		final int regen = i;
		minedOres.forEach((k,v) -> {
			if (k == m) {
				minedOres.put(k, minedOres.get(k)-regen);
			}
		});
		plugin.StatsHandler.addRegenOre(m, regen);

		// Debug
		if (plugin.ConfigHandler.debug()) {
			System.out.println("Generated " + regen + " " + m.toString() + " at " + core.getX() + " " + core.getY() + " " + core.getZ() + ".");
		}

	}

	private int getVeinSize(Material m) {

		int from = plugin.ConfigHandler.getVeinSizeFrom(m);
		int to = plugin.ConfigHandler.getVeinSizeTo(m);

		return plugin.getRandomNumber(from, to);
	}

	private int getRandomY(Material m) {

		int from = plugin.ConfigHandler.getFromY(m);
		int to = plugin.ConfigHandler.getToY(m);

		return plugin.getRandomNumber(from, to);
	}

	private boolean isFillable(Material m) {
		switch (m) {
		case STONE:
			return true;
		case ANDESITE:
			return true;
		case DIORITE:
			return true;
		case GRANITE:
			return true;
		default:
			return false;
		}
	}

	private Block getRandomDir(Block b) {

		Chunk c = b.getChunk();
		int x = Math.floorMod(b.getX(), 16);
		int y = b.getY();
		int z = Math.floorMod(b.getZ(), 16);

		int[] mod = getRandomModifier();
		x += mod[0];
		y += mod[1];
		z += mod[2];

		if(x >= 0 && x < 16 && z >= 0 && z < 16)
			return c.getBlock(x, y, z);
		else return null;
	}

	private int[] getRandomModifier() {
		int[][] states = {{0,1,0}, {0,-1,0}, {1,0,0}, {-1,0,0}, {0,0,1}, {0,0,-1}};

		int[] modifier = states[plugin.getRandomNumber(0, states.length-1)];
		return modifier;
	}

	private Block getRandomBlock(Chunk chunk, int y){
		int x = plugin.getRandomNumber(0, 15);
		int z = plugin.getRandomNumber(0, 15);
		return chunk.getBlock(x, y, z);
	}

	private int[] getRandomChunk(){
		WorldBorder border = world.getWorldBorder();
		Location c = border.getCenter();
		int minX = (int) (border.getSize() / (16*2) * - 1) + c.getBlockX();
		int maxX = (int) (border.getSize() / (16*2)) + c.getBlockX();
		int minZ = (int) (border.getSize() / (16*2) * - 1) + c.getBlockZ();
		int maxZ = (int) (border.getSize() / (16*2)) + c.getBlockZ();

		int x = plugin.getRandomNumber(minX, maxX);
		int z = plugin.getRandomNumber(minZ, maxZ);

		int[] chunkCoords = {x, z};

		return chunkCoords;
	}
}