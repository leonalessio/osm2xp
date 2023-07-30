package com.osm2xp.translators.airfield;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.generation.options.XPlaneOptionsProvider;

public class AerowayData {

	protected String id;
	protected String name;
	protected int elevation = 0;
	protected boolean actualElevation = false;

	public AerowayData(IHasTags osmEntity) {
		String elevStr = osmEntity.getTagValue("ele");
		if (!StringUtils.isEmpty(elevStr)) {
			try {
				elevation = Integer.parseInt(elevStr);
				actualElevation = true;
			} catch (Exception e) {
				// Ignore
			}
		}
		name = osmEntity.getTagValue("name");
		String nameEn = osmEntity.getTagValue("name:en"); 
		if (name == null || (XPlaneOptionsProvider.getOptions().getAirfieldOptions().isPreferEnglish() && nameEn != null)) {
			name = nameEn;
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
		return name;
	}
	
	public String getLabel() {
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

	public void setElevation(int elevation) {
		this.elevation = elevation;
		actualElevation = true;
	}

	public boolean hasActualElevation() {
		return actualElevation;
	}

	public void setName(String name) {
		this.name = name;
		if (name != null && (id.indexOf('/') >= 0 || id.indexOf('_') >= 0)) {
			id = name + "_" + id.replace('/','_');
		}
	}

}