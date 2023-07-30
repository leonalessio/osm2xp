package com.osm2xp.generation.options;

public class FlyLegacyOptionsProvider {

	private static FlyLegacyOptions options;

	public static FlyLegacyOptions getOptions() {
		return options;
	}

	public static void setOptions(FlyLegacyOptions options) {
		FlyLegacyOptionsProvider.options = options;
	}
	
}	
