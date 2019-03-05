package com.osm2xp.generation.options;

import java.io.File;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;

public class FlightGearOptionsProvider {
	private static FlightGearOptions options;

	public static FlightGearOptions getOptions() {
		return options;
	}

	public static void setOptions(FlightGearOptions options) {
		FlightGearOptionsProvider.options = options;
	}

	public static void importObjectsRules(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					ObjectsRulesList.class);
			getOptions().setObjectsRules((FlightGearObjectsRulesList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing objects rules file", e);
		}
		
	}
}
