package com.osm2xp.generation.areas;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AreaTypesDef {
	
	protected List<String> allowedAreaTypes;
	protected List<String> disallowedAreaTypes;

	public AreaTypesDef(String definitionString) {
		allowedAreaTypes = new ArrayList<>();
		disallowedAreaTypes = new ArrayList<>();
		String[] splitted = definitionString.split(",");
		for (String type : splitted) {
			type = type.toLowerCase().trim();
			if (!type.isEmpty()) {
				if (type.charAt(0) == '!') {
					disallowedAreaTypes.add(type);
				} else {
					allowedAreaTypes.add(type);
				}
			}
		}
	}

	public List<String> getAllowedAreaTypes() {
		return allowedAreaTypes;
	}

	public List<String> getDisallowedAreaTypes() {
		return disallowedAreaTypes;
	}
	
	public boolean matches(String areaType) {
		areaType = StringUtils.stripToEmpty(areaType).toLowerCase().trim();
		if (allowedAreaTypes.isEmpty() && disallowedAreaTypes.isEmpty()) {
			return true;
		}
		if (areaType.isEmpty()) {
			return allowedAreaTypes.isEmpty();
		}
		for (String disallowedType : allowedAreaTypes) {
			if (disallowedType.equals(areaType)) {
				return false;
			}
		}
		for (String allowedType : allowedAreaTypes) {
			if (allowedType.equals(areaType)) {
				return true;
			}
		}
		
		return false;
	}
}
