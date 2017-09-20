package blusunrize.trauma.common.utils.network;

import blusunrize.trauma.api.CapabilityTrauma;
import blusunrize.trauma.api.TraumaStatus;
import blusunrize.trauma.common.Trauma;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 * <p>
 * Copyright:
 *
 * @author BluSunrize
 * @since 20.09.2017
 */
public class MessageTraumaStatusSync implements IMessage
{
	int dimension;
	int entityID;
	boolean request = false;
	NBTTagCompound status;

	public MessageTraumaStatusSync(Entity entity, Object o)
	{
		this.dimension = entity.world.provider.getDimension();
		this.entityID = entity.getEntityId();
		if(o instanceof TraumaStatus)
			status = ((TraumaStatus)o).serializeNBT();
		else
			request = true;
	}

	public MessageTraumaStatusSync()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readInt();
		this.entityID = buf.readInt();
		this.request = buf.readBoolean();
		if(!request)
			this.status = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.dimension);
		buf.writeInt(this.entityID);
		buf.writeBoolean(this.request);
		if(!request)
			ByteBufUtils.writeTag(buf, this.status);
	}

	public static class HandlerServer implements IMessageHandler<MessageTraumaStatusSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTraumaStatusSync message, MessageContext ctx)
		{
			World world = DimensionManager.getWorld(message.dimension);
			if(world!=null)
			{
				Entity entity = world.getEntityByID(message.entityID);
				if(entity!=null&&entity.hasCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null))
				{
					TraumaStatus status = entity.getCapability(CapabilityTrauma.TRAUMA_CAPABILITY, null);
					if(status!=null)
						Trauma.packetHandler.sendToAll(new MessageTraumaStatusSync(entity, status));
				}
			}
			return null;
		}
	}

	public static class HandlerClient implements IMessageHandler<MessageTraumaStatusSync, IMessage>
	{
		@Override
		public IMessage onMessage(MessageTraumaStatusSync message, MessageContext ctx)
		{
			World world = Trauma.proxy.getClientWorld();
			if(world!=null)
			{
				Entity entity = world.getEntityByID(message.entityID);
				if(entity instanceof EntityPlayer)
				{
					TraumaStatus status = new TraumaStatus();
					status.deserializeNBT(message.status);
					CapabilityTrauma.playerCapabilityMap.put((EntityPlayer)entity, status);
				}
			}
			return null;
		}
	}
}
