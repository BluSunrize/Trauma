/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common;

import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
@Config(modid = Trauma.MODID)
public class TraumaConfig
{
	@Comment("A list of damages that count as falling and may break legs")
	public static String[] fallDamages = {"fall"};

	public static boolean isFallDamage(DamageSource damageSource)
	{
		if(damageSource!=null)
			for(String s : fallDamages)
				if(s.equals(damageSource.damageType))
					return true;
		return false;
	}
}
