package com.osm2xp.application.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;

import com.osm2xp.generation.preferences.IProgramPreferenceNode;
import com.osm2xp.generation.preferences.IProgramPreferences;

public class EclipsePreferences implements IProgramPreferences {

	@Override
	public IProgramPreferenceNode getNode(String nodeName) {
		return new EclipsePrefNode(InstanceScope.INSTANCE.getNode(nodeName));
	}

	@Override
	public void flush(IProgramPreferenceNode node) {
		((EclipsePrefNode) node).flush();
	}

}
