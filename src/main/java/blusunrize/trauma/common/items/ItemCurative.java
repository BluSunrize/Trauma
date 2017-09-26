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
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author BluSunrize
 * @since 25.09.2017
 */
public class ItemCurative extends ItemBase implements IRecoveryItem
{
	private final BiPredicate<ItemStack, LimbCondition> applyPredicate;
	private final int duration;
	private final BiFunction<ItemStack, LimbCondition, Float> modifier;
	private final BiConsumer<EntityPlayer, LimbCondition> usageConsumer;

	public ItemCurative(String name, String[] subNames, BiPredicate<ItemStack, LimbCondition> applyPredicate, int duration, BiFunction<ItemStack, LimbCondition, Float> modifier, BiConsumer<EntityPlayer, LimbCondition> usageConsumer)
	{
		super(name, subNames);
		this.applyPredicate = applyPredicate;
		this.duration = duration;
		this.modifier = modifier;
		this.usageConsumer = usageConsumer;
	}

	public ItemCurative(String name, BiPredicate<ItemStack, LimbCondition> applyPredicate, int duration, BiFunction<ItemStack, LimbCondition, Float> modifier, BiConsumer<EntityPlayer, LimbCondition> usageConsumer)
	{
		this(name, new String[0], applyPredicate, duration, modifier, null);
	}

	public ItemCurative(String name, BiPredicate<ItemStack, LimbCondition> applyPredicate, int duration)
	{
		this(name, applyPredicate, duration, (stack, limbCondition) -> 1f, null);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		String flavourKey = getUnlocalizedName(stack)+".desc";
		if(I18n.hasKey(flavourKey))
			tooltip.add(TextFormatting.GRAY.toString()+ I18n.format(flavourKey));
	}

	@Override
	public String getIdentifier(ItemStack stack)
	{
		return resource.toString();
	}

	@Override
	public boolean canApply(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return applyPredicate.test(stack, limbCondition);
	}

	@Override
	public int getDuration(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return this.duration;
	}

	@Override
	public float getRecoveryTimeModifier(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		return this.modifier.apply(stack, limbCondition);
	}

	@Override
	public void onApply(ItemStack stack, EntityPlayer player, LimbCondition limbCondition)
	{
		if(this.usageConsumer!=null)
			this.usageConsumer.accept(player, limbCondition);
	}
}
