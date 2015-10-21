package thecodewarrior.equipment.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

/**
 * @author Azanor
 * @author TheCodeWarrior
 */
public class EquipmentApi 
{
	static Method getEquipmentInventory;
	static Method registerSlot;
	static Method getEquipmentStack;
	
	/**
	 * Retrieves the equipment inventory for the supplied player
	 */
	public static IInventory getEquipment(EntityPlayer player)
	{
		IInventory ot = null;
		
	    try
	    {
	        if(getEquipmentInventory == null) 
	        {
	            Class<?> fake = Class.forName("thecodewarrior.equipment.common.lib.PlayerHandler");
	            getEquipmentInventory = fake.getMethod("getPlayerEquipmentInventory", EntityPlayer.class);
	        }
	        
	        ot = (IInventory) getEquipmentInventory.invoke(null, player);
	    } 
	    catch(Exception ex) 
	    { 
	    	FMLLog.warning("[Equipment API] Could not invoke thecodewarrior.equipment.common.lib.PlayerHandler method getPlayerEquipmentInventory");
	    }
	    
		return ot;
	}
	
	public static ItemStack getEquipment(EntityPlayer player, String id)
	{
		ItemStack ot = null;
		
	    try
	    {
	        if(getEquipmentStack == null) 
	        {
	            Class<?> fake = Class.forName("thecodewarrior.equipment.common.lib.PlayerHandler");
	            getEquipmentStack = fake.getMethod("getPlayerEquipment", EntityPlayer.class, String.class);
	        }
	        
	        ot = (ItemStack) getEquipmentInventory.invoke(null, player, id);
	    } 
	    catch(Exception ex) 
	    { 
	    	FMLLog.warning("[Equipment API] Could not invoke thecodewarrior.equipment.common.lib.PlayerHandler method getPlayerEquipment");
	    }
	    
		return ot;
	}
	
	public static void registerEquipment(String id, EquipmentType type) {
		try
	    {
	        if(registerSlot == null) 
	        {
	            Class<?> fake = Class.forName("thecodewarrior.equipment.common.lib.SlotRegistry");
	            registerSlot = fake.getMethod("registerEquipment", String.class, EquipmentType.class);
	        }
	        
	        registerSlot.invoke(null, id, type);
	    } 
	    catch(Exception ex) 
	    { 
	    	FMLLog.warning("[Equipment API] Could not invoke thecodewarrior.equipment.common.lib.SlotRegistry method registerEquipment");
	    }
	}
	
}
