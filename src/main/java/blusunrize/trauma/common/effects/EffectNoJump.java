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
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Prevents jumping, functions as an EventHandler
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectNoJump implements ITraumaEffect
{
	public EffectNoJump()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":NoJump";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.nojump."+limbCondition.getState().getDamageIndex();
	}

	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			float mod = 1;
			if(status.getLimbStatus(EnumLimb.LEG_LEFT).hasEffect(getIndentifier()))
				mod -= .25*status.getLimbStatus(EnumLimb.LEG_LEFT).getState().getDamageIndex();
			if(status.getLimbStatus(EnumLimb.LEG_RIGHT).hasEffect(getIndentifier()))
				mod -= .25*status.getLimbStatus(EnumLimb.LEG_RIGHT).getState().getDamageIndex();

			player.motionY *= mod;
		}
	}
}