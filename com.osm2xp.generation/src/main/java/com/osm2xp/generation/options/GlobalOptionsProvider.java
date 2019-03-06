package com.osm2xp.generation.options;

import java.io.File;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.paths.PathsService;

public class GlobalOptionsProvider {

	private static final String GLOBAL_OPTIONS_XML_FILE = "GlobalOptions.xml";
	private static GlobalOptions options;
	private static String sceneName;
	private static Tag shapefileTag;

	public static GlobalOptions getOptions() {
		if (options == null) {
			File basicFolder = PathsService.getPathsProvider().getBasicFolder();
			File globalOptionsFile = new File(basicFolder, GLOBAL_OPTIONS_XML_FILE);
			if (globalOptionsFile.isFile()) {
				GlobalOptions options = GlobalOptionsProvider.loadOptions(globalOptionsFile);
				if (options != null) {
					GlobalOptionsProvider.setOptions(options);
				}
			}
			if (options == null) {
				Osm2xpLogger.error("Failed to fing global options file. Using default options");
				GlobalOptionsProvider.setOptions(new GlobalOptions()); 
			}
		}
		return options;
	}

	public static void setOptions(GlobalOptions options) {
		GlobalOptionsProvider.options = options;
	}
	
	public static void saveOptions() throws Osm2xpBusinessException {
		XmlHelper.saveToXml(GlobalOptionsProvider.getOptions(), new File(PathsService.getPathsProvider().getBasicFolder(), GLOBAL_OPTIONS_XML_FILE));
	}

	public static GlobalOptions loadOptions(File optionsFile) {
		try {
			return (GlobalOptions) XmlHelper.loadFileFromXml(optionsFile, GlobalOptions.class);
		} catch (com.osm2xp.core.exceptions.Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error initializing Xplane options helper",	e);
		}
		return null;
	}
	
	public static String getSceneName() {
		return sceneName;
	}

	public static void setSceneName(String sceneName) {
		GlobalOptionsProvider.sceneName = sceneName;
	}

	public static Tag getShapefileTag() {
		return shapefileTag;
	}

	public static void setShapefileTag(Tag shapefileTag) {
		GlobalOptionsProvider.shapefileTag = shapefileTag;
	}
	
}
