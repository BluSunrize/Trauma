/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.recovery;

import blusunrize.trauma.api.condition.LimbCondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * May be implemented by an Item class or attached via the {@link CapabilityRecoveryItem}
 *
 * @author BluSunrize
 * @since 25.09.2017
 */
public interface IRecoveryItem
{
	/**
	 * @return a unique name for save reasons. Will be used as localisation key
	 */
	String getIdentifier(ItemStack stack);

	/**
	 * @param stack
	 * @param player
	 * @param limbCondition
	 * @return whether the item can be applied to the given limb
	 */
	boolean canApply(ItemStack stack, EntityPlayer player, LimbCondition limbCondition);

	/**
	 * @param stack
	 * @param player
	 * @param limbCondition
	 * @return the duration for which the item remains applied, 0 for infinite duration
	 */
	int getDuration(ItemStack stack, EntityPlayer player, LimbCondition limbCondition);

	/**
	 * @param stack
	 * @param player
	 * @param limbCondition
	 * @return a modifier to adjust the conditions recovery time on application
	 */
	default float getRecoveryTimeModifier(ItemStack stack, EntityPlayer player, LimbCondition limbCondition) { return 1; }
}
