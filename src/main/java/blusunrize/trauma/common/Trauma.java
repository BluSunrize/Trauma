/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common;

import blusunrize.trauma.api.IDamageAdapter;
import blusunrize.trauma.api.TraumaApiLib;
import blusunrize.trauma.api.TraumaItems;
import blusunrize.trauma.api.condition.CapabilityTrauma;
import blusunrize.trauma.api.condition.EnumLimb;
import blusunrize.trauma.api.condition.EnumTraumaState;
import blusunrize.trauma.api.effects.ITraumaEffect;
import blusunrize.trauma.api.recovery.CapabilityRecoveryItem;
import blusunrize.trauma.common.damageadapters.*;
import blusunrize.trauma.common.effects.*;
import blusunrize.trauma.common.items.ItemCurative;
import blusunrize.trauma.common.utils.EventHandler;
import blusunrize.trauma.common.utils.TraumaPotion;
import blusunrize.trauma.common.utils.commands.CommandTrauma;
import blusunrize.trauma.common.utils.network.MessageApplyRecoveryItem;
import blusunrize.trauma.common.utils.network.MessageTraumaStatusSync;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
@Mod(modid = Trauma.MODID, name = Trauma.MODNAME, version = Trauma.VERSION)
@Mod.EventBusSubscriber
public class Trauma
{
	public static final String MODID = "trauma";
	public static final String MODNAME = "Trauma";
	public static final String VERSION = "0.9";

	@Mod.Instance(MODID)
	public static Trauma instance = new Trauma();
	@SidedProxy(clientSide = "blusunrize.trauma.client.ClientProxy", serverSide = "blusunrize.trauma.common.CommonProxy")
	public static CommonProxy proxy;

	public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
		TraumaConfig.loadConfig();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		CapabilityTrauma.register();
		CapabilityRecoveryItem.register();

		int messageId = 0;
		packetHandler.registerMessage(MessageTraumaStatusSync.HandlerServer.class, MessageTraumaStatusSync.class, messageId++, Side.SERVER);
		packetHandler.registerMessage(MessageTraumaStatusSync.HandlerClient.class, MessageTraumaStatusSync.class, messageId++, Side.CLIENT);
		packetHandler.registerMessage(MessageApplyRecoveryItem.HandlerServer.class, MessageApplyRecoveryItem.class, messageId++, Side.SERVER);

		/*DamageSources*/
		TraumaApiLib.BLEEDING_DAMAGE = new DamageSource("trauma:bleeding");

		/* Register Damage Handlers */
		IDamageAdapter adapter = new DamageAdapterFall();
		for(String dmg : TraumaConfig.fallDamages)
			TraumaApiLib.registerDamageAdapter(dmg, adapter);

		adapter = new DamageAdapterFallingBlock();
		for(String dmg : TraumaConfig.headDamages)
			TraumaApiLib.registerDamageAdapter(dmg, adapter);

		TraumaApiLib.registerDamageAdapter(DamageSource.DROWN.getDamageType(), new DamageAdapterDrowning());
		TraumaApiLib.registerDamageAdapter(DamageSource.CRAMMING.getDamageType(), new DamageAdapterCramming());

		adapter = new DamageAdapterExplosion();
		for(String dmg : TraumaConfig.explosionDamages)
			TraumaApiLib.registerDamageAdapter(dmg, adapter);

		/* Init all the Effects */
		/*Head*/
		ITraumaEffect effect = new EffectVision();
		ITraumaEffect effect2 = new EffectAmnesia();
		for(EnumTraumaState state : EnumTraumaState.DAMAGED_STATES)
		{
			TraumaApiLib.registerEffect(EnumLimb.HEAD, state, effect);//Vision
			TraumaApiLib.registerEffect(EnumLimb.HEAD, state, effect2);//Amnesia
		}
		effect2 = new EffectBleeding();
		TraumaApiLib.registerEffect(EnumLimb.HEAD, EnumTraumaState.HEAVY, effect2);//Internal Bleeding

		/*Chest*/
		effect = new EffectExhaustion();
		for(EnumTraumaState state : EnumTraumaState.DAMAGED_STATES)
			TraumaApiLib.registerEffect(EnumLimb.CHEST, state, effect);//Exhaustion
		TraumaApiLib.registerEffect(EnumLimb.HEAD, EnumTraumaState.HEAVY, effect2);//Internal Bleeding

		/*Abdomen*/
		for(EnumTraumaState state : EnumTraumaState.EQUAL_OR_WORSE_STATES.get(EnumTraumaState.MEDIUM))
			TraumaApiLib.registerEffect(EnumLimb.ABDOMEN, state, effect2);//Internal Bleeding

		/*Arms*/
		effect = new EffectMining();
		effect2 = new EffectAttackSpeed();
		for(EnumTraumaState state : EnumTraumaState.DAMAGED_STATES)
		{
			TraumaApiLib.registerEffect(EnumLimb.ARM_MAIN, state, effect);
			TraumaApiLib.registerEffect(EnumLimb.ARM_MAIN, state, effect2);
		}
		effect = new EffectOffhandDisable();
		TraumaApiLib.registerEffect(EnumLimb.ARM_OFFHAND, EnumTraumaState.HEAVY, effect);

		/*Legs*/
		effect = new EffectSlowness();
		for(EnumTraumaState state : EnumTraumaState.DAMAGED_STATES)
		{
			TraumaApiLib.registerEffect(EnumLimb.LEG_LEFT, state, effect);
			TraumaApiLib.registerEffect(EnumLimb.LEG_RIGHT, state, effect);
		}
		effect = new EffectNoJump();
		for(EnumTraumaState state : EnumTraumaState.EQUAL_OR_WORSE_STATES.get(EnumTraumaState.MEDIUM))
		{
			TraumaApiLib.registerEffect(EnumLimb.LEG_LEFT, state, effect);
			TraumaApiLib.registerEffect(EnumLimb.LEG_RIGHT, state, effect);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandTrauma());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		TraumaItems.PAINKILLER = new ItemCurative("painkiller", new String[]{"poppyextract"}, (stack, limbCondition) -> limbCondition.getLimb()==EnumLimb.HEAD, 36000, (stack, limbCondition) -> 1f, (player, limbCondition) -> player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,160, 2))).register(event).setContainerItem(Items.BOWL);

		TraumaItems.BANDAGE = new ItemCurative("bandage", new String[]{"normal", "medicated"}, (stack, limbCondition) -> {
			if(limbCondition.getLimb().isLeg()||limbCondition.getLimb().isLeg())
				return limbCondition.getState()==EnumTraumaState.LIGHT;
			if(limbCondition.getLimb()==EnumLimb.ABDOMEN)
				return limbCondition.getState().getDamageIndex()<2;
			if(limbCondition.getLimb()==EnumLimb.CHEST)
				return limbCondition.getState()==EnumTraumaState.MEDIUM;
			return false;
		},0, (stack, limbCondition) -> stack.getMetadata()==1?.66f:.8f, null).register(event);

		TraumaItems.SPLINT = new ItemCurative("splint", (stack, limbCondition) -> (limbCondition.getLimb().isArm()||limbCondition.getLimb().isLeg())&&limbCondition.getState().getDamageIndex()>0, 0, (stack, limbCondition) -> .66f, null).register(event);
	}


	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		TraumaApiLib.POTION_DISFOCUS = new TraumaPotion(new ResourceLocation(MODID, "disfocus"), true, 0x2496a7, 0);
		event.getRegistry().register(TraumaApiLib.POTION_DISFOCUS);
	}
}
