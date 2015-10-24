package thecodewarrior.equipment.client.gui;

import java.lang.reflect.Method;

import thecodewarrior.equipment.common.network.PacketHandler;
import thecodewarrior.equipment.common.network.PacketOpenEquipmentInventory;
import thecodewarrior.equipment.common.network.PacketOpenNormalInventory;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiEvents {	
	
	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {

		if (event.gui instanceof GuiInventory || event.gui instanceof GuiPlayerExpanded) {
			
			int xSize = 176;
		    int ySize = 166;
			
			int guiLeft = (event.gui.width - xSize) / 2;
	        int guiTop = (event.gui.height - ySize) / 2;
	        
	        if (!event.gui.mc.thePlayer.getActivePotionEffects().isEmpty() && isNeiHidden()) {
	        	guiLeft = 160 + (event.gui.width - xSize - 200) / 2;
	        }
			
			event.buttonList.add(new GuiEquipmentButton(56, guiLeft + 68, guiTop + 68, 10, 10, 
					I18n.format((event.gui instanceof GuiInventory)?"button.equipment":"button.normal", new Object[0])));
		}
		
	}

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Post event) {

		if (event.gui instanceof GuiInventory) {
			if (event.button.id == 56) {
				PacketHandler.INSTANCE.sendToServer(new PacketOpenEquipmentInventory(event.gui.mc.thePlayer));
			}
		}
		
		if (event.gui instanceof GuiPlayerExpanded) {
			if (event.button.id == 56) {
				event.gui.mc.displayGuiScreen(new GuiInventory(event.gui.mc.thePlayer));
				PacketHandler.INSTANCE.sendToServer(new PacketOpenNormalInventory(event.gui.mc.thePlayer));
			}
		}
	}
	
	static Method isNEIHidden;
	boolean isNeiHidden() {
		boolean hidden=true;
		try {
			if (isNEIHidden==null) {
				Class fake = Class.forName("codechicken.nei.NEIClientConfig");
			    isNEIHidden = fake.getMethod("isHidden");
			}
			hidden = (Boolean) isNEIHidden.invoke(null);
	    } catch(Exception ex) { }		
		return hidden;
	}
}
