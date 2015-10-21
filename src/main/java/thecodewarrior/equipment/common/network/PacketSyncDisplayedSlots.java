package thecodewarrior.equipment.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import thecodewarrior.equipment.common.container.ContainerPlayerExpanded;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncDisplayedSlots implements IMessage, IMessageHandler<PacketSyncDisplayedSlots, IMessage> {
	
	String[] ids;
	
	public PacketSyncDisplayedSlots() {}
	
	public PacketSyncDisplayedSlots(String[] ids) {
		this.ids = ids;
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		byte nonnulls = 0;
		for(int i = 0; i < ids.length; i++) {
			if(ids[i] != null)
				nonnulls++;
		}
		buffer.writeByte(nonnulls);
		for(int i = 0; i < ids.length; i++) {
			if(ids[i] != null) {
				ByteBufUtils.writeUTF8String(buffer, ids[i]);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		ids = new String[8];
		byte nonnulls = buffer.readByte();
		if(nonnulls > 8)
			nonnulls = 8;
		for(int i = 0; i < nonnulls; i++) {
			ids[i] = ByteBufUtils.readUTF8String(buffer);
		}
	}

	@Override
	public IMessage onMessage(PacketSyncDisplayedSlots message, MessageContext ctx) {
		Container c = Minecraft.getMinecraft().thePlayer.openContainer;
		if(c instanceof ContainerPlayerExpanded) {
			ContainerPlayerExpanded cpe = (ContainerPlayerExpanded)c;
			cpe.updateSlots(message.ids);
		}
		return null;
	}


}
