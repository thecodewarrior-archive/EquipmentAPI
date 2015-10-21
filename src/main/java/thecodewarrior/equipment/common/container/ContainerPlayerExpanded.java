package thecodewarrior.equipment.common.container;

import thecodewarrior.equipment.api.EquipmentApi;
import thecodewarrior.equipment.api.EquipmentClass;
import thecodewarrior.equipment.api.IBauble;
import thecodewarrior.equipment.common.lib.PlayerHandler;
import thecodewarrior.equipment.common.lib.SlotRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayerExpanded extends Container
{
    /**
     * The crafting matrix inventory.
     */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();
    public InventoryBaubles baubles;
    public SlotEquipment[] slots = new SlotEquipment[8];
    /**
     * Determines if inventory manipulation should be handled.
     */
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

    public ContainerPlayerExpanded(InventoryPlayer playerInv, boolean par2, EntityPlayer player)
    {
        this.isLocalWorld = par2;
        this.thePlayer = player;
        baubles = new InventoryBaubles(player);
        baubles.setEventHandler(this);
        if (!player.worldObj.isRemote) {
        	baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }
        
        this.addSlotToContainer(new SlotCrafting(playerInv.player, this.craftMatrix, this.craftResult, 0, 144, 36));
        int i;
        int j;

        for (i = 0; i < 2; ++i)
        {
            for (j = 0; j < 2; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            final int k = i;
            this.addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 1 - i, 8, 8 + i * 18)
            {
                @Override
                public int getSlotStackLimit() { return 1; }
                @Override
                public boolean isItemValid(ItemStack par1ItemStack)
                {
                    if (par1ItemStack == null) return false;
                    return par1ItemStack.getItem().isValidArmor(par1ItemStack, k, thePlayer);
                }
            });
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        
        int left = 180, top = 12, dist = 18;
        
        for(i = 0; i < 8; i++) {
        	slots[i] = new SlotEquipment(baubles, null, i, left, top + i * dist);
        	this.addSlotToContainer(slots[i]);
        }

        this.onCraftMatrixChanged(this.craftMatrix);
        updatePage(0);
    }

    public void updatePage(int page) {
    	if(page < 0)
    		page = 0;
    	baubles.updatePage(page);
    	for(int i = 0; i < 8; i++) {
    		slots[i].setType(SlotRegistry.getEquipment(baubles.ids[i]));
    	}
    }
    
    public void updateSlots(String[] ids) {
    	baubles.ids = ids;
    	for(int i = 0; i < 8; i++) {
    		slots[i].setType(SlotRegistry.getEquipment(baubles.ids[i]));
    	}
    }
    
    int page = 0;
    
    public int getCurrentPage() {
    	return page;
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

            if (itemstack != null)
            {
                player.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }

        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
        if (!player.worldObj.isRemote) {
        	PlayerHandler.setPlayerBaubles(player, baubles);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1Player, int par2)
    {
    	ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        boolean didEquipmentFit = false;
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if(par2 < 45)
            {
            	for(int i = 0; i < 8; i++) {
            		SlotEquipment s = slots[i];
            		if(s != null && s.isItemValid(itemstack1)) {
            			ItemStack alreadyIn = s.getStack();
            			if(alreadyIn != null && alreadyIn.stackSize >= s.getSlotStackLimit()) {
            				continue;
            			}
            			if(alreadyIn == null || ( alreadyIn.isItemEqual(itemstack1) && ItemStack.areItemStackTagsEqual(alreadyIn, itemstack1) && alreadyIn.stackSize < s.getSlotStackLimit() )) {
            				int amountCanFit = s.getSlotStackLimit();
            				if(alreadyIn != null) {
            					amountCanFit -= alreadyIn.stackSize;
            				}
	        				int amountToTransfer = itemstack1.stackSize;
	        				if(amountToTransfer > amountCanFit) {
	        					amountToTransfer = amountCanFit;
	        				}
	        				itemstack1.stackSize -= amountToTransfer;
	        				if(alreadyIn != null) {
	        					alreadyIn.stackSize += amountToTransfer;
	        				} else {
	        					ItemStack toinsert = itemstack1.copy();
	        					toinsert.stackSize = amountToTransfer;
	        					s.putStack(toinsert);
	        				}
	        				didEquipmentFit = true;
            			}
            		}
            	}
            }
            if(didEquipmentFit)
            {}
            else if (par2 >= 1 && par2 < 5)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 5 && par2 < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
            {
                int j = 5 + ((ItemArmor)itemstack.getItem()).armorType;

                if (!this.mergeItemStack(itemstack1, j, j + 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 9 && par2 < 36)
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 36 && par2 < 45)
            {
                if (!this.mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, 45, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }
            if(didEquipmentFit) {
            	return null;
            }

            slot.onPickupFromSlot(par1Player, itemstack1);
        }

        return itemstack;
    }
    
    private void unequipBauble(ItemStack stack) {
//    	if (stack.getItem() instanceof IBauble) {
//    		((IBauble)stack.getItem()).onUnequipped(stack, thePlayer);
//    	}
    }
    
    
    
    @Override
	public void putStacksInSlots(ItemStack[] p_75131_1_) {
		baubles.blockEvents=true;
		super.putStacksInSlots(p_75131_1_);
	}
    
    
    

	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, Slot ss)
    {
        boolean flag1 = false;
        int k = par2;

        if (par4)
        {
            k = par3 - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable())
        {
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
                {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;
                    if (l <= par1ItemStack.getMaxStackSize())
                    {
                    	if (ss instanceof SlotEquipment) unequipBauble(par1ItemStack);
                    	par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
                    {
                    	if (ss instanceof SlotEquipment) unequipBauble(par1ItemStack);
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (par1ItemStack.stackSize > 0)
        {
            if (par4)
            {
                k = par3 - 1;
            }
            else
            {
                k = par2;
            }

            while (!par4 && k < par3 || par4 && k >= par2)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {
                	if (ss instanceof SlotEquipment) unequipBauble(par1ItemStack);
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }
        return flag1;
    }

    @Override
    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }
    
    

}
