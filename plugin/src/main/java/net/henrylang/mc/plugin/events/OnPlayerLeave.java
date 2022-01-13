package net.henrylang.mc.plugin.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.henrylang.mc.plugin.scoreboard.MainScoreboard;

public class OnPlayerLeave implements Listener {
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		MainScoreboard.dispose(event.getPlayer());

		for(Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			
			MainScoreboard.State state = MainScoreboard.getState(uuid);
			if(state != null) {
				state.updatePlayerCountState();
			}
		}
	}
}
