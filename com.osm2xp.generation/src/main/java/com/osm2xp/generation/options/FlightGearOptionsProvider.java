package com.osm2xp.generation.options;

public class FlightGearOptionsProvider {
	private static FlightGearOptions options;

	public static FlightGearOptions getOptions() {
		return options;
	}

	public static void setOptions(FlightGearOptions options) {
		FlightGearOptionsProvider.options = options;
	}
}
