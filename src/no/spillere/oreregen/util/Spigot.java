package no.spillere.oreregen.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import no.spillere.oreregen.OreRegenPlugin;

public class Spigot {

    public static void generateOreVein(World world, int x, int z, Material type, int veinSize) {
        Bukkit.getScheduler().runTask(OreRegenPlugin.instance, () -> {
            Chunk chunk = world.getChunkAt(x, z);
            OreRegenPlugin.instance.OreRegenHandler.generateOreVein(chunk, type, veinSize);
        });
    }

}