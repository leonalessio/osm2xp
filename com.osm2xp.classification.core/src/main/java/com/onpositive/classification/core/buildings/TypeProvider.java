package com.onpositive.classification.core.buildings;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.onpositive.classification.core.util.TagUtil;
import com.osm2xp.core.model.osm.Tag;

public class TypeProvider {
	public static OSMBuildingType getBuildingType(List<Tag> tags) {
		String building = TagUtil.getValue("building", tags);
		String shop = TagUtil.getValue("shop", tags);
		if ("department_store".equals(shop) || "mall".equals(shop)) {
			return OSMBuildingType.MALL;
		}
		if ("hospital".equals(building)) {
			return OSMBuildingType.SOCIAL;
		}
		if ("school".equals(building)) {
			return OSMBuildingType.SOCIAL;
		}
		if ("retail".equals(building) || "supermarket".equals(building)) {
			return OSMBuildingType.SHOP;
		}
		String amenity = TagUtil.getValue("amenity", tags);
		if (amenity != null && amenity.endsWith("school") || "college".equals(amenity)) {
			return OSMBuildingType.SOCIAL;
		}
		if ("university".equals(building) || "university".equals(amenity)) {
			return OSMBuildingType.SOCIAL;
		}
		if ("clinic".equals(amenity) || "hospital".equals(amenity) || 
			"dentist".equals(amenity) || "doctors".equals(amenity)) {
			return OSMBuildingType.SOCIAL;
		}
		if (building != null && building.startsWith("garage")) {
			return OSMBuildingType.GARAGE;
		}
		if ("industrial".equals(building)) {
			return OSMBuildingType.INDUSTRIAL;
		}
		if ("residental".equals(building) || "apartments".equals(building)) {
			return OSMBuildingType.BLOCK;
		}
		if ("house".equals(building) || "detached".equals(building)) {
			return OSMBuildingType.HOUSE;
		}
		if ("commercial".equals(building) || !StringUtils.isEmpty(shop)) {
			return OSMBuildingType.SHOP;
		}
		return null;
	}
	
	public static boolean isBuilding(List<Tag> tags) {
		return TagUtil.getValue("building", tags) != null;
	}
	
}
