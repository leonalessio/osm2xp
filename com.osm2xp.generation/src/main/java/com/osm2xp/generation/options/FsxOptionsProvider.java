package com.osm2xp.generation.options;

public class FsxOptionsProvider {
	private static FsxOptions options;

	public static FsxOptions getOptions() {
		return options;
	}

	public static void setOptions(FsxOptions options) {
		FsxOptionsProvider.options = options;
	}
}
