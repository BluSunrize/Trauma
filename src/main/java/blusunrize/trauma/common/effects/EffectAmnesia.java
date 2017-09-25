/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.LimbCondition;
import blusunrize.trauma.api.effects.IEffectTicking;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Occasional loss of XP
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectAmnesia implements IEffectTicking
{
	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Amnesia";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.amnesia";
	}

	@Override
	public void tick(EntityPlayer player, LimbCondition limbCondition)
	{
		if((player.ticksExisted+(player.getRNG().nextInt(80)-40))%120==0)
			player.addExperienceLevel(-1+player.getRNG().nextInt(3));
	}
}
