package thecodewarrior.equipment.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.common.FMLLog;

/**
 * @author Azanor
 */
public class EquipmentApi 
{
	static Method getBaubles;
	static Method registerSlot;
	
	/**
	 * Retrieves the equipment inventory for the supplied player
	 */
	public static IInventory getEquipment(EntityPlayer player)
	{
		IInventory ot = null;
		
	    try
	    {
	        if(getBaubles == null) 
	        {
	            Class<?> fake = Class.forName("thecodewarrior.equipment.common.lib.PlayerHandler");
	            getBaubles = fake.getMethod("getPlayerBaubles", EntityPlayer.class);
	        }
	        
	        ot = (IInventory) getBaubles.invoke(null, player);
	    } 
	    catch(Exception ex) 
	    { 
	    	FMLLog.warning("[Baubles API] Could not invoke thecodewarrior.equipment.common.lib.PlayerHandler method getPlayerBaubles");
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
	    	FMLLog.warning("[Baubles API] Could not invoke thecodewarrior.equipment.common.lib.SlotRegistry method registerEquipment");
	    }
	}
	
}
