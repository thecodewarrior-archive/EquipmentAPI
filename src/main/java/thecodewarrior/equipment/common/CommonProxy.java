
package thecodewarrior.equipment.common;

import thecodewarrior.equipment.common.container.ContainerPlayerExpanded;
import thecodewarrior.equipment.common.event.KeyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;



public class CommonProxy implements IGuiHandler {
	
	public KeyHandler keyHandler;
	
	public void registerHandlers() {}
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case Baubles.GUI: return new ContainerPlayerExpanded(player.inventory, !world.isRemote, player);
		}
		return null;
	}

	public World getClientWorld() {
		return null;
	}
		
	
	public void registerKeyBindings() {}

}
