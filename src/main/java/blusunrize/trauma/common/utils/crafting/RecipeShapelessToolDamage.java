/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

/**
 * @author BluSunrize
 * @since 26.09.2017
 */
public class RecipeShapelessToolDamage extends ShapelessOreRecipe
{
	int toolDamageSlot = -1;

	public RecipeShapelessToolDamage(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result)
	{
		super(group, input, result);
	}

	public RecipeShapelessToolDamage setToolDamageSlot(int slot)
	{
		this.toolDamageSlot = slot;
		return this;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> remains = super.getRemainingItems(inv);
		for(int i = 0; i < remains.size(); i++)
		{
			ItemStack s = inv.getStackInSlot(i);
			ItemStack remain = remains.get(i);
			if(toolDamageSlot >= 0&&toolDamageSlot < getIngredients().size())
			{
				ItemStack tool = ItemStack.EMPTY;
				if(remain.isEmpty()&&!s.isEmpty()&&getIngredients().get(toolDamageSlot).apply(s))
					tool = s.copy();
				else if(!remain.isEmpty()&&getIngredients().get(toolDamageSlot).apply(remain))
					tool = remain;
				if(!tool.isEmpty()&&tool.getItem().isDamageable())
				{
					tool.setItemDamage(tool.getItemDamage()+1);
					if(tool.getItemDamage() > tool.getMaxDamage())
						tool = ItemStack.EMPTY;
					remains.set(i, tool);
				}
			}
		}
		return remains;
	}
}
