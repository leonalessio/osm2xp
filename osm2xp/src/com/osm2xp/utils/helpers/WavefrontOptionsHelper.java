package com.osm2xp.utils.helpers;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.model.options.WavefrontOptions;

/**
 * WavefrontOptionsHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class WavefrontOptionsHelper {

	private static WavefrontOptions options;
	private static final String WAVEFRONT_OPTIONS_FILE_PATH = ResourcesPlugin
			.getWorkspace().getRoot().getLocation()
			+ File.separator + "wavefrontOptions.xml";

	static {
		if (new File(WAVEFRONT_OPTIONS_FILE_PATH).exists()) {
			try {
				options = (WavefrontOptions) XmlHelper.loadFileFromXml(
						new File(WAVEFRONT_OPTIONS_FILE_PATH),
						WavefrontOptions.class);
			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error(
						"Error initializing Wavefront options helper", e);
			}
		} else {
			options = createNewWavefrontOptionsBean();
		}
	}

	private static WavefrontOptions createNewWavefrontOptionsBean() {
		WavefrontOptions result = new WavefrontOptions(40, 40, false, true);
		return result;
	}

	public static WavefrontOptions getOptions() {
		return options;
	}
}
