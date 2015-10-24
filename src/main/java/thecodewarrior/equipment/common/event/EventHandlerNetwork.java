package thecodewarrior.equipment.common.event;

import thecodewarrior.equipment.common.container.InventoryEquipment;
import thecodewarrior.equipment.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;

public class EventHandlerNetwork {
	
	@SubscribeEvent    
	public void playerLoggedInEvent (PlayerEvent.PlayerLoggedInEvent event)    {    
		Side side = FMLCommonHandler.instance().getEffectiveSide();        
		if (side == Side.SERVER)        {
			syncEquipment(event.player);
		}
	}
	
	public static void syncEquipment(EntityPlayer player) {
		InventoryEquipment i = PlayerHandler.getPlayerEquipmentInventory(player);
		for (String key : i.getStackMap().keySet()) {
			i.syncSlotToClients(key);
		}
	}
	
	
}
