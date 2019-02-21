package com.osm2xp.generation.options;

import java.io.File;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.paths.PathsService;

public class XPlaneOptionsProvider {
	
	private static XplaneOptions options;

	public static XplaneOptions getOptions() {
		return options;
	}

	public static void setOptions(XplaneOptions options) {
		XPlaneOptionsProvider.options = options;
	}
	
	public static void loadDefaultOptions() {
		options = loadOptions(getDefaultOptionsFile());
	}
	
	public static XplaneOptions loadOptions(File optionsFile) {
		try {
			return (XplaneOptions) XmlHelper.loadFileFromXml(optionsFile, XplaneOptions.class);
		} catch (com.osm2xp.core.exceptions.Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error initializing Xplane options helper",	e);
		}
		return options;
	}
	
	public static File getDefaultOptionsFile() {
		File installFolder = PathsService.getPathsProvider().getBasicFolder();
		File xPlaneFolder = new File(installFolder.getAbsolutePath(), "xplane");
		File defaultConfig = new File(xPlaneFolder, "xplaneOptions.xml");
		return defaultConfig;
	}
	
	public static String getDefaultFacadeSets() {
		File installFolder = PathsService.getPathsProvider().getBasicFolder();
		File xPlaneFolder = new File(installFolder.getAbsolutePath(), "xplane");
		File facadesFolder = new File(xPlaneFolder,"facades");
		return facadesFolder.getAbsolutePath();
	}
	
}
