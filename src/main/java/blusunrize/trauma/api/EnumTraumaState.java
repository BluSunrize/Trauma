/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.text.TextFormatting;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
@MethodsReturnNonnullByDefault
public enum EnumTraumaState
{
	NONE(TextFormatting.GRAY),
	LIGHT(TextFormatting.YELLOW),
	MEDIUM(TextFormatting.GOLD),
	HEAVY(TextFormatting.RED);

	private final TextFormatting textColor;

	public static final EnumTraumaState[] DAMAGED_STATES = {LIGHT, MEDIUM, HEAVY};

	EnumTraumaState(TextFormatting textColor)
	{
		this.textColor = textColor;
	}

	public TextFormatting getTextColor()
	{
		return textColor;
	}

	public EnumTraumaState getChanged(int steps)
	{
		int next = ordinal()+steps;
		if(next < 0)
			return NONE;
		else if(next > HEAVY.ordinal())
			return HEAVY;
		else
			return values()[next];
	}

	public EnumTraumaState getBetter()
	{
		return getBetter(1);
	}

	public EnumTraumaState getBetter(int steps)
	{
		return getChanged(-steps);
	}

	public EnumTraumaState getWorse()
	{
		return getWorse(1);
	}

	public EnumTraumaState getWorse(int steps)
	{
		return getChanged(steps);
	}
}