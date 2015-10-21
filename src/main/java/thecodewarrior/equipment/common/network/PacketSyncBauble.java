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
import thecodewarrior.equipment.common.container.InventoryBaubles;
import thecodewarrior.equipment.common.lib.PlayerHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncBauble implements IMessage, IMessageHandler<PacketSyncBauble, IMessage> {
	
	String slot;
	int playerId;
	ItemStack bauble=null;
	
	public PacketSyncBauble() {}
	
	public PacketSyncBauble(EntityPlayer player, String slot) {
		this.slot = slot;
		this.bauble = PlayerHandler.getPlayerBaubles(player).getStackInSlot(slot);
		this.playerId = player.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, slot);
		buffer.writeInt(playerId);
		PacketBuffer pb = new PacketBuffer(buffer);
		try { pb.writeItemStackToBuffer(bauble); } catch (IOException e) {}
	}

	@Override
	public void fromBytes(ByteBuf buffer) 
	{
		slot = ByteBufUtils.readUTF8String(buffer);
		
		playerId = buffer.readInt();
		PacketBuffer pb = new PacketBuffer(buffer);
		try { bauble = pb.readItemStackFromBuffer(); } catch (IOException e) {}
	}

	@Override
	public IMessage onMessage(PacketSyncBauble message, MessageContext ctx) {
		World world = EquipmentMod.proxy.getClientWorld();
		if (world==null) return null;
		Entity p = world.getEntityByID(message.playerId);
		if (p !=null && p instanceof EntityPlayer) {
			InventoryBaubles i = PlayerHandler.getPlayerBaubles((EntityPlayer) p);
			i.stackList.put(message.slot, message.bauble);
		}
		return null;
	}


}
