package thecodewarrior.equipment.common.container;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import thecodewarrior.equipment.api.IEquipment;
import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.lib.SlotRegistry;
import thecodewarrior.equipment.common.network.PacketHandler;
import thecodewarrior.equipment.common.network.PacketSyncEquipment;

public class InventoryEquipment implements IInventory {
	public Map<String, ItemStack> stackList;
	public String[] ids;
	private Container eventHandler;
	public WeakReference<EntityPlayer> player;
	public boolean blockEvents=false;

	public InventoryEquipment(EntityPlayer player) {
		this.stackList = new HashMap<String, ItemStack>();
		this.ids = new String[8];
		updatePage(0);
		this.player = new WeakReference<EntityPlayer>(player);
	}

	public Container getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(Container eventHandler) {
		this.eventHandler = eventHandler;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return 8;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.stackList.get(ids[par1]);
	}
	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(String par1) {
		return this.stackList.get(par1);
	}
	
	public void updatePage(int page) {
		if(page < 0)
    		page = 0;
    	for(int i = 0; i < 8; i++) {
    		ids[i] = SlotRegistry.getEquipmentIdAtIndex(i + page*8);
    	}
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return "";
	}

	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.stackList.get(ids[par1]) != null) {
			ItemStack itemstack = this.stackList.get(ids[par1]);
			this.stackList.put(ids[par1], null);
			return itemstack;
		} else {
			return null;
		}
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.stackList.get(ids[par1]) != null) {
			ItemStack itemstack;

			if (this.stackList.get(ids[par1]).stackSize <= par2) {
				itemstack = this.stackList.get(ids[par1]);

				if (itemstack != null && itemstack.getItem() instanceof IEquipment) {
					((IEquipment) itemstack.getItem()).onUnequipped(itemstack,
							player.get());
				}
				
				this.stackList.put(ids[par1], null);

				if (eventHandler != null)
					this.eventHandler.onCraftMatrixChanged(this);
				syncSlotToClients(par1);
				return itemstack;
			} else {
				itemstack = this.stackList.get(ids[par1]).splitStack(par2);
				
				if (itemstack != null && itemstack.getItem() instanceof IEquipment) {
					((IEquipment) itemstack.getItem()).onUnequipped(itemstack,
							player.get());
				}
				
				if (this.stackList.get(ids[par1]).stackSize == 0) {
					this.stackList.put(ids[par1], null);
				}
				
				if (eventHandler != null)
					this.eventHandler.onCraftMatrixChanged(this);
				syncSlotToClients(par1);
				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		this.stackList.put(ids[par1], stack);
		if (eventHandler != null)
			this.eventHandler.onCraftMatrixChanged(this);
		syncSlotToClients(par1);
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved
	 * to disk later - the game won't think it hasn't changed and skip it.
	 */
	@Override
	public void markDirty() {
		try {
			player.get().inventory.markDirty();
		} catch (Exception e) {
		}
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return true;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		return true;
	}

	public void saveNBT(EntityPlayer player) {
		NBTTagCompound tags = player.getEntityData();
		saveNBT(tags);
	}

	public void saveNBT(NBTTagCompound tags) {
		NBTTagList tagList = new NBTTagList();
		NBTTagCompound invSlot;
		for (String key : stackList.keySet()) {
			if (this.stackList.get(key) != null) {
				invSlot = new NBTTagCompound();
				invSlot.setString("Slot", key);
				this.stackList.get(key).writeToNBT(invSlot);
				tagList.appendTag(invSlot);
			}
		}
		tags.setTag("Equipment.Inventory", tagList);
	}

	public void readNBT(EntityPlayer player) {
		NBTTagCompound tags = player.getEntityData();
		readNBT(tags);
	}

	public void readNBT(NBTTagCompound tags) {
		NBTTagList tagList = tags.getTagList("Equipment.Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) tagList
					.getCompoundTagAt(i);
			String slot = nbttagcompound.getString("Slot");
			ItemStack itemstack = ItemStack
					.loadItemStackFromNBT(nbttagcompound);
			if (itemstack != null) {
				this.stackList.put(slot, itemstack);
			}
		}
//		for (int i = 0; i < tagList.tagCount(); ++i) {
//			NBTTagCompound nbttagcompound = (NBTTagCompound) tagList
//					.getCompoundTagAt(i);
//			int j = nbttagcompound.getByte("Slot") & 255;
//			ItemStack itemstack = ItemStack
//					.loadItemStackFromNBT(nbttagcompound);
//			if (itemstack != null) {
//				this.stackList.put(ids[j], itemstack);
//			}
//		}
	}

	public void dropItems(ArrayList<EntityItem> drops) {
		for (int i = 0; i < 4; ++i) {
			if (this.stackList.get(ids[i]) != null) {
				EntityItem ei = new EntityItem(player.get().worldObj,
						player.get().posX, player.get().posY
								+ player.get().eyeHeight, player.get().posZ,
						this.stackList.get(ids[i]).copy());
				ei.delayBeforeCanPickup = 40;
				float f1 = player.get().worldObj.rand.nextFloat() * 0.5F;
				float f2 = player.get().worldObj.rand.nextFloat()
						* (float) Math.PI * 2.0F;
				ei.motionX = (double) (-MathHelper.sin(f2) * f1);
				ei.motionZ = (double) (MathHelper.cos(f2) * f1);
				ei.motionY = 0.20000000298023224D;
				drops.add(ei);
				this.stackList.put(ids[i], null);
				syncSlotToClients(i);
			}
		}
	}
	
	public void dropItemsAt(ArrayList<EntityItem> drops, Entity e) {
		for (String id : this.stackList.keySet()) {
			if (this.stackList.get(id) != null) {
				EntityItem ei = new EntityItem(e.worldObj,
						e.posX, e.posY + e.getEyeHeight(), e.posZ,
						this.stackList.get(id).copy());
				ei.delayBeforeCanPickup = 40;
				float f1 = e.worldObj.rand.nextFloat() * 0.5F;
				float f2 = e.worldObj.rand.nextFloat() * (float) Math.PI * 2.0F;
				ei.motionX = (double) (-MathHelper.sin(f2) * f1);
				ei.motionZ = (double) (MathHelper.cos(f2) * f1);
				ei.motionY = 0.20000000298023224D;
				drops.add(ei);
				this.stackList.put(id, null);
				syncSlotToClients(id);
			}
		}
	}
	public void syncSlotToClients(String slot) {
		try {
			if (EquipmentMod.proxy.getClientWorld() == null && slot != null) {
				PacketHandler.INSTANCE.sendToAll(new PacketSyncEquipment(player
						.get(), slot));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void syncSlotToClients(int slot) {
		syncSlotToClients(ids[slot]);
	}
}
