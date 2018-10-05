package com.osm2xp.translators.airfield;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolyline;

public class AerowayData {

	protected String id;
	protected String name;
	protected int elevation = 0;

	public AerowayData(OsmPolyline osmPolyline) {
		String elevStr = osmPolyline.getTagValue("ele");
		if (!StringUtils.isEmpty(elevStr)) {
			try {
				elevation = Integer.parseInt(elevStr); 
			} catch (Exception e) {
				// Ignore
			}
		}
		name = osmPolyline.getTagValue("name:en");
		if (name == null) {
			name = osmPolyline.getTagValue("name");
		}
	}

	protected String toId(String tagValue) {
		if (StringUtils.isEmpty(tagValue)) {
			return null;
		}
		tagValue = tagValue.replace(' ', '_');
		tagValue = tagValue.replace('/', '_');
		tagValue = tagValue.replace('\\', '_');
		StringBuilder builder = new StringBuilder(tagValue.length());
		for (int i = 0; i < tagValue.length(); i++) {
			char c = tagValue.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	public String getName() {
		if (StringUtils.isEmpty(name)) {
			return id;
		}
		return name;
	}

	public String getId() {
		return id;
	}

	public int getElevation() {
		return elevation;
	}

}