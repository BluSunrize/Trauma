/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.client;

import blusunrize.trauma.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	@Override
	public void init()
	{
	}

	@Override
	public void postInit()
	{
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().world;
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
}
