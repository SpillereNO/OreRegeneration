package no.spillere.oreregen.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.google.common.collect.Maps;

import no.spillere.oreregen.OreRegeneration;
import no.spillere.oreregen.util.OreVein;

public class OreRegenHandler {

	public Map<UUID, OreVein> readyLocations = Maps.newConcurrentMap();

	public Map<Material, Integer> minedOres = Maps.newConcurrentMap();

	private OreRegeneration plugin;

	public OreRegenHandler(OreRegeneration worldKeeperPlugin){
		plugin = worldKeeperPlugin;
	}


	public void startOreRegenerator() {

		plugin.ConfigHandler.getBlockList().forEach((m) -> {
			minedOres.put(m, 0);
		});


		// Start task
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

			// Count ready locations
			HashMap<Material, Integer> countMap = new HashMap<Material, Integer>();
			plugin.ConfigHandler.getBlockList().forEach((m) -> {
				countMap.put(m, 0);
			});
			readyLocations.forEach((k,oreVein) -> {
				Material m = oreVein.getType();
				int value = countMap.get(m)+1;
				countMap.put(m, value);
			});

			// Add ready locations
			countMap.forEach((m,amount) -> {
				while (amount < 20) {
					OreVein oreVein = prepareOreVein(m);
					readyLocations.put(UUID.randomUUID(), oreVein);
					amount++;
				}
			});

			// Generate ore veins
			minedOres.forEach((material,amount) -> {

				// Fill out ores
				if (amount > 0 && plugin.ConfigHandler.debug()) System.out.println(material.toString() + ": " + amount + " ores in generating queue.");
				OreVein oreVein = getReadyOreVein(material);
				while (amount > 0) {
					fillOreVein(oreVein);
					amount -= oreVein.getCoords().size();
					oreVein = getReadyOreVein(material);
				}

			});


		}, 200, 200);
	}

	private OreVein getReadyOreVein(Material m) {
		Iterator<Map.Entry<UUID, OreVein>> it = readyLocations.entrySet().iterator();
		while (it.hasNext()) {
			OreVein oreVein = it.next().getValue();
			if (oreVein.getType() != m) continue;
			it.remove();
			return oreVein;
		}
		return null;
	}

	private OreVein prepareOreVein(Material m) {

		World world = Bukkit.getWorlds().get(0);
		int coordY = getRandomY(m);
		int veinSize = getVeinSize(m);

		List<int[]> coords = new ArrayList<int[]>();

		Block b = null;

		int i = 0;
		while (i < veinSize) {
			if (b == null || i == 0 || coords.size() == 0) {
				b = getRandomBlock(world, coordY);
			}
			int[] coord = {b.getX(), b.getY(), b.getZ()};
			if (coords.contains(coord)) continue;

			if (isFillable(b.getType())) {
				coords.add(coord);
				i++;
			}
			BlockFace direction = getRandomDirection();
			b = b.getRelative(direction);
		}

		OreVein oreVein = new OreVein(m, coords);
		return oreVein;
	}

	private BlockFace getRandomDirection() {
		BlockFace[] faces = BlockFace.values();
		return faces[plugin.getRandomNumber(0, faces.length-1)];
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

	private void fillOreVein(OreVein oreVein) {
		if (plugin.ConfigHandler.debug()) {
			int[] x = oreVein.getCoords().get(0);
			System.out.println("Generating " + oreVein.getCoords().size() + " " + oreVein.getType().toString() + " at " + x[0] + " " + x[1] + " " + x[2] + " ...");
		}

		minedOres.forEach((k,v) -> {
			if (k == oreVein.getType()) {
				minedOres.put(k, minedOres.get(k)-oreVein.getCoords().size());
			}
		});
		plugin.StatsHandler.addRegenOre(oreVein.getType(), oreVein.getCoords().size());
		List<Block> blocks = oreVein.getBlocks();

		Bukkit.getScheduler().runTask(plugin, () -> {

			for (Block b : blocks) {
				b.setType(oreVein.getType());
			}

		});
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

	private Block getRandomBlock(World w, int y){
		WorldBorder border = w.getWorldBorder();
		Location c = border.getCenter();
		int minX = (int) (border.getSize() / 2 * - 1) + c.getBlockX();
		int maxX = (int) (border.getSize() / 2) + c.getBlockX();
		int minZ = (int) (border.getSize() / 2 * - 1) + c.getBlockZ();
		int maxZ = (int) (border.getSize() / 2) + c.getBlockZ();
		int x = plugin.getRandomNumber(minX, maxX);
		int z = plugin.getRandomNumber(minZ, maxZ);
		if (y > 50) y = plugin.getRandomNumber(6, 40);
		Location loc = new Location(w, x, y, z);
		return loc.getBlock();
	}
}