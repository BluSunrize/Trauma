/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils;

import blusunrize.trauma.api.CapabilityTrauma;
import blusunrize.trauma.api.EnumLimb;
import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.TraumaApiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class EventHandler
{
	@SubscribeEvent
	public void onCapabilitiesAttach(AttachCapabilitiesEvent event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getObject();
			event.addCapability(CapabilityTrauma.KEY, new CapabilityTrauma.Provider(player));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityDamaged(LivingHurtEvent event)
	{
		if(event.isCanceled()||event.getAmount() <= 0||!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer)event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		float amount = event.getAmount();

		if(TraumaApiLib.isFallDamage(damageSource))
		{
			EnumLimb leg = player.getRNG().nextBoolean()?EnumLimb.LEG_LEFT: EnumLimb.LEG_RIGHT;
			int steps = (int)Math.ceil(amount/5);
			if(amount > 3&&player.getRNG().nextInt(10) < amount)//Both legs, chances increase with damage
			{
				TraumaApiUtils.damageLimb(player, leg, steps);
				TraumaApiUtils.damageLimb(player, leg.getOpposite(), steps);
			}
			else if(amount >= 1)
				TraumaApiUtils.damageLimb(player, leg, steps);

			return;
		}

		Entity projectile = event.getSource().getImmediateSource();
		Entity attacker = event.getSource().getTrueSource();
	}
}
