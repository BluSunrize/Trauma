/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.client;

import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.TraumaApiUtils;
import blusunrize.trauma.api.condition.*;
import blusunrize.trauma.api.effects.ITraumaEffect;
import blusunrize.trauma.common.Trauma;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
public class ClientEventHandler
{
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event)
	{

	}

	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent event)
	{
		if(event instanceof DrawScreenEvent.Post&&event.getGui() instanceof GuiInventory)
		{
			EntityPlayer player = Trauma.proxy.getClientPlayer();
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			ItemStack held = player.inventory.getItemStack();
			for(EnumLimb limb : EnumLimb.values())
			{
				int[] rect = limb.getGuiRectangle(player);
				if(((GuiInventory)event.getGui()).isPointInRegion(rect[0], rect[1], rect[2], rect[3], event.getMouseX(), event.getMouseY()))
				{
					LimbCondition limbCondition = status.getLimbStatus(limb);
					EnumTraumaState traumaState = limbCondition.getState();
					List<String> text = new ArrayList<>();
					text.add(TextFormatting.GRAY+TraumaApiUtils.getLocalizedLimb(limb));
					String formattedState = traumaState.getTextColor()+TraumaApiUtils.getLocalizedDamage(limb, traumaState);
					if(limbCondition.getRecoveryTimer()>0)
						formattedState += " "+ClientUtils.ticksToFormattedTime(limbCondition.getRecoveryTimer());
					text.add(formattedState);
					for(ITraumaEffect effect : limbCondition.getEffects().values())
						text.add(" "+TextFormatting.DARK_GRAY+I18n.format(effect.getDescription(player, limbCondition)));

					if(!held.isEmpty())
						text.add(TextFormatting.DARK_GRAY+I18n.format("desc.trauma.useItem", held.getDisplayName()));
					event.getGui().drawHoveringText(text, event.getMouseX(), event.getMouseY());
					break;
				}
			}
		}
	}

	private int unfocusPotionCounter = 0;

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player.getActivePotionEffect(TraumaApiLib.POTION_DISFOCUS)!=null)
		{
			float f1 = ((++unfocusPotionCounter + Minecraft.getMinecraft().getRenderPartialTicks())%60)/30;
			double sX = Math.sin(3.14159*f1)*.1875;
			double sY = Math.cos(3.14159*f1)*.1875;
			GlStateManager.scale(1+sX, 1+sY, 1.0F);
		}
		else if(unfocusPotionCounter>0)
			unfocusPotionCounter = 0;
	}
}
