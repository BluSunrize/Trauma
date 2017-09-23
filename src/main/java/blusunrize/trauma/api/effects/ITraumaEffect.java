/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.effects;

import blusunrize.trauma.api.LimbCondition;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * General interface for any effects caused by injured lims
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ITraumaEffect
{
	/**
	 * @return a unique identifier for this effect. Allows easy checks if an effect is present
	 */
	String getIndentifier();

	/**
	 * @return Description of effects for hovering over in GUI
	 */
	String getDescription(EntityPlayer player, LimbCondition limbCondition);
}
