package test.thecodewarrior.equipment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import thecodewarrior.equipment.api.EquipmentApi;
import thecodewarrior.equipment.api.EquipmentType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EquipmentAPITestMod.MODID, version = EquipmentAPITestMod.VERSION)
public class EquipmentAPITestMod
{
    public static final String MODID = "equipmentapitest";
    public static final String VERSION = "0.0.0";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_block", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/block.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemBlock;
			}
			
			@Override
			public int getStackLimit() {
				return 64;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Blocks";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_armor", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/armor.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemArmor;
			}
			
			@Override
			public int getStackLimit() {
				return 1;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Armor";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_tool", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/tool.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemTool;
			}
			
			@Override
			public int getStackLimit() {
				return 1;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Tools";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_dye", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/dye.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemDye;
			}
			
			@Override
			public int getStackLimit() {
				return 16;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Dye";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_food", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/apple.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemFood;
			}
			
			@Override
			public int getStackLimit() {
				return 32;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Food";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_book", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/book.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemBook;
			}
			
			@Override
			public int getStackLimit() {
				return 1;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Book";
			}
		});

		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_bucket", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/bucket.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemBucket;
			}
			
			@Override
			public int getStackLimit() {
				return 1;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Bucket";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_bow", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/bow.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemBow;
			}
			
			@Override
			public int getStackLimit() {
				return 1;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Bow";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_egg", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/egg.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemEgg;
			}
			
			@Override
			public int getStackLimit() {
				return 16;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Eggs!";
			}
		});
		
		EquipmentApi.registerEquipment(EquipmentAPITestMod.MODID + ":type_arrow", new EquipmentType() {
			
			ResourceLocation overlay = new ResourceLocation(EquipmentAPITestMod.MODID, "textures/slot/arrow.png");
			
			@Override
			public ResourceLocation getSlotOverlay(ItemStack stack) {
				return overlay;
			}
			
			@Override
			public boolean canRemoveStack(ItemStack stack, EntityPlayer player) {
				return true;
			}
			
			@Override
			public boolean canPlaceStack(ItemStack stack) {
				return stack != null && stack.getItem() != null && stack.getItem() == Items.arrow;
			}
			
			@Override
			public int getStackLimit() {
				return 64;
			}

			@Override
			public String getSlotDescription(EntityPlayer player) {
				return "Arrows";
			}
		});
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		
    }
}
