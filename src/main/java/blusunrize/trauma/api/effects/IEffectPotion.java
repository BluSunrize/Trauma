/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.effects;

import net.minecraft.potion.Potion;

import java.util.HashMap;

/**
 * A trauma effect designed for constantly upkept potion effects
 *
 * @author BluSunrize
 * @since 23.09.2017
 */
public interface IEffectPotion
{
	/**
	 * @param map A map of Potion->Amplifier, increase if potion is already present
	 */
	void addToPotionMap(HashMap<Potion,Integer> map);
}
