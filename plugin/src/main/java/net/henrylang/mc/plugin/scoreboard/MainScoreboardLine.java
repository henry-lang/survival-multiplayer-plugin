package net.henrylang.mc.plugin.scoreboard;

import org.bukkit.ChatColor;

public enum MainScoreboardLine {
	
	TOP_PADDING(ChatColor.RESET, " ", 9, false),
	ONLINE_PLAYERS(ChatColor.WHITE, ChatColor.RED, "Players:", 8, true),
	MID_PADDING(ChatColor.RESET, "  ", 7, false),
	X_COORD(ChatColor.WHITE, ChatColor.RED, "X: ", 6, true),
	Y_COORD(ChatColor.WHITE, ChatColor.RED, "Y: ", 5, true),
	Z_COORD(ChatColor.WHITE, ChatColor.RED, "Z: ", 4, true);
	
	public static class Formatting {
		public ChatColor color;
		public ChatColor styling;
		
		public Formatting(ChatColor color, ChatColor styling) {
			this.color = color;
			this.styling = styling;
		}
	}
	
	ChatColor prefixColor;
	ChatColor valueColor; // May not be used if isValue = false
	String prefix;
	int num;
	boolean isValue;
	
	MainScoreboardLine(ChatColor prefixColor, ChatColor valueColor, String prefix, int num, boolean isValue) {
		this.prefixColor = prefixColor;
		this.valueColor = valueColor;
		this.prefix = prefix;
		this.num = num;
		this.isValue = isValue;
	}
	
	MainScoreboardLine(ChatColor prefixColor, String prefix, int num, boolean isValue) {
		this.prefixColor = prefixColor;
		this.valueColor = null;
		this.prefix = prefix;
		this.num = num;
		this.isValue = isValue;
	}
	
	public String getFormattedPrefix() {
		return new StringBuilder()
				.append(this.prefixColor)
				.append(" ")
				.append(this.prefix)
				.append(" ")
				.toString();
	}
	
	public <T> String getFormattedValue(T value) {
		return new StringBuilder()
				.append(this.valueColor)
				.append(value)
				.toString();
	}
}