package thecodewarrior.equipment.common.network;

import thecodewarrior.equipment.common.EquipmentMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(EquipmentMod.MODID.toLowerCase());

    public static void init()
    {
        INSTANCE.registerMessage(PacketOpenBaublesInventory.class, PacketOpenBaublesInventory.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncBauble.class, PacketSyncBauble.class, 2, Side.CLIENT);
        
        INSTANCE.registerMessage(PacketNextPage.class, PacketNextPage.class, 3, Side.SERVER);
        INSTANCE.registerMessage(PacketPrevPage.class, PacketPrevPage.class, 4, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncDisplayedSlots.class, PacketSyncDisplayedSlots.class, 5, Side.CLIENT);
    }
    
    
}
