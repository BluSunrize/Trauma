/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.client;

/**
 * Utility class for rendering an other client only calls
 *
 * @author BluSunrize
 * @since 22.09.2017
 */
public class ClientUtils
{
	public static String ticksToFormattedTime(long ticks)
	{
		long seconds = ticks / 20;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds = seconds % 60;
		minutes = minutes % 60;
		return (hours>0?String.format("%02d",hours)+":":"")+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
	}
}
