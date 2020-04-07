package no.spillere.oreregen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import no.spillere.oreregen.commands.OreRegenCommand;
import no.spillere.oreregen.handlers.*;
import no.spillere.oreregen.listeners.*;

public class OreRegeneration extends JavaPlugin {

	public FileConfiguration config;

	public int configVersion = 1;

	public OreRegenHandler OreRegenHandler;
	public final ConfigHandler ConfigHandler = new ConfigHandler(this);
	public final StatsHandler StatsHandler = new StatsHandler(this);

	public final OreListener OreListener = new OreListener(this);

	public void onEnable() {
		loadListeners();
		getDataFolder().mkdir();
		loadConfig();
		registerCommands();

		// Start ore re-generator task
		OreRegenHandler = new OreRegenHandler(this, Bukkit.getWorlds().get(0));
		OreRegenHandler.startOreRegenerator();
	}

	public void loadListeners() {
		getServer().getPluginManager().registerEvents(OreListener, this);
	}

	public int getRandomNumber(int start, int stop){
		Random r = new Random();
		int Low = start;
		int High = stop;
		int R = r.nextInt(High-Low) + Low;
		return R;
	}

	private void loadConfig() {
		config = getConfig();
		config.options().copyDefaults(true);
		config.addDefault("Config Version", 0);
		ConfigHandler.exportConfig();
	}

	private void registerCommands() {
		registerCommand("oreregen", new OreRegenCommand(this), "oreregeneration", "oreregenerate");
	}

	public void registerCommand(String name, CommandExecutor executor, String... aliases) {
		try {
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);

			PluginCommand command = constructor.newInstance(name, this);

			command.setExecutor(executor);
			command.setAliases(Lists.newArrayList(aliases));
			if (executor instanceof TabCompleter) {
				command.setTabCompleter((TabCompleter) executor);
			}
			this.getCommandMap().register("blowableobsidians", command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CommandMap getCommandMap() {
		try {
			org.bukkit.Server server = Bukkit.getServer();
			Field commandMap = server.getClass().getDeclaredField("commandMap");
			commandMap.setAccessible(true);
			return (CommandMap) commandMap.get(server);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}
}