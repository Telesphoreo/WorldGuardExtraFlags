package net.goldtreeservers.worldguardextraflags.flags.helpers;

import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

public class PotionEffectTypeFlag extends Flag<PotionEffectType>
{
	public PotionEffectTypeFlag(String name)
	{
		super(name);
	}

	@Override
	public Object marshal(PotionEffectType o)
	{
		return o.getKey().toString();
	}

	@Override
	public PotionEffectType parseInput(FlagContext context) throws InvalidFlagFormat
	{
		PotionEffectType potionEffect = PotionEffectFlag.findPotionEffectType(context.getUserInput().trim());
		if (potionEffect != null)
		{
			return potionEffect;
		}

		throw new InvalidFlagFormat("Unable to find the potion effect type! Input valid namespaced ids.");
	}

	@Override
	public PotionEffectType unmarshal(Object o)
	{
		return PotionEffectFlag.findPotionEffectType(o.toString());
	}
}
