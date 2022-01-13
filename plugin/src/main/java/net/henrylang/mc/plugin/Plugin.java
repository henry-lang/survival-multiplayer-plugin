package net.henrylang.mc.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.henrylang.mc.plugin.commands.ScoreboardToggle;
import net.henrylang.mc.plugin.events.OnPlayerJoin;
import net.henrylang.mc.plugin.events.OnPlayerLeave;
import net.henrylang.mc.plugin.scoreboard.MainScoreboard;

public class Plugin extends JavaPlugin {
	public static Plugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerLeave(), this);
		
		getCommand("sbtoggle").setExecutor(new ScoreboardToggle());
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			MainScoreboard.create(player);
			
			UUID uuid = player.getUniqueId();
			MainScoreboard.getState(uuid).updatePlayerCountState();
		}
	}
	
	@Override
	public void onDisable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			MainScoreboard.dispose(player);
		}
	}
}
