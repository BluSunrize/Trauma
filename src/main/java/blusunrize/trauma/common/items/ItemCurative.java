/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.items;

import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.recovery.IRecoveryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

/**
 * @author BluSunrize
 * @since 25.09.2017
 */
public class ItemCurative extends ItemBase implements IRecoveryItem
{
	private final Predicate<LimbCondition> applyPredicate;
	private final int duration;
	private final float modifier;

	public ItemCurative(String name, Predicate<LimbCondition> applyPredicate, int duration, float modifier)
	{
		super(name);
		this.applyPredicate = applyPredicate;
		this.duration = duration;
		this.modifier = modifier;
	}

	public ItemCurative(String name, Predicate<LimbCondition> applyPredicate, int duration)
	{
		this(name, applyPredicate, duration, 1);
	}

	@Override
	public String getIdentifier(ItemStack stack)
	{
		return resource.toString();
	}

	@Override
	public boolean canApply(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return applyPredicate.test(limbCondition);
	}

	@Override
	public int getDuration(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return this.duration;
	}

	@Override
	public float getRecoveryTimeModifier(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return this.modifier;
	}
}
