/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.effects;

import blusunrize.trauma.api.CapabilityTrauma;
import blusunrize.trauma.api.EnumLimb;
import blusunrize.trauma.api.LimbCondition;
import blusunrize.trauma.api.TraumaStatus;
import blusunrize.trauma.api.effects.ITraumaEffect;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Prevents jumping, functions as an EventHandler
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectMining implements ITraumaEffect
{
	public EffectMining()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":Mining";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.mining."+limbCondition.getState().getDamageIndex();
	}

	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed event)
	{
		EntityPlayer player = event.getEntityPlayer();
		TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
		if(status.getLimbStatus(EnumLimb.ARM_MAIN).hasEffect(getIndentifier()))
		{
			float mod = .68f - (.32f * status.getLimbStatus(EnumLimb.ARM_MAIN).getState().getDamageIndex());
			event.setNewSpeed(mod*event.getOriginalSpeed());
		}
	}
}