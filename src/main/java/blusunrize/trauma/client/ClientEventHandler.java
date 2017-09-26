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
import blusunrize.trauma.api.recovery.CapabilityRecoveryItem;
import blusunrize.trauma.api.recovery.IRecoveryItem;
import blusunrize.trauma.common.Trauma;
import blusunrize.trauma.common.items.ItemBase;
import blusunrize.trauma.common.utils.network.MessageApplyRecoveryItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
public class ClientEventHandler
{
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event)
	{
		for(ItemBase item : ItemBase.ITEMLIST)
		{
			String[] subNames = item.getSubnames();
			if(subNames!=null & subNames.length>0)
				for(int i = 0; i < subNames.length; i++)
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryLoc()+"_"+subNames[i]));
			else
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryLoc().toString()));
		}
	}

	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent.Post event)
	{
		if(event.getGui() instanceof GuiInventory)
		{
			EntityPlayer player = Trauma.proxy.getClientPlayer();
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
			ItemStack held = player.inventory.getItemStack();
			IRecoveryItem iRecoveryItem = null;
			if(!held.isEmpty()&&(held.getItem() instanceof IRecoveryItem||held.hasCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null)))
				iRecoveryItem = held.getItem() instanceof IRecoveryItem?(IRecoveryItem)held.getItem(): held.getCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null);

			for(EnumLimb limb : EnumLimb.values())
			{
				int[] rect = limb.getGuiRectangle(player);
				if(((GuiInventory)event.getGui()).isPointInRegion(rect[0], rect[1], rect[2], rect[3], event.getMouseX(), event.getMouseY()))
				{
					LimbCondition limbCondition = status.getLimbCondition(limb);
					EnumTraumaState traumaState = limbCondition.getState();
					List<String> text = new ArrayList<>();
					text.add(TextFormatting.GRAY+TraumaApiUtils.getLocalizedLimb(limb));
					String formattedState = traumaState.getTextColor()+TraumaApiUtils.getLocalizedDamage(limb, traumaState);
					if(limbCondition.getRecoveryTimer() > 0)
						formattedState += " "+ClientUtils.ticksToFormattedTime(limbCondition.getRecoveryTimer());
					text.add(formattedState);
					for(Entry<String,Integer> recoveryItem : limbCondition.getRecoveryItems().entrySet())
					{
						int timer = recoveryItem.getValue();
						String item = TextFormatting.GRAY+I18n.format("desc.trauma.recovery."+recoveryItem.getKey());
						if(timer>0)
							item += " "+ClientUtils.ticksToFormattedTime(timer);
						text.add(item);
					}

					for(ITraumaEffect effect : limbCondition.getEffects().values())
						text.add(" "+TextFormatting.DARK_GRAY+I18n.format(effect.getDescription(player, limbCondition)));

					if(iRecoveryItem!=null && iRecoveryItem.canApply(held, player, limbCondition)  && !limbCondition.hasRecoveryItems(iRecoveryItem.getIdentifier(held)))
						text.add(TextFormatting.DARK_GRAY+I18n.format("desc.trauma.useItem", held.getDisplayName()));

					event.getGui().drawHoveringText(text, event.getMouseX(), event.getMouseY());
					break;
				}
			}
		}
	}

	@SubscribeEvent
	public void onMouseEvent(MouseInputEvent.Pre event)
	{
		if(Mouse.getEventButtonState() && event.getGui() instanceof GuiInventory && Mouse.getEventButton()==0)
		{
			EntityPlayer player = Trauma.proxy.getClientPlayer();
			ItemStack held = player.inventory.getItemStack();
			if(!held.isEmpty()&&(held.getItem() instanceof IRecoveryItem||held.hasCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null)))
			{
				Minecraft mc = Minecraft.getMinecraft();
				GuiInventory gui = (GuiInventory)Minecraft.getMinecraft().currentScreen;
				ScaledResolution res = new ScaledResolution(mc);
				final int mouseX = Mouse.getEventX()*res.getScaledWidth()/mc.displayWidth;
				final int mouseY = res.getScaledHeight()-Mouse.getEventY()*res.getScaledHeight()/mc.displayHeight-1;

				TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
				IRecoveryItem iRecoveryItem = held.getItem() instanceof IRecoveryItem?(IRecoveryItem)held.getItem(): held.getCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null);
				for(EnumLimb limb : EnumLimb.values())
				{
					int[] rect = limb.getGuiRectangle(player);
					if(gui.isPointInRegion(rect[0], rect[1], rect[2], rect[3], mouseX, mouseY))
					{
						LimbCondition limbCondition = status.getLimbCondition(limb);
						if(iRecoveryItem.canApply(held, player, limbCondition) && !limbCondition.hasRecoveryItems(iRecoveryItem.getIdentifier(held)))
						{
							limbCondition.addRecoveryItem(iRecoveryItem.getIdentifier(held), iRecoveryItem.getDuration(held, player, limbCondition));
							held.shrink(1);
							Trauma.packetHandler.sendToServer(new MessageApplyRecoveryItem(player, limb));
							break;
						}
					}
				}
			}
		}
	}

	private int unfocusPotionCounter = 0;

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		EntityPlayer player = Trauma.proxy.getClientPlayer();
		if(player.getActivePotionEffect(TraumaApiLib.POTION_DISFOCUS)!=null)
		{
			float f1 = ((++unfocusPotionCounter+Minecraft.getMinecraft().getRenderPartialTicks())%60)/30;
			double sX = Math.sin(3.14159*f1)*.1875;
			double sY = Math.cos(3.14159*f1)*.1875;
			GlStateManager.scale(1+sX, 1+sY, 1.0F);
		}
		else if(unfocusPotionCounter > 0)
			unfocusPotionCounter = 0;
	}
}
