package com.osm2xp.generation.options;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.options.GlobalOptions;

public class GlobalOptionsProvider {

	private static GlobalOptions options;
	private static String sceneName;
	private static Tag shapefileTag;
	private static String outputFormat;

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
	
	/**
	 * Gets the value of the outputFormat property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public static String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * Sets the value of the outputFormat property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public static void setOutputFormat(String value) {
		outputFormat = value;
	}
	
}
