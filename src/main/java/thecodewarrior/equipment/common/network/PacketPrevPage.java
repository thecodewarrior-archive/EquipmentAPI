package thecodewarrior.equipment.common.network;

import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.container.ContainerPlayerExpanded;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPrevPage implements IMessage, IMessageHandler<PacketPrevPage, IMessage> {
	
	public PacketPrevPage() {}
	
	public PacketPrevPage(EntityPlayer player) {}

	@Override
	public void toBytes(ByteBuf buffer) {}

	@Override
	public void fromBytes(ByteBuf buffer) {}

	@Override
	public IMessage onMessage(PacketPrevPage message, MessageContext ctx) {
		Container c = ctx.getServerHandler().playerEntity.openContainer;
		if(c instanceof ContainerPlayerExpanded) {
			ContainerPlayerExpanded cpe = (ContainerPlayerExpanded)c;
			cpe.updatePage(cpe.getCurrentPage()-1);
			return new PacketSyncDisplayedSlots(cpe.equipment.ids);
		}
		return null;
	}


}
