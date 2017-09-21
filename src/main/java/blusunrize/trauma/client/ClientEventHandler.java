package blusunrize.trauma.client;

import blusunrize.trauma.api.*;
import blusunrize.trauma.common.Trauma;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 * <p>
 * Copyright:
 *
 * @author BluSunrize
 * @since 21.09.2017
 */
public class ClientEventHandler
{
	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent event)
	{
		if(event instanceof DrawScreenEvent.Post && event.getGui() instanceof GuiInventory)
		{
			EntityPlayer player = Trauma.proxy.getClientPlayer();
			TraumaStatus status = player.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY,null);
			ItemStack held = player.inventory.getItemStack();
			for(EnumLimb limb : EnumLimb.values())
			{
				int[] rect = limb.getGuiRectangle(player);
				if(((GuiInventory)event.getGui()).isPointInRegion(rect[0],rect[1], rect[2],rect[3], event.getMouseX(),event.getMouseY()))
				{
					EnumTraumaState traumaState = status.getLimbStatus(limb).getState();
					List<String> text = new ArrayList<>();
					text.add(TextFormatting.GRAY+ApiUtils.getLocalizedLimb(limb));
					text.add(traumaState.getTextColor() + ApiUtils.getLocalizedDamage(limb, traumaState));
					if(!held.isEmpty())
						text.add(TextFormatting.DARK_GRAY+I18n.format("des.trauma.useItem", held.getDisplayName()));
					event.getGui().drawHoveringText(text, event.getMouseX(), event.getMouseY());
					break;
				}
			}
		}
	}
}
