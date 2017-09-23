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
import net.minecraft.potion.Potion;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

/**
 * A trauma effect designed for constantly upkept potion effects
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
@ParametersAreNonnullByDefault
public interface IEffectPotion
{
	/**
	 * @param player injured player
	 * @param limbCondition the condition which has this effect applied
	 * @param map A map of Potion->Amplifier, increase if potion is already present
	 */
	void addToPotionMap(EntityPlayer player, LimbCondition limbCondition, HashMap<Potion, Integer> map);
}
