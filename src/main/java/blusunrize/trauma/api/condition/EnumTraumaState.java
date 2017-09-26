/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.api.condition;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;

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

	public static final EnumTraumaState[] DAMAGED_STATES;
	public static final HashMap<EnumTraumaState, EnumTraumaState[]> EQUAL_OR_WORSE_STATES = new HashMap(4);

	EnumTraumaState(TextFormatting textColor)
	{
		this.textColor = textColor;
	}

	static
	{
		DAMAGED_STATES = new EnumTraumaState[]{LIGHT, MEDIUM, HEAVY};
		EQUAL_OR_WORSE_STATES.put(NONE, new EnumTraumaState[]{NONE, LIGHT, MEDIUM, HEAVY});
		EQUAL_OR_WORSE_STATES.put(LIGHT, new EnumTraumaState[]{LIGHT, MEDIUM, HEAVY});
		EQUAL_OR_WORSE_STATES.put(MEDIUM, new EnumTraumaState[]{MEDIUM, HEAVY});
		EQUAL_OR_WORSE_STATES.put(HEAVY, new EnumTraumaState[]{HEAVY});
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

	public int getDamageIndex()
	{
		return ordinal()-1;
	}
}