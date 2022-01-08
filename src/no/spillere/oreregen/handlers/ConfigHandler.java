package no.spillere.oreregen.handlers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;

import no.spillere.oreregen.OreRegenPlugin;
import no.spillere.oreregen.util.Util;

public class ConfigHandler {

    private OreRegenPlugin plugin;

    public ConfigHandler(OreRegenPlugin worldKeeperPlugin) {
        plugin = worldKeeperPlugin;
    }

    public void exportConfig() {
        try {
            if (plugin.getConfig().getInt("Config Version") < plugin.configVersion) {
                URL inputUrl = plugin.getClass().getResource("/config.yml");
                File dest = new File(plugin.getDataFolder() + File.separator + "config.yml");

                if (dest.exists()) {
                    File renameTo = new File(dest.getParent() + File.separator + "old_config.yml");
                    if (renameTo.exists()) renameTo.delete();
                    dest.renameTo(renameTo);
                    System.out.println("[OreRegeneration] Previous configuration file was renamed to old_config.yml.");
                }

                Util.copyUrlToFile(inputUrl, dest);

                System.out.println("[OreRegeneration] Configuration file was successfully exported to plugin folder.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Material> getBlockList() {
        List<Material> blockList = new ArrayList<Material>();

        Set<String> blockTypes = plugin.getConfig().getConfigurationSection("Active Ore Types").getKeys(false);

        blockTypes.forEach((k) -> {
            blockList.add(Material.valueOf(k.toUpperCase()));
        });

        return blockList;
    }

    public boolean recordStats() {
        return plugin.getConfig().getBoolean("Record Stats");
    }

    public int getAboveHeight() {
        return plugin.getConfig().getInt("Ignore.Above Y level");
    }

    public boolean isTypeActive(Material type) {
        return getBlockList().contains(type);
    }

    public boolean debug() {
        return plugin.getConfig().getBoolean("Debug Mode");
    }

    public int getFromY(Material m) {
        return plugin.getConfig().getInt("Active Ore Types." + m.toString() + ".From Y");
    }

    public int getToY(Material m) {
        return plugin.getConfig().getInt("Active Ore Types." + m.toString() + ".To Y");
    }

    public int getVeinSizeFrom(Material m) {
        String genY = plugin.getConfig().getString("Active Ore Types." + m.toString() + ".Vein Size");
        if (genY.contains("-")) genY.replace("-", "~");
        if (genY.contains("~")) {
            return Integer.parseInt(genY.split("~")[0]);
        }
        return Integer.parseInt(genY);
    }

    public int getVeinSizeTo(Material m) {
        String genY = plugin.getConfig().getString("Active Ore Types." + m.toString() + ".Vein Size");
        if (genY.contains("-")) genY.replace("-", "~");
        if (genY.contains("~")) {
            return Integer.parseInt(genY.split("~")[1]);
        }
        return Integer.parseInt(genY);
    }

    public boolean checkBlockPlace() {
        return plugin.getConfig().getBoolean("Ignore.Placed blocks");
    }

}
