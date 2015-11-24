package thecodewarrior.equipment.api;

import java.util.List;

import javax.annotation.Nonnull;

import java.util.Arrays;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author TheCodeWarrior
 */
public abstract class EquipmentType {

	/**
	 * Can the supplied ItemStack be placed in this slot
	 * @param stack
	 * @return
	 */
	public abstract boolean canPlaceStack(ItemStack stack);
	
	/**
	 * Can the supplied ItemStack be removed from this slot
	 * @param stack
	 * @param player
	 * @return
	 */
	public abstract boolean canRemoveStack(ItemStack stack, EntityPlayer player);
	
	/**
	 * The image that will be overlaid on the slot, below the item
	 * @param stack
	 * @return
	 */
	public abstract ResourceLocation getSlotOverlay(ItemStack stack);
	
	/**
	 * The description of this slot, \n will work to create multiple lines
	 * @param player
	 * @return
	 */
	public abstract String getSlotDescription(EntityPlayer player);
	
	/**
	 * The maximum stack size that can be placed in this slot
	 * @return
	 */
	public int getStackLimit() {
		return 1;
	}
	
	/**
	 * The list of lines in the description tooltip
	 * @param stack
	 * @param player
	 * @return
	 */
	public List<String> getTooltip(ItemStack stack, EntityPlayer player) {
		if(stack != null)
			return null;
		String desc = getSlotDescription(player);
		if(desc != null) {
			return Arrays.asList(desc.split("\\r?\\n"));
		}
		return null;
	}
	
	/**
	 * Called after a slot of this type changes, when possible use this instead of repeatedly calling EquipmentApi.getEquipment()
	 * 
	 * @param from The itemstack that was in the slot
	 * @param to The itemstack that is now in the slot
	 * @param player The player in question
	 */
	public void onChange(ItemStack from, ItemStack to, EntityPlayer player) {}
}
