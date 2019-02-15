package com.osm2xp.generation.options;

public class XPlaneOptionsProvider {
	
	private static XplaneOptions options;

	public static XplaneOptions getOptions() {
		return options;
	}

	public static void setOptions(XplaneOptions options) {
		XPlaneOptionsProvider.options = options;
	}
	
}
