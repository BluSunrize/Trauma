/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import blusunrize.trauma.api.condition.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
@ParametersAreNonnullByDefault
public class TraumaApiUtils
{
	@SideOnly(Side.CLIENT)
	public static String getLocalizedLimb(EnumLimb limb)
	{
		return I18n.format(limb.getUnlocalizedName());
	}

	public static String getUnlocalizedDamage(EnumLimb limb, EnumTraumaState state)
	{
		if(state==EnumTraumaState.NONE)
			return "desc.trauma.state."+state.name();
		return "desc.trauma.limb."+limb.name()+".state."+state.name();
	}

	@SideOnly(Side.CLIENT)
	public static String getLocalizedDamage(EnumLimb limb, EnumTraumaState state)
	{
		return I18n.format(getUnlocalizedDamage(limb, state));
	}

	public static EnumTraumaState getLimbState(EntityPlayer player, EnumLimb limb)
	{
		TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
		return status.getLimbCondition(limb).getState();
	}

	public static boolean setLimbState(EntityPlayer player, EnumLimb limb, EnumTraumaState state, boolean fireEvent)
	{
		if(!fireEvent||!MinecraftForge.EVENT_BUS.post(new LimbStateEvent(player, limb, state)))
		{
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			LimbCondition limbCondition = status.getLimbCondition(limb);
			limbCondition.assumeState(player, state);
			return true;
		}
		return false;
	}

	public static boolean damageLimb(EntityPlayer player, EnumLimb limb, int steps)
	{
		EnumTraumaState oldState = getLimbState(player, limb);
		EnumTraumaState newState = oldState.getWorse(steps);
		if(setLimbState(player, limb, newState, true))
		{
			if(oldState!=newState)
				player.sendMessage(new TextComponentTranslation("chat.trauma.injury", new TextComponentTranslation(limb.getUnlocalizedName()), new TextComponentTranslation(getUnlocalizedDamage(limb, newState))));
			return true;
		}
		return false;
	}

	public static void clearPlayer(EntityPlayer player)
	{
		TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
		for(EnumLimb limb : EnumLimb.values())
			status.getLimbCondition(limb).cure(player);
	}
}
