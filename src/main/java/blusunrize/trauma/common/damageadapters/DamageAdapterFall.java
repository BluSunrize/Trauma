/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.damageadapters;

import blusunrize.trauma.api.IDamageAdapter;
import blusunrize.trauma.api.TraumaApiUtils;
import blusunrize.trauma.api.condition.EnumLimb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

/**
 * @author BluSunrize
 * @since 24.09.2017
 */
public class DamageAdapterFall implements IDamageAdapter
{
	@Override
	public boolean handleDamage(EntityPlayer player, DamageSource damageSource, float amount)
	{
		EnumLimb leg = player.getRNG().nextBoolean()?EnumLimb.LEG_LEFT: EnumLimb.LEG_RIGHT;
		int steps = (int)Math.ceil(amount/5);
		if(amount > 3&&player.getRNG().nextInt(10) < amount)//Both legs, chances increase with damage
		{
			if(TraumaApiUtils.damageLimb(player, leg, steps) || TraumaApiUtils.damageLimb(player, leg.getOpposite(), steps))
				return true;
		}
		else if(amount >= 1)
		{
			if(TraumaApiUtils.damageLimb(player, leg, steps))
				return true;
		}
		return false;
	}
}
