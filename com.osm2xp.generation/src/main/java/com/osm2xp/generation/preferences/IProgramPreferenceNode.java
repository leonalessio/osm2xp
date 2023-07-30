package com.osm2xp.generation.preferences;

public interface IProgramPreferenceNode {
	
	public String get(String property, String defaultValue);
	
	public void put(String property, String value);
	
}
