
package thecodewarrior.equipment.client;

import thecodewarrior.equipment.client.gui.GuiEvents;
import thecodewarrior.equipment.client.gui.GuiPlayerExpanded;
import thecodewarrior.equipment.common.Baubles;
import thecodewarrior.equipment.common.CommonProxy;
import thecodewarrior.equipment.common.event.KeyHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerHandlers() {}
	
	@Override
	public void registerKeyBindings() {
		keyHandler = new KeyHandler();
		FMLCommonHandler.instance().bus().register(keyHandler);
		MinecraftForge.EVENT_BUS.register(new GuiEvents());
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world instanceof WorldClient) {
			switch (ID) {
				case Baubles.GUI: return new GuiPlayerExpanded(player);
			}
		}
		return null;
	}
				
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
		
}
