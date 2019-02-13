package com.osm2xp.generation.options;

import com.osm2xp.model.options.GlobalOptions;

public class GlobalOptionsProvider {

	private static GlobalOptions options;
	private static String sceneName;

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
	
}
