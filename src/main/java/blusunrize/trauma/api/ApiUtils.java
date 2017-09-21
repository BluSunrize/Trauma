package blusunrize.trauma.api;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 * <p>
 * Copyright:
 *
 * @author BluSunrize
 * @since 21.09.2017
 */
public class ApiUtils
{
	@SideOnly(Side.CLIENT)
	public static String getLocalizedLimb(@Nonnull EnumLimb limb)
	{
		return I18n.format(limb.getUnlocalizedName());
	}

	public static String getUnlocalizedDamage(@Nonnull EnumLimb limb, @Nonnull EnumTraumaState state)
	{
		if(state==EnumTraumaState.NONE)
			return "desc.trauma.state."+state.name();
		return "desc.trauma.limb."+limb.name()+".state."+state.name();
	}

	@SideOnly(Side.CLIENT)
	public static String getLocalizedDamage(@Nonnull EnumLimb limb, @Nonnull EnumTraumaState state)
	{
		return I18n.format(getUnlocalizedDamage(limb, state));
	}
}
