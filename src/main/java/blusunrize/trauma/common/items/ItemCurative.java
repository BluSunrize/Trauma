/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.items;

import blusunrize.trauma.common.Trauma;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author BluSunrize
 * @since 25.09.2017
 */
public class ItemCurative extends Item
{
	public ItemCurative(String name)
	{
		ResourceLocation rl = new ResourceLocation(Trauma.MODID, name);
		this.setRegistryName(rl);
		this.setUnlocalizedName(rl.toString());
	}
}
