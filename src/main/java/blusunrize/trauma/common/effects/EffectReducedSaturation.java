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
import blusunrize.trauma.api.effects.IEffectTicking;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Regularly ticking exhaustion
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public class EffectReducedSaturation implements IEffectTicking
{
	public EffectReducedSaturation()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getIndentifier()
	{
		return Trauma.MODID+":ReducedSaturation";
	}

	@Override
	public String getDescription(EntityPlayer player, LimbCondition limbCondition)
	{
		return "desc.trauma.effect.reducedsaturation";
	}

	@Override
	public void tick(EntityPlayer player, LimbCondition limbCondition)
	{
		if(!player.world.isRemote)
		{
			if(foodMap.containsKey(player.getUniqueID()))
			{
				Pair<Integer, Float> entry = foodMap.remove(player.getUniqueID());
				float mod = limbCondition.getState().ordinal()*.25f;
				player.getFoodStats().addStats(Math.round(-entry.getLeft()*mod), entry.getRight()*mod);
			}
			if(potionMap.containsKey(player.getUniqueID()))
			{
				Collection<PotionEffect> entry = potionMap.remove(player.getUniqueID());
				float mod = 1-(limbCondition.getState().ordinal()*.25f);
				for(PotionEffect effect : entry)
					if(!effect.getPotion().isInstant()&&(!effect.getPotion().isBadEffect()||effect.getPotion().isBeneficial()))
					{
						PotionEffect active = player.getActivePotionEffect(effect.getPotion());
						if(active!=null&&effect.getDuration()-active.getDuration() <= 1)//Initiated just now
						{
							int dur = 60;//Math.round(active.getDuration()*mod);
							int amp = Math.round(active.getAmplifier()*mod);
							PotionEffect replacement = new PotionEffect(active.getPotion(), dur, amp, active.getIsAmbient(), active.doesShowParticles());
							replacement.setCurativeItems(active.getCurativeItems());
							player.getActivePotionMap().put(replacement.getPotion(), replacement);
						}
					}
			}
		}
	}

	private static HashMap<UUID, Pair<Integer, Float>> foodMap = new HashMap<>();
	private static HashMap<UUID, Collection<PotionEffect>> potionMap = new HashMap<>();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUseItemTick(LivingEntityUseItemEvent.Tick event)
	{
		if(!event.isCanceled()&&event.getDuration()==1&&event.getEntityLiving() instanceof EntityPlayer&&!event.getEntity().world.isRemote)//Last tick, uncancelled
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			if(status.getLimbCondition(EnumLimb.ABDOMEN).hasEffect(getIndentifier()))
			{
				ItemStack stack = event.getItem();
				if(stack.getItem() instanceof ItemFood)
					foodMap.put(event.getEntityLiving().getUniqueID(), Pair.of(((ItemFood)stack.getItem()).getHealAmount(stack), ((ItemFood)stack.getItem()).getSaturationModifier(stack)));
				else if(stack.getItem() instanceof ItemPotion)
					potionMap.put(event.getEntityLiving().getUniqueID(), PotionUtils.getEffectsFromStack(stack));
			}
		}
	}
}
