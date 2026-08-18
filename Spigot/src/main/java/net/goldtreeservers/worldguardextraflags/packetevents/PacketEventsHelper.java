package net.goldtreeservers.worldguardextraflags.packetevents;

import org.bukkit.plugin.Plugin;

import com.github.retrooper.packetevents.PacketEvents;

import lombok.Getter;
import net.goldtreeservers.worldguardextraflags.WorldGuardExtraFlagsPlugin;

public class PacketEventsHelper
{
	@Getter private final WorldGuardExtraFlagsPlugin plugin;
	@Getter private final Plugin packetEventsPlugin;

	public PacketEventsHelper(WorldGuardExtraFlagsPlugin plugin, Plugin packetEventsPlugin)
	{
		this.plugin = plugin;
		this.packetEventsPlugin = packetEventsPlugin;
	}

	public void onEnable()
	{
		PacketEvents.getAPI().getEventManager().registerListener(new RemoveEffectPacketListener());
	}
}
