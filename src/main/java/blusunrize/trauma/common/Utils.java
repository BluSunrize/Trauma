/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common;

import blusunrize.trauma.api.CapabilityTrauma;
import blusunrize.trauma.common.utils.network.MessageTraumaStatusSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

/**
 * @author BluSunrize
 * @since 22.09.2017
 */
public class Utils
{
	public static void sendSyncPacket(EntityPlayer player)
	{
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
			Trauma.packetHandler.sendTo(new MessageTraumaStatusSync(player, player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null)), (EntityPlayerMP)player);
	}

	public static boolean shouldTick(EntityPlayer player)
	{
		if(!isSingleplayer())//if multiplayer
			return true;
		return !player.getEntityWorld().isRemote;
	}

	public static boolean isSingleplayer()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server==null)//is client
			return false;
		return !server.isDedicatedServer();
	}

	public static Enum parseEnum(String s, Class enumClass)
	{
		try
		{
			return Enum.valueOf(enumClass, s);
		} catch(Exception e)
		{
			return null;
		}
	}

	static long UUIDBase = 109406002018L;
	static long UUIDAdd = 01L;
	public static UUID generateNewUUID()
	{
		UUID uuid = new UUID(UUIDBase,UUIDAdd);
		UUIDAdd++;
		return uuid;
	}

}
