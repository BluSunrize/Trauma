/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils.commands;

import blusunrize.trauma.api.TraumaApiUtils;
import blusunrize.trauma.api.condition.CapabilityTrauma;
import blusunrize.trauma.api.condition.EnumLimb;
import blusunrize.trauma.api.condition.EnumTraumaState;
import blusunrize.trauma.api.condition.TraumaStatus;
import blusunrize.trauma.common.Trauma;
import blusunrize.trauma.common.Utils;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		return "chat.trauma.command.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0)
		{
			if("get".equals(args[0]))
			{
				EntityPlayer player = getPlayerOrSender(server, sender, args.length>1?args[1]:null);
				TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
				String msg = "Status: "+status;
				if(status!=null)
				{
					for(EnumLimb limb : EnumLimb.values())
						msg += "\n  "+limb+": "+status.getLimbStatus(limb).getState();
				}
				sender.sendMessage(new TextComponentString(msg));
			}
			else if("set".equals(args[0]))
			{
				if(args.length<3)
					throw new WrongUsageException("chat.trauma.command.usage.set", new Object[0]);
				EnumLimb limb = (EnumLimb)Utils.parseEnum(args[1], EnumLimb.class);
				if(limb==null)
					throw new CommandException("chat.trauma.command.error.limb", args[1]);
				EnumTraumaState state = (EnumTraumaState)Utils.parseEnum(args[2], EnumTraumaState.class);
				if(state==null)
					throw new CommandException("chat.trauma.command.error.state", args[2]);

				EntityPlayer player = getPlayerOrSender(server, sender, args.length>3?args[3]:null);
				if(player!=null)
				{
					if(TraumaApiUtils.setLimbState(player, limb, state, true))
					{
						if(state==EnumTraumaState.NONE)

						Utils.sendSyncPacket(player);
						sender.sendMessage(new TextComponentTranslation("chat.trauma.command.set.success", new TextComponentTranslation(limb.getUnlocalizedName()), new TextComponentTranslation(TraumaApiUtils.getUnlocalizedDamage(limb, state))));
					}
				}
			}
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
		{
			return getListOfStringsMatchingLastWord(args, "get","set");
		}
		else if(args.length>1)
		{
			if("get".equals(args[0]) && args.length==2)//optional players on get
				return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			else if("set".equals(args[0]))
			{
				if(args.length==2)//limb
					return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumLimb.values()));
				else if(args.length==3)//state
					return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumTraumaState.values()));
				else if(args.length==4)//optional player
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			}
		}
		return Collections.<String>emptyList();
	}

	private static EntityPlayer getPlayerOrSender(MinecraftServer server, ICommandSender sender, String nameArg) throws PlayerNotFoundException
	{
		if(nameArg!=null)
		{
			EntityPlayer player = server.getPlayerList().getPlayerByUsername(nameArg);
			if(player==null)
				throw new PlayerNotFoundException("commands.generic.player.notFound", nameArg);
			return player;
		}
		else if(sender instanceof EntityPlayer)
			return (EntityPlayer)sender;
		else
			throw new PlayerNotFoundException("commands.generic.player.notFound");
	}
}
