/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.LimbCondition;
import blusunrize.trauma.api.effects.IEffectAttribute;
import blusunrize.trauma.common.Trauma;
import blusunrize.trauma.common.Utils;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Reduced attack speed
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectAttackSpeed implements IEffectAttribute
{
	private static final UUID ATTACK_SPEED_MODIFIER = Utils.generateNewUUID();

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":AttackSpeed";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.attackspeed."+limbCondition.getState().getDamageIndex();
	}

	@Override
	public void gatherModifiers(EntityPlayer player, LimbCondition limbCondition, Multimap<String, AttributeModifier> map)
	{
		float mod = .16f + .24f*limbCondition.getState().ordinal();
		map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, getIndentifier(), -mod, 2));
	}
}
