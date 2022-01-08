package no.spillere.oreregen.util;

import org.bukkit.Material;
import org.bukkit.World;

import no.spillere.oreregen.OreRegenPlugin;

public class Paper {

    public static void generateOreVein(World world, int x, int z, Material type, int veinSize) {
        world.getChunkAtAsync(x, z, false, (chunk) -> {
            if (chunk == null) chunk = world.getChunkAt(x, z);
            OreRegenPlugin.instance.OreRegenHandler.generateOreVein(chunk, type, veinSize);
        });
    }

}