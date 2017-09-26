/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

/**
 * @author BluSunrize
 * @since 24.09.2017
 */
public class TraumaPotion extends Potion
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("trauma", "textures/potioneffects.png");

	public TraumaPotion(ResourceLocation resource, boolean isBadEffectIn, int liquidColorIn, int icon)
	{
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName("potion."+resource.toString());
		this.setRegistryName(resource);
		this.setIconIndex(icon%8, icon/8);
	}

	@Override
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		return super.getStatusIconIndex();
	}
}
