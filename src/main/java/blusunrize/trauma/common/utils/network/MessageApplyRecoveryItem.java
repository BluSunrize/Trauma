/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package blusunrize.trauma.common.utils.network;

import blusunrize.trauma.api.condition.CapabilityTrauma;
import blusunrize.trauma.api.condition.EnumLimb;
import blusunrize.trauma.api.condition.LimbCondition;
import blusunrize.trauma.api.condition.TraumaStatus;
import blusunrize.trauma.api.recovery.CapabilityRecoveryItem;
import blusunrize.trauma.api.recovery.IRecoveryItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author BluSunrize
 * @since 20.09.2017
 */
public class MessageApplyRecoveryItem implements IMessage
{
	int dimension;
	int entityID;
	EnumLimb limb;

	public MessageApplyRecoveryItem(EntityPlayer player, EnumLimb limb)
	{
		this.dimension = player.world.provider.getDimension();
		this.entityID = player.getEntityId();
		this.limb = limb;
	}

	public MessageApplyRecoveryItem()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readInt();
		this.entityID = buf.readInt();
		this.limb = EnumLimb.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.dimension);
		buf.writeInt(this.entityID);
		buf.writeInt(this.limb.ordinal());
	}

	public static class HandlerServer implements IMessageHandler<MessageApplyRecoveryItem, IMessage>
	{
		@Override
		public IMessage onMessage(MessageApplyRecoveryItem message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.dimension);
			if(world!=null)
			{
				Entity entity = world.getEntityByID(message.entityID);
				if(entity!=null&&entity instanceof EntityPlayer)
				{
					ItemStack held = ((EntityPlayer)entity).inventory.getItemStack();
					if(!held.isEmpty())
					{
						if(!held.isEmpty()&&(held.getItem() instanceof IRecoveryItem||held.hasCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null)))
						{
							IRecoveryItem iRecoveryItem = held.getItem() instanceof IRecoveryItem?(IRecoveryItem)held.getItem(): held.getCapability(CapabilityRecoveryItem.REVOVERYITEM_CAPABILITY, null);
							TraumaStatus status = entity.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
							LimbCondition limbCondition = status.getLimbCondition(message.limb);
							limbCondition.addRecoveryItem(iRecoveryItem.getIdentifier(held), iRecoveryItem.getDuration(held, (EntityPlayer)entity, limbCondition));
							int newTimer = Math.round(limbCondition.getRecoveryTimer()*iRecoveryItem.getRecoveryTimeModifier(held, (EntityPlayer)entity, limbCondition));
							limbCondition.setRecoveryTimer(newTimer);
							iRecoveryItem.onApply(held, (EntityPlayer)entity, limbCondition);
							held.shrink(1);
						}
					}
				}
			}
			return null;
		}
	}
}
