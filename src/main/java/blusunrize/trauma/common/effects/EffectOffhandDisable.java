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
import blusunrize.trauma.api.condition.TraumaStatus;
import blusunrize.trauma.api.effects.ITraumaEffect;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Reduced Miningspeed, functions as an EventHandler
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectOffhandDisable implements ITraumaEffect
{
	public EffectOffhandDisable()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":OffhandDisable";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.unusable";
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
		if(event.getHand()==EnumHand.OFF_HAND && status.getLimbStatus(EnumLimb.ARM_OFFHAND).hasEffect(getIndentifier()))
		{
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.FAIL);
		}
	}
}