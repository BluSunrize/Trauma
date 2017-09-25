/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.effects.IEffectTicking;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * Random application of vision-impairing effects
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectVision implements IEffectTicking
{
	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Vision";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.vision";
	}

	@Override
	public void tick(EntityPlayer player, LimbCondition limbCondition)
	{
		int freq = 360/limbCondition.getState().ordinal();
		if(player.getRNG().nextInt(freq)==0)
		{
			int type = player.getRNG().nextInt(3);
			if(type==0)
				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 140, 2, false, false));
			else if(type==1)
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80, 0, false, false));
			else if(type==2)
				player.addPotionEffect(new PotionEffect(TraumaApiLib.POTION_DISFOCUS, 60, 0, false, false));
		}
	}
}
