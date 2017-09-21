/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class TraumaStatus implements INBTSerializable<NBTTagCompound>
{
	private final Map<EnumLimb, LimbStatus> limbMap = new HashMap<EnumLimb, LimbStatus>()
	{{
		for(EnumLimb limb : EnumLimb.values())
			put(limb, new LimbStatus(limb));
	}};

	public LimbStatus getLimbStatus(EnumLimb limb)
	{
		return limbMap.get(limb);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList limbs = new NBTTagList();
		for(LimbStatus status : limbMap.values())
			limbs.appendTag(status.writeToNBT(null));
		nbt.setTag("limbMap", limbs);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		NBTTagList limbs = nbt.getTagList("limbMap", 10);
		for(int i = 0; i < limbs.tagCount(); i++)
		{
			LimbStatus limbStatus = LimbStatus.readFromNBT(limbs.getCompoundTagAt(i));
			limbMap.put(limbStatus.getLimb(), limbStatus);
		}
	}
}
