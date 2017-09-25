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
public class DamageAdapterCramming implements IDamageAdapter
{
	@Override
	public boolean handleDamage(EntityPlayer player, DamageSource damageSource, float amount)
	{
		if(TraumaApiUtils.damageLimb(player, EnumLimb.CHEST, 1))
			return true;
		return false;
	}
}
