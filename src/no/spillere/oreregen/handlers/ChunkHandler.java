package no.spillere.oreregen.handlers;

import org.bukkit.Material;
import org.bukkit.World;

import no.spillere.oreregen.OreRegeneration;
import no.spillere.oreregen.util.Paper;
import no.spillere.oreregen.util.Spigot;

public class ChunkHandler {

	private OreRegeneration plugin;

	public ChunkHandler(OreRegeneration worldKeeperPlugin){
		plugin = worldKeeperPlugin;
	}

	public void generateOreVein(World world, int x, int z, Material type, int veinSize) {

		if (plugin.isPaperMC) {
			Paper.generateOreVein(world, x, z, type, veinSize);

		} else {
			Spigot.generateOreVein(world, x, z, type, veinSize);
		}
	}
}