/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class CapabilityTrauma
{
	@CapabilityInject(TraumaStatus.class)
	public static Capability<TraumaStatus> TRAUMA_CAPABILITY = null;

	public static final ResourceLocation KEY = new ResourceLocation(Trauma.MODID, "capabilityTrauma");

	public static final ConcurrentMap<EntityPlayer, TraumaStatus> playerCapabilityMap = new ConcurrentHashMap();

	public static void register()
	{
		CapabilityManager.INSTANCE.register(TraumaStatus.class, new Capability.IStorage<TraumaStatus>()
		{
			@Override
			public NBTBase writeNBT(Capability<TraumaStatus> capability, TraumaStatus instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<TraumaStatus> capability, TraumaStatus instance, EnumFacing side, NBTBase nbt)
			{
				instance.deserializeNBT((NBTTagCompound)nbt);
			}
		}, TraumaStatus::new);
	}

	public static class Provider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>
	{
		private final EntityPlayer player;

		public Provider(EntityPlayer player)
		{
			this.player = player;
			CapabilityTrauma.playerCapabilityMap.putIfAbsent(player, new TraumaStatus());
		}

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability==CapabilityTrauma.TRAUMA_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			if(capability==CapabilityTrauma.TRAUMA_CAPABILITY)
				return (T)playerCapabilityMap.get(this.player);
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return playerCapabilityMap.get(this.player).serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			playerCapabilityMap.get(this.player).deserializeNBT(nbt);
		}
	}
}
