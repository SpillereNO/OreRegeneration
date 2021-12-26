package no.spillere.oreregen.handlers;

import java.util.Map;

import org.bukkit.Material;

import com.google.common.collect.Maps;

import no.spillere.oreregen.OreRegeneration;

public class StatsHandler {

    private OreRegeneration plugin;

    public StatsHandler(OreRegeneration worldKeeperPlugin) {
        plugin = worldKeeperPlugin;
    }

    public static Map<Material, Integer> minedOres = Maps.newConcurrentMap();
    public static Map<Material, Integer> regenOres = Maps.newConcurrentMap();

    public int getMinedOres(Material m) {
        if (minedOres.containsKey(m))
            return minedOres.get(m);
        else return 0;
    }

    public int getRegenOres(Material m) {
        if (regenOres.containsKey(m))
            return regenOres.get(m);
        else return 0;
    }

    public void addMinedOre(Material m) {
        if (!plugin.ConfigHandler.recordStats()) return;

        int amount = minedOres.containsKey(m) ? minedOres.get(m) + 1 : 1;
        minedOres.put(m, amount);
    }

    public void addRegenOre(Material m, int add) {
        if (!plugin.ConfigHandler.recordStats()) return;

        int amount = regenOres.containsKey(m) ? regenOres.get(m) + add : add;
        regenOres.put(m, amount);
    }

}