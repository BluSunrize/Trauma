/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.condition;

import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.effects.IEffectAttribute;
import blusunrize.trauma.api.effects.ITraumaEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A class representing the condition of a limb<br>
 * Stores limb, state, recovery timer and all applied TraumaEffects
 *
 * @author BluSunrize
 * @since 20.09.2017
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LimbCondition
{
	private final EnumLimb limb;
	private EnumTraumaState state = EnumTraumaState.NONE;
	private long recoveryTimer;
	private HashMap<String, Integer> recoveryItems = new HashMap<>();
	private HashMap<String, ITraumaEffect> effects = new HashMap<>();

	public LimbCondition(EnumLimb limb)
	{
		this.limb = limb;
	}

	/**
	 * @return the limb this condition refers to
	 */
	public EnumLimb getLimb()
	{
		return limb;
	}

	/**
	 * @return the TraumaState of the condition
	 */
	public EnumTraumaState getState()
	{
		return state;
	}

	/**
	 * Sets the TraumaState of the condition
	 * @param state the given state
	 */
	public void setState(EnumTraumaState state)
	{
		this.state = state;
	}

	/**
	 * @return the ticks left until the limb recovers
	 */
	public long getRecoveryTimer()
	{
		return recoveryTimer;
	}

	/**
	 * Sets the timer (in ticks) until the limb recovers
	 * @param recoveryTimer
	 */
	public void setRecoveryTimer(long recoveryTimer)
	{
		this.recoveryTimer = recoveryTimer;
	}

	/**
	 * @return a set of all recovery items applied, by their unique String keys
	 */
	public HashMap<String, Integer> getRecoveryItems()
	{
		return recoveryItems;
	}

	/**
	 * @return if a given recovery item (identified by its key) is applied
	 */
	public boolean hasRecoveryItems(String item)
	{
		return recoveryItems.containsKey(item);
	}

	/**
	 * Applies a recovery item, saved by its unique String key
	 * @param item
	 */
	public void addRecoveryItem(String item, int timer)
	{
		this.recoveryItems.put(item, timer);
	}

	/**
	 * @return A map of all ITraumaEffects this limb is applying to the player
	 */
	public HashMap<String, ITraumaEffect> getEffects()
	{
		return effects;
	}

	/**
	 * Adds an ITraumaEffect to the condition. Expires with the recovery timer
	 * @param effect
	 */
	public void addEffect(ITraumaEffect effect)
	{
		this.effects.put(effect.getIndentifier(), effect);
	}

	/**
	 * @param ident
	 * @return true if an effect with the given identifier is present
	 */
	public boolean hasEffect(String ident)
	{
		return this.effects.containsKey(ident);
	}

	/**
	 * Removes an effect by its identifier
	 * @param ident
	 */
	public void removeEffect(String ident)
	{
		this.effects.remove(ident);
	}

	/**
	 * Removes all ITraumaEffects from this limb, along with possible Attribute modifiers
	 * @param player
	 */
	public void clearEffects(EntityPlayer player)
	{
		Multimap<String, AttributeModifier> attributeMap = HashMultimap.create();
		for(ITraumaEffect effect : getEffects().values())
			if(effect instanceof IEffectAttribute)
				((IEffectAttribute)effect).gatherModifiers(player, this, attributeMap);
		player.getAttributeMap().removeAttributeModifiers(attributeMap);
		this.effects.clear();
	}

	/**
	 * @return true, if the condition was cured
	 */
	public boolean tick(EntityPlayer player)
	{
		int val;
		for(String item : this.recoveryItems.keySet())
		{
			val = this.recoveryItems.get(item);
			if(val > 0)
				if(--val <= 0)
					this.recoveryItems.remove(item);
				else
					this.recoveryItems.put(item, val);
		}
		if(this.recoveryTimer>0 && --this.recoveryTimer<=0)
		{
			cure(player);
			return true;
		}
		return false;
	}

	/**
	 * Sets the state, recovery timer and all associated effects
	 * @param player the Player
	 * @param state given TraumaState
	 */
	public void assumeState(EntityPlayer player, EnumTraumaState state)
	{
		this.cure(player); //Cure first to clear everything
		this.setState(state);
		this.setRecoveryTimer(TraumaApiLib.getRecoveryTime(limb, state));
		for(ITraumaEffect effect : TraumaApiLib.getRegisteredEffects(getLimb(), state))
			this.addEffect(effect);
	}

	/**
	 * Cures this condition, resetting state, timer and all effects
	 */
	public void cure(EntityPlayer player)
	{
		this.setState(EnumTraumaState.NONE);
		this.recoveryTimer = 0;
		this.clearEffects(player);
		this.recoveryItems.clear();
	}

	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt)
	{
		if(nbt==null)
			nbt = new NBTTagCompound();
		nbt.setInteger("limb", limb.ordinal());
		nbt.setInteger("state", state.ordinal());
		nbt.setLong("recoveryTimer", recoveryTimer);
		NBTTagList recItems = new NBTTagList();
		for(Entry<String, Integer> e : recoveryItems.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("item", e.getKey());
			tag.setInteger("timer", e.getValue());
			recItems.appendTag(tag);
		}
		nbt.setTag("recoveryItems", recItems);
		return nbt;
	}

	public static LimbCondition readFromNBT(NBTTagCompound nbt)
	{
		EnumLimb limb = EnumLimb.values()[nbt.getInteger("limb")];
		LimbCondition limbCondition = new LimbCondition(limb);
		limbCondition.setState(EnumTraumaState.values()[nbt.getInteger("state")]);
		limbCondition.setRecoveryTimer(nbt.getLong("recoveryTimer"));
		NBTTagList recItems = nbt.getTagList("recoveryItems", 10);
		for(int i=0; i<recItems.tagCount(); i++)
		{
			NBTTagCompound tag = recItems.getCompoundTagAt(i);
			limbCondition.addRecoveryItem(tag.getString("item"), tag.getInteger("timer"));
		}
		for(ITraumaEffect effect : TraumaApiLib.getRegisteredEffects(limbCondition.getLimb(), limbCondition.getState()))
			limbCondition.addEffect(effect);
		return limbCondition;
	}
}
