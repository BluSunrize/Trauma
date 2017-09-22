/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class LimbStatus
{
	private final EnumLimb limb;
	private EnumTraumaState state = EnumTraumaState.NONE;
	private long recoveryTimer;

	public LimbStatus(EnumLimb limb)
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

	public static LimbStatus readFromNBT(NBTTagCompound nbt)
	{
		EnumLimb limb = EnumLimb.values()[nbt.getInteger("limb")];
		LimbStatus status = new LimbStatus(limb);
		status.setState(EnumTraumaState.values()[nbt.getInteger("state")]);
		status.setRecoveryTimer(nbt.getLong("recoveryTimer"));
		return status;
	}
}
