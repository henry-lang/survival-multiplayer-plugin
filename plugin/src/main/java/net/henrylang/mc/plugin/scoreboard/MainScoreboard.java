package net.henrylang.mc.plugin.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.henrylang.mc.plugin.Plugin;
import net.md_5.bungee.api.ChatColor;

public class MainScoreboard {
	public static class State {
		private int titleState;
		private int maxTitleState;
		private int playerCountState;
		private boolean toggleState;
		
		private Player player;
		private Scoreboard scoreboard;
		
		private int titleTask;
		private int coordTask;
		
		public State(Player player, Scoreboard scoreboard) {
			this.titleState = 0;
			this.maxTitleState = 8;
			this.playerCountState = 0;
			this.toggleState = true;
			
			this.player = player;
			this.scoreboard = scoreboard;
			
			this.startTasks();
		}
		
		public void updateTitleState() {
			if(this.titleState == this.maxTitleState) {
				this.titleState = 0;
			} else {
				this.titleState++;
			}
			
			this.scoreboard.getObjective("MainScoreboard").setDisplayName(getTitleText());
		}
		
		public void updateCoordState() {
			Location location = this.player.getLocation();
			
			String playerX = MainScoreboardLine.X_COORD.getFormattedValue(location.getBlockX());
			String playerY = MainScoreboardLine.Y_COORD.getFormattedValue(location.getBlockY());
			String playerZ = MainScoreboardLine.Z_COORD.getFormattedValue(location.getBlockZ());
			
			Team teamX = scoreboard.getTeam(MainScoreboardLine.X_COORD.name());
			teamX.setSuffix(playerX);
			
			Team teamY = scoreboard.getTeam(MainScoreboardLine.Y_COORD.name());
			teamY.setSuffix(playerY);
			
			Team teamZ = scoreboard.getTeam(MainScoreboardLine.Z_COORD.name());
			teamZ.setSuffix(playerZ);
		}
		
		public void updateToggleState() {
			this.toggleState = !this.toggleState;
			
			if(this.toggleState) {
				player.setScoreboard(scoreboard);
				this.startTasks();
			} else {
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				this.stopTasks();
			}
		}
		
		public void updatePlayerCountState() {
			this.playerCountState = Bukkit.getOnlinePlayers().size();
			String playerCount = MainScoreboardLine.ONLINE_PLAYERS.getFormattedValue(this.playerCountState);
			
			Team teamPlayerCount = this.scoreboard.getTeam(MainScoreboardLine.ONLINE_PLAYERS.name());
			teamPlayerCount.setSuffix(playerCount);
		}
		
		public String getTitleText() {
			String bc = "" + ChatColor.WHITE + ChatColor.BOLD; // BACKGROUND COLOR
			String ac = "" + ChatColor.RED + ChatColor.BOLD; // ACCENT COLOR
			String dc = "" + ChatColor.DARK_GRAY; // DARK COLOR
			
			
			String title = "";
			switch(this.titleState) {
				case 0: title = dc+" >> "+ac+"HL"+ac+"Server"+dc+" << "; break;
				case 1: title = dc+" >> "+ac+"HL"+bc+"Server"+dc+" << "; break;
				case 2: title = dc+" >> "+ac+"HL"+ac+"Server"+dc+" << "; break;
				case 3: title = dc+" >> "+ac+"HL"+bc+"S"+ac+"erver"+dc+" << "; break;
				case 4: title = dc+" >> "+ac+"HL"+bc+"Se"+ac+"rver"+dc+" << "; break;
				case 5: title = dc+" >> "+ac+"HL"+bc+"Ser"+ac+"ver"+dc+" << "; break;
				case 6: title = dc+" >> "+ac+"HL"+bc+"Serv"+ac+"er"+dc+" << "; break;
				case 7: title = dc+" >> "+ac+"HL"+bc+"Serve"+ac+"r"+dc+" << "; break;
				case 8: title = dc+" >> "+ac+"HL"+bc+"Server"+dc+" << "; break;
			}
			return title;
		}
		
		public void startTasks() {
			this.titleTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, () -> {
				this.updateTitleState();
			}, 0, 16);
			this.coordTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, () -> {
				this.updateCoordState();
			}, 0, 1);
		}
		
		public void stopTasks() {
			Bukkit.getScheduler().cancelTask(this.titleTask);
			Bukkit.getScheduler().cancelTask(this.coordTask);
		}
	}
	
	private static HashMap<UUID, State> states = new HashMap<>();
	
	public static void create(Player player) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		
		State state = new State(player, board);
		states.put(player.getUniqueId(), state);
		
		Objective objective = board.registerNewObjective("MainScoreboard", "dummy", state.getTitleText());
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(MainScoreboardLine line : MainScoreboardLine.values()) {
			addLine(board, line);
		}
		
		player.setScoreboard(board);
	}
	
	public static void addLine(Scoreboard board, MainScoreboardLine line) {
		String formattedPrefix = line.getFormattedPrefix();
		String formattedValue = line.getFormattedValue("..."); // ... is default value
		
		Objective objective = board.getObjective("MainScoreboard");
		Score score = objective.getScore(formattedPrefix);
		score.setScore(line.num);
		
		if(line.isValue) {
			Team team = board.registerNewTeam(line.name());
			team.addEntry(formattedPrefix);
			team.setSuffix(formattedValue);
		}
		
	}
	
	public static State getState(UUID uuid) {
		if(states.containsKey(uuid)) {
			return states.get(uuid);
		}
		return (State) null;
	}
	
	public static void dispose(Player player) {
		states.get(player.getUniqueId()).stopTasks();
		states.remove(player.getUniqueId());
	}
}
