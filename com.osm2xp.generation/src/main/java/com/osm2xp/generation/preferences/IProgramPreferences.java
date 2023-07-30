package com.osm2xp.generation.preferences;

public interface IProgramPreferences {
	public IProgramPreferenceNode getNode(String nodeName);
	public void flush(IProgramPreferenceNode node);
}
