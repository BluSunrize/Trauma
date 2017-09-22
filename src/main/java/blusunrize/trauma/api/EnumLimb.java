/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public enum EnumLimb
{
	HEAD(new int[]{44, 19, 14, 14}),
	CHEST(new int[]{44, 33, 14, 11}),
	ABDOMEN(new int[]{44, 44, 14, 11}),
	ARM_MAIN(new int[]{36, 33, 8, 22}),
	ARM_OFFHAND(new int[]{58, 33, 8, 22}),
	LEG_LEFT(new int[]{51, 55, 7, 22}),
	LEG_RIGHT(new int[]{44, 55, 7, 22});

	private final int[] guiRetangle;

	EnumLimb(int[] guiRetangle)
	{
		this.guiRetangle = guiRetangle;
	}

	public String getUnlocalizedName()
	{
		return "desc.trauma.limb."+name();
	}

	/**
	 * @param player used for mirroring of dominant hand
	 * @return the area of the default player Inventory to highlight
	 */
	public int[] getGuiRectangle(EntityPlayer player)
	{
		if(player.getPrimaryHand()==EnumHandSide.LEFT&&(this==ARM_MAIN||this==ARM_OFFHAND))
			return getOpposite().guiRetangle;
		return guiRetangle;
	}

	/**
	 * Only works with arms and legs
	 *
	 * @return the opposite side limb
	 */
	public EnumLimb getOpposite()
	{
		return this==ARM_MAIN?ARM_OFFHAND: this==ARM_OFFHAND?ARM_MAIN: this==LEG_LEFT?LEG_RIGHT: this==LEG_RIGHT?LEG_LEFT: null;
	}
}
