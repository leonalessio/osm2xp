package com.osm2xp.classification;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;

public class TypeProvider {
	public static BuildingType getBuildingType(List<Tag> tags) {
		if (TagUtil.getValue("shop", tags) != null) {
			return BuildingType.SHOP;
		}
		String building = TagUtil.getValue("building", tags);
		if ("office".equals(building)) {
			return BuildingType.OFFICE;
		}
		if ("hospital".equals(building)) {
			return BuildingType.SOCIAL;
		}
		if ("school".equals(building)) {
			return BuildingType.SOCIAL;
		}
		if ("retail".equals(building) || "supermarket".equals(building)) {
			return BuildingType.SHOP;
		}
		String amenity = TagUtil.getValue("amenity", tags);
		if (amenity != null && amenity.endsWith("school") || "college".equals(amenity)) {
			return BuildingType.SOCIAL;
		}
		if ("university".equals(building) || "university".equals(amenity)) {
			return BuildingType.SOCIAL;
		}
		if ("clinic".equals(amenity) || "hospital".equals(amenity) || 
			"dentist".equals(amenity) || "doctors".equals(amenity)) {
			return BuildingType.SOCIAL;
		}
		if (building != null && building.startsWith("garage")) {
			return BuildingType.GARAGE;
		}
		if ("industrial".equals(building)) {
			return BuildingType.INDUSTRIAL;
		}
		if ("residental".equals(building) || "apartments".equals(building)) {
			return BuildingType.BLOCK;
		}
		if ("house".equals(building) || "detached".equals(building)) {
			return BuildingType.HOUSE;
		}
		if ("commercial".equals(building)) {
			return BuildingType.SHOP;
		}
		return null;
	}
	
	public static boolean isBuilding(List<Tag> tags) {
		return TagUtil.getValue("building", tags) != null;
	}
	
}
