/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.recovery;

import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.common.Trauma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class CapabilityRecoveryItem
{
	@CapabilityInject(IRecoveryItem.class)
	public static Capability<IRecoveryItem> REVOVERYITEM_CAPABILITY = null;

	public static final ResourceLocation KEY = new ResourceLocation(Trauma.MODID, "capabilityRecoveryItem");

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IRecoveryItem.class, new Capability.IStorage<IRecoveryItem>()
		{
			@Override
			public NBTBase writeNBT(Capability<IRecoveryItem> capability, IRecoveryItem instance, EnumFacing side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<IRecoveryItem> capability, IRecoveryItem instance, EnumFacing side, NBTBase nbt)
			{
			}
		}, RecoveryItemMissing::new);
	}

	public static class RecoveryItemMissing implements IRecoveryItem
	{
		@Override
		public String getIdentifier(ItemStack stack)
		{
			return null;
		}

		@Override
		public boolean canApply(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
		{
			return false;
		}

		@Override
		public int getDuration(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
		{
			return 0;
		}
	}
}
