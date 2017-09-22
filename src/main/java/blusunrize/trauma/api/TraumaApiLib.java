/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.util.DamageSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Lib Class, storing various constants
 *
 * @author BluSunrize
 * @since 22.09.2017
 */
public class TraumaApiLib
{
	public static Set<String> FALL_DAMAGES;

	public static boolean isFallDamage(DamageSource damageSource)
	{
		return damageSource!=null && FALL_DAMAGES.contains(damageSource.damageType);
	}

	public static Map<EnumLimb, int[]> RECOVERY_TIMES;
}
