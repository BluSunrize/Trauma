/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils.commands;

import blusunrize.trauma.api.CapabilityTrauma;
import blusunrize.trauma.api.EnumLimb;
import blusunrize.trauma.api.EnumTraumaState;
import blusunrize.trauma.api.TraumaStatus;
import blusunrize.trauma.common.Trauma;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class CommandTrauma extends CommandBase
{
	@Override
	public String getName()
	{
		return Trauma.MODID;
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		System.out.println("Run command with args: "+Arrays.toString(args));

		if(args.length > 0&&sender instanceof EntityPlayer)
		{
			TraumaStatus status = ((EntityPlayer)sender).getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			if("get".equals(args[0]))
			{
				String msg = "Status: "+status;
				if(status!=null)
				{
					for(EnumLimb limb : EnumLimb.values())
						msg += "\n  "+limb+": "+status.getLimbStatus(limb).getState();
				}
				sender.sendMessage(new TextComponentString(msg));
			}
			else if("set".equals(args[0])&&args.length > 2)
			{
				try
				{
					EnumLimb limb = EnumLimb.valueOf(args[1].toUpperCase());
					int state = Integer.parseInt(args[2].replaceAll("\\D", ""));
					if(limb!=null&&state >= 0&&state < EnumTraumaState.values().length)
					{
						System.out.println("Setting status of "+limb+" to "+state);
						status.getLimbStatus(limb).setState(EnumTraumaState.values()[state]);
					}
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
