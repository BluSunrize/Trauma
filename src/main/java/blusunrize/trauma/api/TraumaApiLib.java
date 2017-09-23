/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import blusunrize.trauma.api.effects.ITraumaEffect;
import com.google.common.collect.ArrayListMultimap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lib Class, storing various constants
 *
 * @author BluSunrize
 * @since 22.09.2017
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TraumaApiLib
{
	public static Set<String> FALL_DAMAGES;

	/**
	 * @param damageSource given damagesource
	 * @return true, if the source is to be considered fall damage
	 */
	public static boolean isFallDamage(@Nullable DamageSource damageSource)
	{
		return damageSource!=null && FALL_DAMAGES.contains(damageSource.damageType);
	}

	public static Map<EnumLimb, int[]> RECOVERY_TIMES;

	/**
	 * @param limb the queried limh
	 * @param state the state of the queried limb
	 * @return the recovery time. Long because it may very well be a long time
	 */
	public static int getRecoveryTime(EnumLimb limb, EnumTraumaState state)
	{
		if(state==EnumTraumaState.NONE)
			return 0;
		return RECOVERY_TIMES.get(limb)[state.ordinal()-1];
	}

	private static ArrayListMultimap<ImmutablePair<EnumLimb,EnumTraumaState>, ITraumaEffect> EFFECT_MUTLIMAP = ArrayListMultimap.create();

	/**
	 * Registers a ITraumaEffect for the relevant limb condition
	 * @param limb the limb this can apply to
	 * @param state the state of the limb this applies in
	 * @param effect the effect to register
	 */
	public static void registerEffect(EnumLimb limb, EnumTraumaState state, ITraumaEffect effect)
	{
		EFFECT_MUTLIMAP.put(ImmutablePair.of(limb, state), effect);
	}

	/**
	 * @param limb the limb queried
	 * @param state the state of the limb
	 * @return a list of all registered ITraumaEffects
	 */
	public static List<ITraumaEffect> getRegisteredEffects(EnumLimb limb, EnumTraumaState state)
	{
		return EFFECT_MUTLIMAP.get(ImmutablePair.of(limb, state));
	}
}
