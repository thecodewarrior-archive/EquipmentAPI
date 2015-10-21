package thecodewarrior.equipment.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thecodewarrior.equipment.api.EquipmentType;
import thecodewarrior.equipment.api.IEquipment;

public class SlotEquipment extends Slot
{
	
	EquipmentType type;

    public SlotEquipment(IInventory par2IInventory, EquipmentType type, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.type = type;
    }
    
    public void setType(EquipmentType type) {
    	this.type = type;
    }

    public EquipmentType getType() {
    	return this.type;
    }
    
    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	return this.type == null ? false : stack != null && stack.getItem() != null && this.type.canPlaceStack(stack);
    }
    

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return this.type == null ? true : this.getStack() != null && this.getStack().getItem() != null && this.type.canRemoveStack(this.getStack(), player);
	}


	@Override
    public int getSlotStackLimit()
    {
        return this.type == null ? 0 : this.type.getStackLimit();
    }
    
}
