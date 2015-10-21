package thecodewarrior.equipment.common.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import thecodewarrior.equipment.api.EquipmentType;

public class SlotRegistry {

	static SortedMap<String, EquipmentType> equipment = new TreeMap<String, EquipmentType>();
	static List<String> sortedIDs;
	
	public static void registerEquipment(String id, EquipmentType type) {
		if(id == null || id.length() == 0)
			throw new IllegalArgumentException("ID cannot be null or empty");
		if(equipment.containsKey(id))
			throw new IllegalArgumentException("Equipment type with id '" + id + "' already exists");
		equipment.put(id, type);
		updateEquipmentList();
	}
	
	static void updateEquipmentList() {
		sortedIDs = new ArrayList<String>();
		for (String id : equipment.keySet()) {
			sortedIDs.add(id);
		}
	}
	
	public static EquipmentType getEquipment(String id) {
		return id == null ? null : equipment.get(id);
	}
	
	public static EquipmentType getEquipmentAtIndex(int index) {
		if(index >= sortedIDs.size())
			return null;
		return equipment.get(sortedIDs.get(index));
	}
	
	public static String getEquipmentIdAtIndex(int index) {
		if(index >= sortedIDs.size())
			return null;
		return sortedIDs.get(index);
	}
}
