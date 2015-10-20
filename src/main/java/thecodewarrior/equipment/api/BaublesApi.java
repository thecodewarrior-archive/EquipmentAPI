package thecodewarrior.equipment.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.common.FMLLog;

/**
 * @author Azanor
 */
public class BaublesApi 
{
	static Method getBaubles;
	
	/**
	 * Retrieves the thecodewarrior.equipment inventory for the supplied player
	 */
	public static IInventory getBaubles(EntityPlayer player)
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
	
}
