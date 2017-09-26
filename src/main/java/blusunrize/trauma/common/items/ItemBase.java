/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.items;

import blusunrize.trauma.common.Trauma;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BluSunrize
 * @since 25.09.2017
 */
public class ItemBase extends Item
{
	protected final ResourceLocation resource;
	protected final String[] subnames;

	public static final List<ItemBase> ITEMLIST = new ArrayList<>();

	public ItemBase(String name, String... subnames)
	{
		this.resource = new ResourceLocation(Trauma.MODID, name);
		this.subnames = subnames;
		this.setRegistryName(resource);
		this.setUnlocalizedName(resource.toString().replaceAll(":", "."));
		if(this.subnames!=null&&this.subnames.length > 0)
			this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if(this.subnames!=null&&this.subnames.length > 0&&stack.getMetadata() < this.subnames.length)
			return super.getUnlocalizedName(stack)+"."+subnames[stack.getMetadata()];
		return super.getUnlocalizedName(stack);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.subnames!=null&&this.subnames.length > 0&&this.isInCreativeTab(tab))
			for(int i = 0; i < subnames.length; i++)
				items.add(new ItemStack(this, 1, i));
		else
			super.getSubItems(tab, items);
	}

	public ResourceLocation getRegistryLoc()
	{
		return resource;
	}

	public String[] getSubnames()
	{
		return this.subnames;
	}

	public final Item register(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(this);
		ITEMLIST.add(this);
		return this;
	}
}
