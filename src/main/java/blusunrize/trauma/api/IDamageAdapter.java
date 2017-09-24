/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

/**
 * An interface for custom damage adapters<br>
 * Used for stuff like falldamage, falling anvils and other specialcases
 *
 * @author BluSunrize
 * @since 24.09.2017
 */
public interface IDamageAdapter
{
	/**
	 * Fired on every LivingHurtEvent involving a player, provided this was registered to the given damage source
	 * @param player the player hurt
	 * @param damageSource the damagesource, given for projectile + attacker entities
	 * @param amount the amount of damage
	 * @return true to send a Sync package (do this if you changed any conditions)
	 */
	boolean handleDamage(EntityPlayer player, DamageSource damageSource, float amount);
}
