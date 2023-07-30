package com.osm2xp.generation.preferences;

public class PreferenceService {
	
	private static IProgramPreferences programPreferences;

	public static IProgramPreferences getProgramPreferences() {
		return programPreferences;
	}

	public static void setProgramPreferences(IProgramPreferences programPreferences) {
		PreferenceService.programPreferences = programPreferences;
	}
	
}
