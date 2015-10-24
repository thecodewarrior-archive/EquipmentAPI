package thecodewarrior.equipment.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.container.InventoryEquipment;
import thecodewarrior.equipment.common.lib.PlayerHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncEquipment implements IMessage, IMessageHandler<PacketSyncEquipment, IMessage> {
	
	String slot;
	int playerId;
	ItemStack item=null;
	
	public PacketSyncEquipment() {}
	
	public PacketSyncEquipment(EntityPlayer player, String slot) {
		this.slot = slot;
		this.item = PlayerHandler.getPlayerEquipmentInventory(player).getStack(slot);
		this.playerId = player.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, slot);
		buffer.writeInt(playerId);
		PacketBuffer pb = new PacketBuffer(buffer);
		try { pb.writeItemStackToBuffer(item); } catch (IOException e) {}
	}

	@Override
	public void fromBytes(ByteBuf buffer) 
	{
		slot = ByteBufUtils.readUTF8String(buffer);
		
		playerId = buffer.readInt();
		PacketBuffer pb = new PacketBuffer(buffer);
		try { item = pb.readItemStackFromBuffer(); } catch (IOException e) {}
	}

	@Override
	public IMessage onMessage(PacketSyncEquipment message, MessageContext ctx) {
		World world = EquipmentMod.proxy.getClientWorld();
		if (world==null) return null;
		Entity p = world.getEntityByID(message.playerId);
		if (p !=null && p instanceof EntityPlayer) {
			InventoryEquipment i = PlayerHandler.getPlayerEquipmentInventory((EntityPlayer) p);
			i.setStack(message.slot, message.item);
		}
		return null;
	}


}
