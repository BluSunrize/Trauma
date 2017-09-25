/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils;

import blusunrize.trauma.api.IDamageAdapter;
import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.TraumaApiUtils;
import blusunrize.trauma.api.condition.CapabilityTrauma;
import blusunrize.trauma.api.condition.EnumLimb;
import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.condition.TraumaStatus;
import blusunrize.trauma.api.effects.*;
import blusunrize.trauma.common.Utils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.util.Map;

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
		if(event.isCanceled()||event.getAmount() <= 0||!(event.getEntityLiving() instanceof EntityPlayer)||event.getEntity().getEntityWorld().isRemote)
			return;

		EntityPlayer player = (EntityPlayer)event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		float amount = event.getAmount();

		if(TraumaApiLib.isIgnoredDamage(damageSource))
			return;

		IDamageAdapter adapter = TraumaApiLib.getDamageAdapter(damageSource.getDamageType());
		if(adapter!=null)
		{
			if(adapter.handleDamage(player, damageSource, amount))
				Utils.sendSyncPacket(player);
			return;
		}
		if(amount <= 2)//Anything under a single heart is not bad
			return;
		Entity projectile = event.getSource().getImmediateSource();
		Entity attacker = event.getSource().getTrueSource();

		EnumLimb limb = null;
		if(projectile!=null&&!projectile.equals(attacker)&&!(projectile instanceof EntityLivingBase))//projectile exists and is not living
		{
			double headRelation = projectile.posY-(player.posY+1.75);
			if(Math.abs(headRelation) <= .25)//Headshot :D
				limb = EnumLimb.HEAD;
			else if(Math.abs(projectile.posY-(player.posY+.375)) <= .375)//Legs
				limb = player.getRNG().nextBoolean()?EnumLimb.LEG_LEFT: EnumLimb.LEG_RIGHT;
			else
				limb = EnumLimb.values()[1+player.getRNG().nextInt(4)];//Random limb in the Torso area
		}
		else if(attacker!=null)
		{
			double attackHeight = attacker.posY+attacker.height/2-player.posY;
			if(attackHeight > 1.5)//head
				limb = EnumLimb.HEAD;
			else if(attackHeight<.75)
				limb = player.getRNG().nextBoolean()?EnumLimb.LEG_LEFT: EnumLimb.LEG_RIGHT;
			else
				limb = EnumLimb.values()[1+player.getRNG().nextInt(4)];//Random limb in the Torso area
		}
		else
			limb = EnumLimb.values()[player.getRNG().nextInt(EnumLimb.values().length)];//Random limb

		if(limb!=null)
		{
			TraumaApiUtils.damageLimb(player, limb, 1+(int)(amount/6));
			Utils.sendSyncPacket(player);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase==Phase.END && Utils.shouldTick(event.player))
		{
			TraumaStatus status = event.player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			PotionEffectMap potionEffectMap = new PotionEffectMap();
			Multimap<String, AttributeModifier> attributeMap = HashMultimap.create();
			for(EnumLimb limb : EnumLimb.values())
			{
				LimbCondition condition = status.getLimbStatus(limb);
				condition.tick(event.player);
				for(ITraumaEffect effect : condition.getEffects().values())
				{
					if(effect instanceof IEffectTicking)
						((IEffectTicking)effect).tick(event.player, condition);
					if(effect instanceof IEffectPotion)
						((IEffectPotion)effect).addToPotionMap(event.player, condition, potionEffectMap);
					if(effect instanceof IEffectAttribute)
						((IEffectAttribute)effect).gatherModifiers(event.player, condition, attributeMap);
				}
			}
			if(!potionEffectMap.isEmpty())
				for(Map.Entry<Potion, Integer> entry : potionEffectMap.entrySet())
					if(entry.getValue()>=1)
					{
						PotionEffect effect = event.player.getActivePotionEffect(entry.getKey());
						if(effect==null || effect.getDuration()<=20)//Reset potion effects only when they get low
							event.player.addPotionEffect(new PotionEffect(entry.getKey(), 80, entry.getValue()-1, false, false));
					}
			if(!attributeMap.isEmpty())
				event.player.getAttributeMap().applyAttributeModifiers(attributeMap);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		Utils.sendSyncPacket(event.player);
	}
}
