package com.osm2xp.generation.preferences;

import java.util.Map;

public class HashMapPrefsNode implements IProgramPreferenceNode {

	private Map<String, String> map;

	public HashMapPrefsNode(Map<String, String> map) {
		if (map == null) {
			throw new IllegalArgumentException("Argument can't be null");
		}
		this.map = map;
	}

	@Override
	public String get(String property, String defaultValue) {
		String res = map.get(property);
		if (res == null) {
			res = defaultValue;
		}
		return res;
	}

	@Override
	public void put(String property, String value) {
		map.put(property, value);
	}

}
