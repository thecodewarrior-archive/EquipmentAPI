package thecodewarrior.equipment.common.event;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import thecodewarrior.equipment.api.IEquipment;
import thecodewarrior.equipment.common.Config;
import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.container.InventoryEquipment;
import thecodewarrior.equipment.common.lib.PlayerHandler;

import com.google.common.io.Files;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerEntity {

	// player directory
	private File playerDirectory;
	
	// hash containing game mode of all players
	private Map<String,Boolean> playerModes = new HashMap<String,Boolean>();
	
	@SubscribeEvent
	public void playerTick(PlayerEvent.LivingUpdateEvent event) {

		// player events
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			
			if (Config.isSplitSurvivalCreative()) {
				// detect game mode changes
				if (playerModes.containsKey(player.getCommandSenderName()) && (playerDirectory != null))
				{
					Boolean mode = playerModes.get(player.getCommandSenderName());
					if (mode && !player.capabilities.isCreativeMode)
					{
						playerSaveDo(player, playerDirectory, true);
						playerLoadDo(player, playerDirectory, false);
					}
					else if (!mode && player.capabilities.isCreativeMode)
					{
						playerSaveDo(player, playerDirectory, false);
						playerLoadDo(player, playerDirectory, true);
					}
				}
				playerModes.put(player.getCommandSenderName(), player.capabilities.isCreativeMode);
			}
			
			InventoryEquipment equipment = PlayerHandler.getPlayerEquipmentInventory(player);
			for (String key : equipment.getStackMap().keySet()) {
				ItemStack s = equipment.getStack(key);
				if (s != null && s.getItem() instanceof IEquipment) {
					((IEquipment) s.getItem()).onWornTick(s, player);
				}
			}

		}

	}

	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if (event.entity instanceof EntityPlayer
				&& !event.entity.worldObj.isRemote
				&& !event.entity.worldObj.getGameRules()
						.getGameRuleBooleanValue("keepInventory")) {
			PlayerHandler.getPlayerEquipmentInventory(event.entityPlayer).dropItemsAt(
					event.drops,event.entityPlayer);
		}

	}

	@SubscribeEvent
	public void playerLoad(PlayerEvent.LoadFromFile event) {
		playerLoadDo(event.entityPlayer, event.playerDirectory, event.entityPlayer.capabilities.isCreativeMode);
		playerDirectory = event.playerDirectory;
	}
	
	private void playerLoadDo(EntityPlayer player, File directory, Boolean gamemode) {
		PlayerHandler.clearPlayerEquipment(player);
		
		File file1, file2;
		String fileName, fileNameBackup;
		if (gamemode || !Config.isSplitSurvivalCreative()) {
			fileName = "equip";
			fileNameBackup = "equipback";
		}
		else {
			fileName = "equips";
			fileNameBackup = "equipsback";
		}
		
		// look for normal files first
		file1 = getPlayerFile(fileName, directory, player.getCommandSenderName());
		file2 = getPlayerFile(fileNameBackup, directory, player.getCommandSenderName());
		
		// look for uuid files when normal file missing
		if (!file1.exists()) {
			File filep = getPlayerFileUUID(fileName, directory, player.getGameProfile().getId().toString());
			if (filep.exists()) {
				try {
					Files.copy(filep, file1);					
					EquipmentMod.log.info("Using and converting UUID Equipment savefile for "+player.getCommandSenderName());
					filep.delete();
					File fb = getPlayerFileUUID(fileNameBackup, directory, player.getGameProfile().getId().toString());
					if (fb.exists()) fb.delete();					
				} catch (IOException e) {}
			} else {
				File filet = getLegacyFileFromPlayer(player);
				if (filet.exists()) {
					try {
						Files.copy(filet, file1);
						EquipmentMod.log.info("Using pre MC 1.7.10 Equipment savefile for "+player.getCommandSenderName());
					} catch (IOException e) {}
				}
			}
		}
		
		PlayerHandler.loadPlayerEquipment(player, file1, file2);
		EventHandlerNetwork.syncEquipment(player);
	}
	
	public File getPlayerFile(String suffix, File playerDirectory, String playername)
    {
        if ("dat".equals(suffix)) throw new IllegalArgumentException("The suffix 'dat' is reserved");
        return new File(playerDirectory, playername+"."+suffix);
    }
	
	public File getPlayerFileUUID(String suffix, File playerDirectory, String playerUUID)
    {
        if ("dat".equals(suffix)) throw new IllegalArgumentException("The suffix 'dat' is reserved");
        return new File(playerDirectory, playerUUID+"."+suffix);
    }
	
	public static File getLegacyFileFromPlayer(EntityPlayer player)
    {
		try {
			File playersDirectory = new File(player.worldObj.getSaveHandler().getWorldDirectory(), "players");
			return new File(playersDirectory, player.getCommandSenderName() + ".equip");
		} catch (Exception e) { e.printStackTrace(); }
		return null;
    }

	@SubscribeEvent
	public void playerSave(PlayerEvent.SaveToFile event) {
		playerSaveDo(event.entityPlayer, event.playerDirectory, event.entityPlayer.capabilities.isCreativeMode);
	}
	
	private void playerSaveDo(EntityPlayer player, File directory, Boolean gamemode) {
		if (gamemode || !Config.isSplitSurvivalCreative()) {
			PlayerHandler.savePlayerEquipment(player, 
					getPlayerFile("equip", directory, player.getCommandSenderName()), 
					getPlayerFile("equipback", directory, player.getCommandSenderName()));
		}
		else {
			PlayerHandler.savePlayerEquipment(player, 
					getPlayerFile("equips", directory, player.getCommandSenderName()), 
					getPlayerFile("equipsback", directory, player.getCommandSenderName()));
		}
	}

}
