package ch.dlyn.mouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Mouse extends JavaPlugin {

	public static ArrayList<UUID> notSneakingPlayers;
	public static HashMap<UUID, UUID> playerCanSee;
	public static ProtocolManager manager;
	public static BukkitTask sneakTask;
	
	@Override
	public void onEnable() {
		getServer().getLogger().info("Enabling Mouse (Sneak Plugin)");
		
		notSneakingPlayers = new ArrayList<>();
		playerCanSee = new HashMap<>();
		
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		
		getCommand("sneak").setExecutor(new SneakCommandExecutor());
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		if (configFile.exists()) {
			for (Object uuid : this.getConfig().getList("notSneakingPlayers")) {
				notSneakingPlayers.add(UUID.fromString((String) uuid));
			}
		}
		
		
		manager = ProtocolLibrary.getProtocolManager();
		
		sneakTask = new BukkitRunnable() {
			@Override
			public void run() {
				Collection<? extends Player> players = Mouse.getPlugin(Mouse.class).getServer().getOnlinePlayers();
				
				for (Player sneaker : players) {
					for (Player looker : players) {
						if (sneaker.equals(looker)) {
							continue;
						}

						if (!EntityHideUtil.canSee(looker, sneaker, false)) {
							EntityHideUtil.hideEntity(looker, sneaker, Mouse.manager);
						} else {
							EntityHideUtil.showEntity(looker, sneaker, Mouse.manager);
						}
					}
				}
			}
		}.runTaskTimer(this, 0, 1);
		
	}
	
	@Override
	public void onDisable() {
		getServer().getLogger().info("Disabling Mouse (Sneak Plugin)");
	}
	
	public static void writeState() {
		ArrayList<String> uuidStrings = new ArrayList<>();
		for(UUID uuid : notSneakingPlayers) {
			uuidStrings.add(uuid.toString());
		}
		
		getPlugin(Mouse.class).getConfig().set("notSneakingPlayers", uuidStrings);
		getPlugin(Mouse.class).saveConfig();
	}
	
}
