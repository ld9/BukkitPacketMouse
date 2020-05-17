package ch.dlyn.mouse;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (Mouse.notSneakingPlayers.contains(p.getUniqueId())) {
			p.sendMessage(ChatColor.GOLD + "Sneak is currently toggled " + ChatColor.DARK_GREEN + "on");
		} else {
			if (p.hasPermission("mouse.sneak")) {
				p.sendMessage(ChatColor.GOLD + "Sneak is currently toggled " + ChatColor.DARK_RED + "off");
			}
		}
	}
	
}
