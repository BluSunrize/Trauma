/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.LimbCondition;
import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.effects.IEffectTicking;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Irregular damage ticks
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectBleeding implements IEffectTicking
{
	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Bleeding";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.bleeding";
	}

	@Override
	public void tick(EntityPlayer player, LimbCondition limbCondition)
	{
		int step = 200-(30*limbCondition.getState().getDamageIndex());
		if((player.ticksExisted+(player.getRNG().nextInt(80)-40))%step==0)
			player.attackEntityFrom(TraumaApiLib.BLEEDING_DAMAGE, 1);
	}
}
