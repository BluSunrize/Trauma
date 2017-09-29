/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.effects.IEffectTicking;
import blusunrize.trauma.common.Trauma;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Regularly ticking exhaustion
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectBreath implements IEffectTicking
{
	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Breath";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.breath";
	}

	@Override
	public void tick(EntityPlayer player, LimbCondition limbCondition)
	{
		if(player.isInsideOfMaterial(Material.WATER))
		{
			int i = EnchantmentHelper.getRespirationModifier(player);
			int draw = i>0 && player.getRNG().nextInt(i + 1) > 0 ? 0 : limbCondition.getState().ordinal();
			if(draw>0)
				player.setAir(player.getAir()-draw);
		}

	}
}
