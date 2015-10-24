package thecodewarrior.equipment.common;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import thecodewarrior.equipment.common.event.EventHandlerEntity;
import thecodewarrior.equipment.common.event.EventHandlerNetwork;
import thecodewarrior.equipment.common.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = EquipmentMod.MODID, name = EquipmentMod.MODNAME, version = EquipmentMod.VERSION, dependencies="required-after:Forge@[10.13.2,);")

public class EquipmentMod {
	
	public static final String MODID = "EquipmentApi";
	public static final String MODNAME = "EquipmentApi";
	public static final String VERSION = "0.1.0";

	@SidedProxy(clientSide = "thecodewarrior.equipment.client.ClientProxy", serverSide = "thecodewarrior.equipment.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(value=EquipmentMod.MODID)
	public static EquipmentMod instance;
	
	public EventHandlerEntity entityEventHandler;
	public EventHandlerNetwork entityEventNetwork;
	public File modDir;
	
	public static final Logger log = LogManager.getLogger("Equipment");
	public static final int GUI = 0;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		event.getModMetadata().version = EquipmentMod.VERSION;
		modDir = event.getModConfigurationDirectory();		
		
		try {
			Config.initialize(event.getSuggestedConfigurationFile());
		} catch (Exception e) {
			EquipmentMod.log.error("EquipmentAPI has a problem loading it's configuration");
		} finally {
			if (Config.config!=null) Config.save();
		}
		
		PacketHandler.init();
		
		entityEventHandler = new EventHandlerEntity();
		entityEventNetwork = new EventHandlerNetwork();
		
		MinecraftForge.EVENT_BUS.register(entityEventHandler);
		FMLCommonHandler.instance().bus().register(entityEventNetwork);
		proxy.registerHandlers();
		
		/////////////////////
			
		Config.save();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
  		proxy.registerKeyBindings();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		Config.initRecipe();
		
	}
		
}
