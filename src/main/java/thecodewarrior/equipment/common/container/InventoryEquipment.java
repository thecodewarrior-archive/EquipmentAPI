package thecodewarrior.equipment.common.container;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private Map<String, ItemStack> stackMap;
	public String[] ids;
	private Container eventHandler;
	public WeakReference<EntityPlayer> player;
	public boolean blockEvents=false;

	public InventoryEquipment(EntityPlayer player) {
		this.stackMap = new HashMap<String, ItemStack>();
		this.ids = new String[8];
		updatePage(0);
		this.player = new WeakReference<EntityPlayer>(player);
	}

	/**
	 * INTERNAL USE ONLY - use {@code setStack(id, stack)} to set values
	 * 
	 * Sets the stack map to the provided map
	 * @param map
	 */
	public void setStackMap(Map<String, ItemStack> map) {
		Map<String, ItemStack> old = stackMap;
		stackMap = map;
		Set<String> keys = new HashSet<String>(map.keySet());
		keys.addAll(old.keySet());
		for(String key : keys) {
			SlotRegistry.getEquipment(key).onChange(old.get(key), map.get(key), player.get());
		}
	}
	
	/**
	 * Gets an un-modifiable reference to the stack map 
	 * @return
	 */
	public Map<String, ItemStack> getStackMap() {
		return Collections.unmodifiableMap(stackMap);
	}
	
	/**
	 * INTERNAL USE ONLY - use {@code setStack(id, stack)} to set values<br><br>
	 * Gets the modifiable reference to the stack map
	 * @return
	 */
	public Map<String, ItemStack> getRawStackMap() {
		return stackMap;
	}
	
	public Container getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(Container eventHandler) {
		this.eventHandler = eventHandler;
	}
	
	
	/**
	 * Sets the stack at the provided id.
	 * @param id
	 * @param stack
	 */
	public void setStack(String id, ItemStack stack) {
		ItemStack oldStack = stackMap.get(id);
		stackMap.put(id, stack);
		SlotRegistry.getEquipment(id).onChange(oldStack, stack, player.get());
	}
	
	/**
	 * Gets the stack at the provided id
	 * @param id
	 * @return
	 */
	public ItemStack getStack(String id) {
		return this.stackMap.get(id);
	}
	
	/**
	 * INTERNAL USE ONLY - Use {@code getStackMap().keySet()} to iterate over the slots
	 */
	@Override
	public int getSizeInventory() {
		return 8;
	}

	/**
	 * INTERNAL USE ONLY - Use {@code getStack(id)} to get values
	 */
	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.stackMap.get(ids[par1]);
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
		if (this.stackMap.get(ids[par1]) != null) {
			ItemStack itemstack = this.stackMap.get(ids[par1]);
			this.stackMap.put(ids[par1], null);
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
		if (this.stackMap.get(ids[par1]) != null) {
			ItemStack itemstack;

			if (this.stackMap.get(ids[par1]).stackSize <= par2) {
				itemstack = this.stackMap.get(ids[par1]);

				if (itemstack != null && itemstack.getItem() instanceof IEquipment) {
					((IEquipment) itemstack.getItem()).onUnequipped(itemstack,
							player.get());
				}
				
				this.stackMap.put(ids[par1], null);

				if (eventHandler != null)
					this.eventHandler.onCraftMatrixChanged(this);
				syncSlotToClients(par1);
				return itemstack;
			} else {
				itemstack = this.stackMap.get(ids[par1]).splitStack(par2);
				
				if (itemstack != null && itemstack.getItem() instanceof IEquipment) {
					((IEquipment) itemstack.getItem()).onUnequipped(itemstack,
							player.get());
				}
				
				if (this.stackMap.get(ids[par1]).stackSize == 0) {
					this.stackMap.put(ids[par1], null);
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
		this.stackMap.put(ids[par1], stack);
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
		for (String key : stackMap.keySet()) {
			if (this.stackMap.get(key) != null) {
				invSlot = new NBTTagCompound();
				invSlot.setString("Slot", key);
				this.stackMap.get(key).writeToNBT(invSlot);
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
				this.stackMap.put(slot, itemstack);
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
			if (this.stackMap.get(ids[i]) != null) {
				EntityItem ei = new EntityItem(player.get().worldObj,
						player.get().posX, player.get().posY
								+ player.get().eyeHeight, player.get().posZ,
						this.stackMap.get(ids[i]).copy());
				ei.delayBeforeCanPickup = 40;
				float f1 = player.get().worldObj.rand.nextFloat() * 0.5F;
				float f2 = player.get().worldObj.rand.nextFloat()
						* (float) Math.PI * 2.0F;
				ei.motionX = (double) (-MathHelper.sin(f2) * f1);
				ei.motionZ = (double) (MathHelper.cos(f2) * f1);
				ei.motionY = 0.20000000298023224D;
				drops.add(ei);
				this.stackMap.put(ids[i], null);
				syncSlotToClients(i);
			}
		}
	}
	
	public void dropItemsAt(ArrayList<EntityItem> drops, Entity e) {
		for (String id : this.stackMap.keySet()) {
			if (this.stackMap.get(id) != null) {
				EntityItem ei = new EntityItem(e.worldObj,
						e.posX, e.posY + e.getEyeHeight(), e.posZ,
						this.stackMap.get(id).copy());
				ei.delayBeforeCanPickup = 40;
				float f1 = e.worldObj.rand.nextFloat() * 0.5F;
				float f2 = e.worldObj.rand.nextFloat() * (float) Math.PI * 2.0F;
				ei.motionX = (double) (-MathHelper.sin(f2) * f1);
				ei.motionZ = (double) (MathHelper.cos(f2) * f1);
				ei.motionY = 0.20000000298023224D;
				drops.add(ei);
				this.stackMap.put(id, null);
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
