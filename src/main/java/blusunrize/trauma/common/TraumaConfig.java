/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common;

import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.condition.EnumLimb;
import com.google.common.collect.Sets;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

import java.util.HashMap;

/**
 * @author BluSunrize
 * @since 21.09.2017
 */
@Config(modid = Trauma.MODID)
public class TraumaConfig
{
	@Comment("A list of damages that count as falling and may break legs")
	public static String[] fallDamages = {"fall"};

	@Comment("A list of damages that count as falling blocks, and specifically target the head")
	public static String[] headDamages = {"fallingBlock", "anvil", "flyIntoWall", "lightningBolt"};

	@Comment("A list of damages that count as explosions, causing concussions, and chest + abdomen damage")
	public static String[] explosionDamages = {"explosion", "explosion.player", "fireworks"};

	@Comment("A list of damages that are ignored, because they wouldn't really cause limb damage")
	public static String[] ignoredDamages = {
			"inFire", "onFire", "lava", "hotFloor",
			"starve", "cactus", "cactus", "generic", "wither", "thorns", "outOfWorld", "trauma:bleeding"
	};

	@Comment("The amount of time (in ticks) it takes for head injuries to heal. Array sorted by severity, light, medium, heavy")
	public static int[] recovery_head = {2000, 120000, 336000};

	@Comment("The amount of time (in ticks) it takes for arm injuries to heal. Array sorted by severity, light, medium, heavy")
	public static int[] recovery_arms = {24000, 72000, 168000};

	@Comment("The amount of time (in ticks) it takes for leg injuries to heal. Array sorted by severity, light, medium, heavy")
	public static int[] recovery_legs = {24000, 72000, 168000};

	@Comment("Whether LimbConditions should be healed upon dying+respawning")
	public static boolean clearOnDeath = true;

	@Comment("The amount of health that needs to be recovered at minimum to cause a limb to heal. Set to 0 to make healing potions and more not affect limbs")
	public static int healingThreshold = 4;

	@Comment("Whether absorption hearts should be ignored when calculating limb damage")
	public static boolean ignoreAbsorption = true;

	public static void loadConfig()
	{
		TraumaApiLib.IGNORED_DAMAGES = Sets.newHashSet(ignoredDamages);

		TraumaApiLib.RECOVERY_TIMES = new HashMap<EnumLimb, int[]>()
		{{
			put(EnumLimb.HEAD, recovery_head);
			put(EnumLimb.CHEST, new int[3]);
			put(EnumLimb.ABDOMEN, new int[3]);
			put(EnumLimb.ARM_MAIN, recovery_arms);
			put(EnumLimb.ARM_OFFHAND, recovery_arms);
			put(EnumLimb.LEG_LEFT, recovery_legs);
			put(EnumLimb.LEG_RIGHT, recovery_legs);
		}};
	}
}
