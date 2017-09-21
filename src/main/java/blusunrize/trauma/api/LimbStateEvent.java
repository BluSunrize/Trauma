/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
@Cancelable
public class LimbStateEvent extends PlayerEvent
{
	private EnumLimb limb;
	private EnumTraumaState state;

	public LimbStateEvent(EntityPlayer player, EnumLimb limb, EnumTraumaState state)
	{
		super(player);
		this.limb = limb;
		this.state = state;
	}

	public EnumLimb getLimb()
	{
		return limb;
	}

	public void setLimb(EnumLimb limb)
	{
		this.limb = limb;
	}

	public EnumTraumaState getState()
	{
		return state;
	}

	public void setState(EnumTraumaState state)
	{
		this.state = state;
	}
}
