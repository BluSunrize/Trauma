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
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
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
	public static Set<String> IGNORED_DAMAGES;

	/**
	 * @param damageSource given damagesource
	 * @return true, if the source is set in teh config to be ignored
	 */
	public static boolean isIgnoredDamage(@Nullable DamageSource damageSource)
	{
		return damageSource!=null && IGNORED_DAMAGES.contains(damageSource.damageType);
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

	private static Map<String, IDamageAdapter> DAMAGE_ADAPTERS = new HashMap<>();

	/**
	 * Registers a damage adapter to any source of the given name
	 * @param damageSource the name of the source, e.g. "fall"
	 * @param adapter the adapter
	 */
	public static void registerDamageAdapter(String damageSource, IDamageAdapter adapter)
	{
		DAMAGE_ADAPTERS.put(damageSource, adapter);
	}

	/**
	 * @param damageSource the name of the source, e.g. "fall"
	 * @return an adapter, or null if none is registered
	 */
	@Nullable
	public static IDamageAdapter getDamageAdapter(String damageSource)
	{
		return DAMAGE_ADAPTERS.get(damageSource);
	}

	public static Potion POTION_DISFOCUS;
}
