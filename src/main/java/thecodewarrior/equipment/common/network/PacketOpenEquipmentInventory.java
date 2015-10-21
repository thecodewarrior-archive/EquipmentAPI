package thecodewarrior.equipment.common.network;

import thecodewarrior.equipment.common.EquipmentMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenEquipmentInventory implements IMessage, IMessageHandler<PacketOpenEquipmentInventory, IMessage> {
	
	public PacketOpenEquipmentInventory() {}
	
	public PacketOpenEquipmentInventory(EntityPlayer player) {}

	@Override
	public void toBytes(ByteBuf buffer) {}

	@Override
	public void fromBytes(ByteBuf buffer) {}

	@Override
	public IMessage onMessage(PacketOpenEquipmentInventory message, MessageContext ctx) {
		ctx.getServerHandler().playerEntity.openGui(EquipmentMod.instance, EquipmentMod.GUI, ctx.getServerHandler().playerEntity.worldObj, (int)ctx.getServerHandler().playerEntity.posX, (int)ctx.getServerHandler().playerEntity.posY, (int)ctx.getServerHandler().playerEntity.posZ);
		return null;
	}


}
