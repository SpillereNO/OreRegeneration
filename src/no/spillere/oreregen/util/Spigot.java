package no.spillere.oreregen.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import no.spillere.oreregen.OreRegeneration;

public class Spigot {

    public static void generateOreVein(World world, int x, int z, Material type, int veinSize) {
        Bukkit.getScheduler().runTask(OreRegeneration.instance, () -> {
            Chunk chunk = world.getChunkAt(x, z);
            OreRegeneration.instance.OreRegenHandler.generateOreVein(chunk, type, veinSize);
        });
    }

}