package com.osm2xp.utils.helpers;

import java.io.File;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.BuildingsExclusionsList;
import com.osm2xp.generation.options.ObjectsList;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.generation.options.rules.FacadesRulesList;
import com.osm2xp.generation.options.rules.ForestsRulesList;
import com.osm2xp.generation.options.rules.ObjectsRulesList;
import com.osm2xp.generation.options.rules.PolygonRulesList;
import com.osm2xp.generation.options.rules.XplaneObjectsRulesList;

/**
 * XplaneOptionsHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneOptionsHelper extends OptionsHelper {

	/**
	 * @param file
	 */
	public static void importExclusions(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					BuildingsExclusionsList.class);
			XPlaneOptionsProvider.getOptions().setBuildingsExclusions(
					(BuildingsExclusionsList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing exclusion file", e);
		}

	}

	/**
	 * @param file
	 */
	public static void importFacadesRules(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					FacadesRulesList.class);
			XPlaneOptionsProvider.getOptions().setFacadesRules((FacadesRulesList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing facades rules file", e);
		}

	}

	/**
	 * @param file
	 */
	public static void importForestsRules(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					ForestsRulesList.class);
			XPlaneOptionsProvider.getOptions().setForestsRules((ForestsRulesList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing forests rules file", e);
		}

	}

	/**
	 * @param file
	 */
	public static void importObjectsRules(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					ObjectsRulesList.class);
			XPlaneOptionsProvider.getOptions().setObjectsRules((XplaneObjectsRulesList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing objects rules file", e);
		}

	}
	
	/**
	 * @param file
	 */
	public static void importPolyRules(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file,
					ObjectsRulesList.class);
			XPlaneOptionsProvider.getOptions().setPolygonRules((PolygonRulesList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing objects rules file", e);
		}
		
	}

	public static void importStreetLightObjects(File file) {
		try {
			Object result = XmlHelper.loadFileFromXml(file, ObjectsList.class);
			XPlaneOptionsProvider.getOptions().setStreetLightObjects((ObjectsList) result);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error importing street lights file", e);
		}

	}

}
