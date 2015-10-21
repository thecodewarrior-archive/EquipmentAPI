package thecodewarrior.equipment.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * 
 * This interface should be extended by items that should have special behaviour in baubles slots
 * 
 * @author Azanor
 * @author TheCodeWarrior
 */

public interface IEquipment {
	
	/**
	 * This method is called once per tick if the item is equipped
	 */
	public void onWornTick(ItemStack itemstack, EntityLivingBase player);
	
	/**
	 * This method is called when the item is equipped by a player
	 */
	public void onEquipped(ItemStack itemstack, EntityLivingBase player);
	
	/**
	 * This method is called when the item is unequipped by a player
	 */
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player);

	/**
	 * can this item be placed in an equipment slot
	 */
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player);
	
	/**
	 * Can this item be removed from an equipment slot
	 */
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player);
}
