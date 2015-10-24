package thecodewarrior.equipment.common.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.container.InventoryEquipment;

import com.google.common.io.Files;

public class PlayerHandler {

	private static HashMap<String, InventoryEquipment> playerEquipment = new HashMap<String, InventoryEquipment>();

	public static void clearPlayerEquipment(EntityPlayer player) {
		playerEquipment.remove(player.getCommandSenderName());
	}

	public static InventoryEquipment getPlayerEquipmentInventory(EntityPlayer player) {
		if (!playerEquipment.containsKey(player.getCommandSenderName())) {
			InventoryEquipment inventory = new InventoryEquipment(player);
			playerEquipment.put(player.getCommandSenderName(), inventory);
		}
		return playerEquipment.get(player.getCommandSenderName());
	}
	public static ItemStack getPlayerEquipment(EntityPlayer player, String id) {
		if (!playerEquipment.containsKey(player.getCommandSenderName())) {
			InventoryEquipment inventory = new InventoryEquipment(player);
			playerEquipment.put(player.getCommandSenderName(), inventory);
		}
		return playerEquipment.get(player.getCommandSenderName()).getStack(id);
	}

	public static void setPlayerEquipment(EntityPlayer player,
			InventoryEquipment inventory) {
		playerEquipment.put(player.getCommandSenderName(), inventory);
	}

	public static void loadPlayerEquipment(EntityPlayer player, File file1, File file2) {
		if (player != null && !player.worldObj.isRemote) {
			try {
				NBTTagCompound data = null;
				boolean save = false;
				if (file1 != null && file1.exists()) {
					try {
						FileInputStream fileinputstream = new FileInputStream(
								file1);
						data = CompressedStreamTools
								.readCompressed(fileinputstream);
						fileinputstream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (file1 == null || !file1.exists() || data == null
						|| data.hasNoTags()) {
					EquipmentMod.log.warn("Data not found for "
							+ player.getCommandSenderName()
							+ ". Trying to load backup data.");
					if (file2 != null && file2.exists()) {
						try {
							FileInputStream fileinputstream = new FileInputStream(
									file2);
							data = CompressedStreamTools
									.readCompressed(fileinputstream);
							fileinputstream.close();
							save = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				if (data != null) {
					InventoryEquipment inventory = new InventoryEquipment(player);
					inventory.readNBT(data);
					playerEquipment.put(player.getCommandSenderName(), inventory);
					if (save)
						savePlayerEquipment(player, file1, file2);
				}
			} catch (Exception exception1) {
				EquipmentMod.log.fatal("Error loading equipment inventory");
				exception1.printStackTrace();
			}
		}
	}

	public static void savePlayerEquipment(EntityPlayer player, File file1, File file2) {
		if (player != null && !player.worldObj.isRemote) {
			try {
				if (file1 != null && file1.exists()) {
					try {
						Files.copy(file1, file2);
					} catch (Exception e) {
						EquipmentMod.log
								.error("Could not backup old equipment file for player "
										+ player.getCommandSenderName());
					}
				}

				try {
					if (file1 != null) {
						InventoryEquipment inventory = getPlayerEquipmentInventory(player);
						NBTTagCompound data = new NBTTagCompound();
						inventory.saveNBT(data);

						FileOutputStream fileoutputstream = new FileOutputStream(
								file1);
						CompressedStreamTools.writeCompressed(data,
								fileoutputstream);
						fileoutputstream.close();

					}
				} catch (Exception e) {
					EquipmentMod.log.error("Could not save equipment file for player "
							+ player.getCommandSenderName());
					e.printStackTrace();
					if (file1.exists()) {
						try {
							file1.delete();
						} catch (Exception e2) {
						}
					}
				}
			} catch (Exception exception1) {
				EquipmentMod.log.fatal("Error saving equipment inventory");
				exception1.printStackTrace();
			}
		}
	}
}
