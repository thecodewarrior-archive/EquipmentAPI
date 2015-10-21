package thecodewarrior.equipment.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author TheCodeWarrior
 */
public abstract class EquipmentType {

	public abstract boolean canPlaceStack(ItemStack stack);
	public abstract boolean canRemoveStack(ItemStack stack, EntityPlayer player);
	public abstract ResourceLocation getSlotOverlay(ItemStack stack);
	public int getStackLimit() {
		return 1;
	}
}
