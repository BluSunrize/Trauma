package blusunrize.trauma.api;

import net.minecraft.util.text.TextFormatting;

/**
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 * <p>
 * Copyright:
 *
 * @author BluSunrize
 * @since 20.09.2017
 */
public enum EnumTraumaState
{
	NONE(TextFormatting.GRAY),
	LIGHT(TextFormatting.YELLOW),
	MEDIUM(TextFormatting.GOLD),
	HEAVY(TextFormatting.RED);

	private final TextFormatting textColor;

	EnumTraumaState(TextFormatting textColor)
	{
		this.textColor = textColor;
	}

	public TextFormatting getTextColor()
	{
		return textColor;
	}
}
