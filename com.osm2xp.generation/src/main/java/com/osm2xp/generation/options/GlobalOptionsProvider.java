package com.osm2xp.generation.options;

import java.io.File;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;

public class GlobalOptionsProvider {

	private static GlobalOptions options;
	private static String sceneName;
	private static Tag shapefileTag;

	public static GlobalOptions getOptions() {
		return options;
	}

	public static void setOptions(GlobalOptions options) {
		GlobalOptionsProvider.options = options;
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
