/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.effects;

import blusunrize.trauma.api.LimbCondition;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A trauma effect designed for attribute modifiers, handled similarly to equipped items
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
@ParametersAreNonnullByDefault
public interface IEffectAttribute extends ITraumaEffect
{
	/**
	 * @param player injured player
	 * @param limbCondition the condition which has this effect applied
	 * @param map A multimap of attribute name to modifiers
	 */
	void gatherModifiers(EntityPlayer player, LimbCondition limbCondition, Multimap<String, AttributeModifier> map);
}
