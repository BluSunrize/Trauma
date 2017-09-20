package blusunrize.trauma.api;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 * <p>
 * Copyright:
 *
 * @author BluSunrize
 * @since 20.09.2017
 */
public class LimbStatus
{
	private final EnumLimb limb;
	private EnumTraumaState state = EnumTraumaState.NONE;

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

	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt)
	{
		if(nbt==null)
			nbt = new NBTTagCompound();
		nbt.setInteger("limb", limb.ordinal());
		nbt.setInteger("state", state.ordinal());
		return nbt;
	}

	public static LimbStatus readFromNBT(NBTTagCompound nbt)
	{
		EnumLimb limb = EnumLimb.values()[nbt.getInteger("limb")];
		LimbStatus status = new LimbStatus(limb);
		status.setState(EnumTraumaState.values()[nbt.getInteger("state")]);

		return status;
	}
}
