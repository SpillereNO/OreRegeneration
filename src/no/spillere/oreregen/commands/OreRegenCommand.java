package no.spillere.oreregen.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.spillere.oreregen.OreRegenPlugin;

public class OreRegenCommand implements CommandExecutor {

    private OreRegenPlugin plugin;

    public OreRegenCommand(OreRegenPlugin worldKeeperPlugin) {
        plugin = worldKeeperPlugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        if (!(s instanceof Player) || s.hasPermission("oreregen.cmd")) {

            if (args.length != 1) {
                s.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + "/oreregen <stats/reload>");

            } else if (args[0].equalsIgnoreCase("stats")) {

                s.sendMessage(ChatColor.DARK_GRAY + "-------------" + ChatColor.GOLD + "{ Statistics }" + ChatColor.DARK_GRAY + "--------------");

                plugin.ConfigHandler.getBlockList().forEach((m) -> {
                    s.sendMessage(ChatColor.YELLOW + m.toString() + ": " + ChatColor.GREEN + plugin.StatsHandler.getMinedOres(m) + ChatColor.RESET + "/"
                            + ChatColor.GREEN + plugin.StatsHandler.getRegenOres(m) + ChatColor.RESET + " (mined/regenerated)");
                });

                s.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------");

            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                s.sendMessage(ChatColor.GREEN + "You successfully reloaded the configuration.");

            } else {
                s.sendMessage(ChatColor.RED + "You entered a invalid argument!");
            }

        } else {
            s.sendMessage(ChatColor.RED + "You don't have permission to perform this command!");
        }

        return true;
    }

}
