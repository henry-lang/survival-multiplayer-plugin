package net.henrylang.mc.plugin.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.henrylang.mc.plugin.scoreboard.MainScoreboard;

public class ScoreboardToggle implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		UUID uuid = ((Player) sender).getUniqueId();
		MainScoreboard.getState(uuid).updateToggleState();
		
		return true;
	}
}
