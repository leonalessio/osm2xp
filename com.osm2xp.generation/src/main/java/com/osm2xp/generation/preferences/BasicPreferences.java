package com.osm2xp.generation.preferences;

import java.io.File;

import com.osm2xp.generation.paths.PathsService;

public class BasicPreferences implements IProgramPreferences {

	private File prefsFolder;

	public BasicPreferences() {
		prefsFolder = new File(PathsService.getPathsProvider().getInstallFolder(),".settings");
		prefsFolder.mkdir();
	}
	
	@Override
	public IProgramPreferenceNode getNode(String nodeName) {
		File file = new File(prefsFolder, nodeName + ".prefs");
		return PropertiesNode.from(nodeName, file);
	}

	@Override
	public void flush(IProgramPreferenceNode node) {
		((PropertiesNode) node).save();
	}

}
