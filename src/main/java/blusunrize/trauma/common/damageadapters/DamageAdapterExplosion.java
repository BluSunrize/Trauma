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
public class DamageAdapterExplosion implements IDamageAdapter
{
	@Override
	public boolean handleDamage(EntityPlayer player, DamageSource damageSource, float amount)
	{
		boolean ret = false;
		if(amount>=4)//That's enough for concussions
			ret |= TraumaApiUtils.damageLimb(player, EnumLimb.HEAD, 1);
		if(amount>=12)//That's really bad
		{
			ret |= TraumaApiUtils.damageLimb(player, EnumLimb.CHEST, 1);
			ret |= TraumaApiUtils.damageLimb(player, EnumLimb.ABDOMEN, 1);
		}
		else if(amount>=8)
			ret |= TraumaApiUtils.damageLimb(player, player.getRNG().nextBoolean()?EnumLimb.CHEST:EnumLimb.ABDOMEN, 1);
		return ret;
	}
}
