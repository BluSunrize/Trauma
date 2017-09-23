/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import blusunrize.trauma.api.effects.ITraumaEffect;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class LimbCondition
{
	private final EnumLimb limb;
	private EnumTraumaState state = EnumTraumaState.NONE;
	private long recoveryTimer;
	private HashMap<String, ITraumaEffect> effects;

	public LimbCondition(EnumLimb limb)
	{
		this.limb = limb;
	}

	public EnumLimb getLimb()
	{
		return limb;
	}

	public EnumTraumaState getState()
	{
		return state;
	}

	public void setState(EnumTraumaState state)
	{
		this.state = state;
	}

	public long getRecoveryTimer()
	{
		return recoveryTimer;
	}

	public void setRecoveryTimer(long recoveryTimer)
	{
		this.recoveryTimer = recoveryTimer;
	}

	public HashMap<String, ITraumaEffect> getEffects()
	{
		return effects;
	}

	public void addEffect(ITraumaEffect effect)
	{
		this.effects.put(effect.getIndentifier(), effect);
	}

	public boolean hasEffect(String ident)
	{
		return this.effects.containsKey(ident);
	}

	public void removeEffect(String ident)
	{
		this.effects.remove(ident);
	}

	public void clearEffects()
	{
		this.effects.clear();
	}

	public void tick()
	{
		if(this.recoveryTimer>0 && --this.recoveryTimer<=0)
			this.setState(EnumTraumaState.NONE);
	}

	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt)
	{
		if(nbt==null)
			nbt = new NBTTagCompound();
		nbt.setInteger("limb", limb.ordinal());
		nbt.setInteger("state", state.ordinal());
		nbt.setLong("recoveryTimer", recoveryTimer);
		return nbt;
	}

	public static LimbCondition readFromNBT(NBTTagCompound nbt)
	{
		EnumLimb limb = EnumLimb.values()[nbt.getInteger("limb")];
		LimbCondition limbCondition = new LimbCondition(limb);
		limbCondition.setState(EnumTraumaState.values()[nbt.getInteger("state")]);
		limbCondition.setRecoveryTimer(nbt.getLong("recoveryTimer"));
		for(ITraumaEffect effect : TraumaApiLib.getRegisteredEffects(limbCondition.getLimb(), limbCondition.getState()))
			limbCondition.addEffect(effect);
		return limbCondition;
	}
}
