/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.effects;

import net.minecraft.potion.Potion;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

/**
 * Custom HashMap implementation to allow easy modification of effects
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
@ParametersAreNonnullByDefault
public class PotionEffectMap extends HashMap<Potion, Integer>
{
	/**
	 * Modifies current value for given potion, adds effect to map if it doesn't exist otherwise
	 *
	 * @param potion given Potion
	 * @param mod    modifier
	 */
	public void modifyEffect(Potion potion, int mod)
	{
		if(!this.containsKey(potion))
			this.put(potion, mod);
		else
		{
			int val = this.get(potion);
			this.put(potion, val+mod);
		}
	}
}
