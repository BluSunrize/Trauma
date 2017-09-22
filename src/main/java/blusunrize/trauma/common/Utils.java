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
}
