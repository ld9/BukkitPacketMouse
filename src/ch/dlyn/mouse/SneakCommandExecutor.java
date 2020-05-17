package ch.dlyn.mouse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SneakCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be used by players.");
			
			return false;
		}
		
		Player p = (Player) sender;
		
		if (Mouse.notSneakingPlayers.contains(p.getUniqueId())) {
			Mouse.notSneakingPlayers.remove(p.getUniqueId());
		} else {
			Mouse.notSneakingPlayers.add(p.getUniqueId());
		}
		
		Mouse.writeState();

		p.sendMessage(String.format("Sneak for %s: %s.", p.getDisplayName(), (!Mouse.notSneakingPlayers.contains(p.getUniqueId()) ? "enabled" : "disabled")));
		//
		
		return false;
	}

}
