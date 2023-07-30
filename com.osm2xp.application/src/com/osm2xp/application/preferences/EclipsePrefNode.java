package com.osm2xp.application.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.preferences.IProgramPreferenceNode;

public class EclipsePrefNode implements IProgramPreferenceNode {
	
	private IEclipsePreferences node;

	public EclipsePrefNode(IEclipsePreferences node) {
		this.node = node;
	}

	@Override
	public String get(String property, String defaultValue) {
		return node.get(property, defaultValue);
	}

	@Override
	public void put(String property, String value) {
		node.put(property,value);
	}

	public void flush() {
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Osm2xpLogger.error(e);
		}
	}

}
