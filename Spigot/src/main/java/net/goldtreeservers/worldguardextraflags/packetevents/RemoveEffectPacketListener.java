package net.goldtreeservers.worldguardextraflags.packetevents;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.sk89q.worldguard.session.Session;

import net.goldtreeservers.worldguardextraflags.wg.handlers.GiveEffectsFlagHandler;

public class RemoveEffectPacketListener extends PacketListenerAbstract
{
	public RemoveEffectPacketListener()
	{
		super(PacketListenerPriority.NORMAL);
	}

	@Override
	public void onPacketSend(PacketSendEvent event)
	{
		if (event.getPacketType() != PacketType.Play.Server.REMOVE_ENTITY_EFFECT)
		{
			return;
		}

		if (event.isCancelled())
		{
			return;
		}

		Player player = event.getPlayer();
		if (player == null || !player.isValid()) //Work around, getIfPresent is broken inside WG due to using LocalPlayer as key instead of CacheKey
		{
			return;
		}

		try
		{
			Session session = WorldGuard.getInstance().getPlatform().getSessionManager().get(WorldGuardPlugin.inst().wrapPlayer(player));

			GiveEffectsFlagHandler giveEffectsHandler = session.getHandler(GiveEffectsFlagHandler.class);
			if (giveEffectsHandler.isSupressRemovePotionPacket())
			{
				event.setCancelled(true);
			}
		}
		catch(IllegalStateException wgBug)
		{

		}
	}
}
