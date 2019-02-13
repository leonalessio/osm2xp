package com.osm2xp.generation.options;

import com.osm2xp.model.options.XplaneOptions;

public class XPlaneOptionsProvider {
	
	private static XplaneOptions options;

	public static XplaneOptions getOptions() {
		return options;
	}

	public static void setOptions(XplaneOptions options) {
		XPlaneOptionsProvider.options = options;
	}
	
}
