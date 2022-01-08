package no.spillere.oreregen.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import no.spillere.oreregen.OreRegenPlugin;

public class OreListener implements org.bukkit.event.Listener {

    OreRegenPlugin plugin;

    public OreListener(OreRegenPlugin worldKeeperPlugin) {
        plugin = worldKeeperPlugin;
    }

    private List<Block> placedBlocks = new ArrayList<Block>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            Block b = e.getBlock();
            Material m = b.getType();

            if (!b.getWorld().getName().equals(Bukkit.getWorlds().get(0).getName())) return;
            if (placedBlocks.contains(b)) return;
            if (plugin.ConfigHandler.getAboveHeight() <= b.getY()) return;

            if (plugin.ConfigHandler.isTypeActive(m)) {
                // Add ore
                plugin.OreRegenHandler.minedOres.put(m, plugin.OreRegenHandler.minedOres.get(m) + 1);
                plugin.StatsHandler.addMinedOre(m);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.isCancelled() && plugin.ConfigHandler.checkBlockPlace()) {
            Block b = e.getBlock();
            Material m = b.getType();

            if (!b.getWorld().getName().equals(Bukkit.getWorlds().get(0).getName())) return;

            if (plugin.ConfigHandler.isTypeActive(m)) {
                placedBlocks.add(b);
            }
        }
    }
}