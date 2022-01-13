package net.henrylang.mc.plugin.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.henrylang.mc.plugin.scoreboard.MainScoreboard;

public class OnPlayerJoin implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MainScoreboard.create(event.getPlayer());
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			MainScoreboard.getState(uuid).updatePlayerCountState();
		}
	}
}
