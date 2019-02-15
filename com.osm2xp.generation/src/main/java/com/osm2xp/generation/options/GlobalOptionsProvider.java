package com.osm2xp.generation.options;

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
