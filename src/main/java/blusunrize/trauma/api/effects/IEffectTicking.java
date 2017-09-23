/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.effects;

import blusunrize.trauma.api.LimbCondition;
import net.minecraft.entity.player.EntityPlayer;

/**
 * A trauma effect designed for things that need to be applied every tick
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public interface IEffectTicking extends ITraumaEffect
{
	/**
	 * Called every tick
	 * @param player injured player
	 * @param limbCondition the condition which has this effect applied
	 */
	void tick(EntityPlayer player, LimbCondition limbCondition);
}
