/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.condition.CapabilityTrauma;
import blusunrize.trauma.api.condition.EnumLimb;
import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.effects.ITraumaEffect;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Reduced attack speed
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectArrowImprecision implements ITraumaEffect
{
	public EffectArrowImprecision()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Imprecision";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.imprecision";
	}

	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityArrow && ((EntityArrow)event.getEntity()).shootingEntity instanceof EntityPlayer)
		{
			EntityArrow arrow = (EntityArrow)event.getEntity();
			EntityPlayer player = (EntityPlayer)arrow.shootingEntity;
			LimbCondition limbCondition = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null).getLimbCondition(EnumLimb.ARM_OFFHAND);
			if(limbCondition.hasEffect(getIndentifier()))
			{
				float mod = limbCondition.getState().ordinal()*.5f;
				arrow.motionX += player.getRNG().nextGaussian() * 0.0075D * mod;
				arrow.motionY += player.getRNG().nextGaussian() * 0.0075D * mod;
				arrow.motionZ += player.getRNG().nextGaussian() * 0.0075D * mod;
			}
		}
	}
}
