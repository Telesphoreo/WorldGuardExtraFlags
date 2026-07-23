package net.goldtreeservers.worldguardextraflags;

import com.sk89q.worldedit.entity.Player;

public class CommandUtils
{
	public static String formatCommand(String command, Player player)
	{
		return command
				.replace("%username%", player.getName())
				.replace("%uuid%", player.getUniqueId().toString());
	}
}
