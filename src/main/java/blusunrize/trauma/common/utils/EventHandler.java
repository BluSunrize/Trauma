/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils;

import blusunrize.trauma.api.*;
import blusunrize.trauma.common.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

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
		if(event.isCanceled()||event.getAmount() <= 0||!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntity().getEntityWorld().isRemote)
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
				Utils.sendSyncPacket(player);
			}
			else if(amount >= 1)
			{
				TraumaApiUtils.damageLimb(player, leg, steps);
				Utils.sendSyncPacket(player);
			}
			return;
		}
		Entity projectile = event.getSource().getImmediateSource();
		Entity attacker = event.getSource().getTrueSource();

	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase==Phase.END && Utils.shouldTick(event.player))
		{
			TraumaStatus status = event.player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			for(EnumLimb limb : EnumLimb.values())
				status.getLimbStatus(limb).tick();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		Utils.sendSyncPacket(event.player);
	}
}
