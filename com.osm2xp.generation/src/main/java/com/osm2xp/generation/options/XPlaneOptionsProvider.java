package com.osm2xp.generation.options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.generation.paths.PathsService;

public class XPlaneOptionsProvider {
	
	private static XplaneOptions options;

	public static XplaneOptions getOptions() {
		if (options == null) {
			loadDefaultOptions();
		}
		if (options == null) {
			Osm2xpLogger.warning("Unable to find X-Plane options file at " + getDefaultOptionsFile().getAbsolutePath() + ". Will use default options.");
			options = createNewXplaneOptionsBean();
		}
		return options;
	}

	public static void setOptions(XplaneOptions options) {
		XPlaneOptionsProvider.options = options;
	}
	
	public static void loadDefaultOptions() {
		options = loadOptions(getDefaultOptionsFile());
	}
	
	public static XplaneOptions loadOptions(File optionsFile) {
		try {
			return (XplaneOptions) XmlHelper.loadFileFromXml(optionsFile, XplaneOptions.class);
		} catch (com.osm2xp.core.exceptions.Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error initializing Xplane options helper",	e);
		}
		return null;
	}
	
	public static void saveOptions() throws Osm2xpBusinessException {
		XmlHelper.saveToXml(getOptions(), getDefaultOptionsFile());
	}
	
	public static File getDefaultOptionsFile() {
		File installFolder = PathsService.getPathsProvider().getBasicFolder();
		File xPlaneFolder = new File(installFolder.getAbsolutePath(), "xplane");
		File defaultConfig = new File(xPlaneFolder, "xplaneOptions.xml");
		return defaultConfig;
	}
	
	public static String getDefaultFacadeSets() {
		File installFolder = PathsService.getPathsProvider().getBasicFolder();
		File xPlaneFolder = new File(installFolder.getAbsolutePath(), "xplane");
		File facadesFolder = new File(xPlaneFolder,"facades");
		return facadesFolder.getAbsolutePath();
	}
	
	/**
	 * @return
	 */
	private static XplaneOptions createNewXplaneOptionsBean() {
		XplaneOptions result = new XplaneOptions();
		result.setBuildingMax(30);
		result.setBuildingMin(6);
		result.setResidentialMax(15);
		result.setResidentialMin(3);
		result.setExcludeFor(true);
		result.setExcludeObj(true);
		result.setFacadeLod(25000);
		result.setGenerateBuildings(true);
		result.setGenerateFor(true);
		result.setGenerateFence(true);
		result.setGeneratePowerlines(true);
		result.setGenerateRailways(true);
		result.setGenerateRoads(true);
		result.setGenerateTanks(true);
		result.setGenerateBridges(true);
		result.setGeneratePdfStats(true);
		result.setGenerateDebugImg(false);
		result.setGenerateSlopedRoofs(true);
		result.setGenerateObj(true);
		result.setPackageFacades(true);
		result.setBuildingsExclusions(createNewXplaneExclusions());
		result.setForestsRules(createNewForestRules());
		result.setObjectsRules(createNewObjectsRules());
		result.setLightsRules(createNewLightsRules());
		result.setStreetLightObjects(createNewStreetLightsObjects());
		result.setFacadesRules(new FacadesRulesList());
		result.setMinHouseSegment(2);
		result.setMinHouseArea(20);
		result.setMaxHouseSegment(200);
		result.setSmartExclusionDistance(25);
		result.setSmartExclusionSize(100);
		return result;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("serial")
	private static ObjectsList createNewStreetLightsObjects() {
		List<ObjectFile> objectFiles = new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"opensceneryx/objects/furniture/lights/street/3.obj"));
				add(new ObjectFile(
						"opensceneryx/objects/furniture/lights/street/2.obj"));
			}
		};
		ObjectsList result = new ObjectsList(objectFiles);
		return result;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("serial")
	private static XplaneObjectsRulesList createNewObjectsRules() {
		List<XplaneObjectTagRule> XplaneObjectTagRules = new ArrayList<XplaneObjectTagRule>();
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(
				"power_source", "wind"), new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"objects/wind_turbine.obj"));
			}
		}, 0, true, false, false, 0, 0, 0, 0, false, 0, 0, false, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"lighthouse"), new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"objects/capemay.obj"));
			}
		}, 0, true, false, false, 0, 0, 0, 0, false, 0, 0, false, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"water_tower"), new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"objects/watertower-3.obj"));
				add(new ObjectFile(
						"objects/watertower-3.obj"));
			}
		}, 0, true, false, false, 0, 0, 0, 0, false, 0, 0, false, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"tower"), new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"objects/watertower-3.obj"));
				add(new ObjectFile(
						"objects/watertower-3.obj"));
			}
		}, 0, true, false, false, 0, 0, 0, 0, false, 0, 0, false, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"crane"), new ArrayList<ObjectFile>() {
			{
				add(new ObjectFile(
						"objects/crane.obj"));
			}
		}, 0, true, false, false, 0, 0, 0, 0, false, 0, 0, false, false));
		XplaneObjectsRulesList result = new XplaneObjectsRulesList(
				XplaneObjectTagRules);
		return result;
	}

	/**
	 * @return
	 */
	private static BuildingsExclusionsList createNewXplaneExclusions() {
		List<Tag> exclusionsList = new ArrayList<Tag>();
		exclusionsList.add(new Tag("aeroway", "hangar"));
		exclusionsList.add(new Tag("aeroway", "terminal"));
		exclusionsList.add(new Tag(OsmConstants.MAN_MADE_TAG, "chimney")); //Such a stuff should be handled with objects instead of generating building facades 
		exclusionsList.add(new Tag(OsmConstants.MAN_MADE_TAG, "tower"));
		exclusionsList.add(new Tag(OsmConstants.MAN_MADE_TAG, "cooling_tower"));
		BuildingsExclusionsList result = new BuildingsExclusionsList(
				exclusionsList);
		return result;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("serial")
	private static ForestsRulesList createNewForestRules() {
		List<ForestTagRule> forestsRules = new ArrayList<ForestTagRule>();
		forestsRules.add(new ForestTagRule(new Tag("landuse", "forest"),
				new ArrayList<ObjectFile>() {
					{
						add(new ObjectFile(
								"forests/mixed.for"));
						add(new ObjectFile(
								"forests/conifer.for"));
						add(new ObjectFile(
								"forests/broad_leaf.for"));
					}
				}, 255));
		forestsRules.add(new ForestTagRule(new Tag("natural", "wood"),
				new ArrayList<ObjectFile>() {
					{
						add(new ObjectFile(
								"forests/mixed.for"));
						add(new ObjectFile(
								"forests/conifer.for"));
						add(new ObjectFile(
								"forests/broad_leaf.for"));
					}
				}, 255));
		forestsRules.add(new ForestTagRule(new Tag("leisure", "garden"),
				new ArrayList<ObjectFile>() {
					{
						add(new ObjectFile(
								"forests/heathland.for"));
						add(new ObjectFile(
								"forests/sclerophyllous.for"));
						add(new ObjectFile(
								"forests/conifer.for"));
					}
				}, 255));
		forestsRules.add(new ForestTagRule(new Tag("leisure", "park"),
				new ArrayList<ObjectFile>() {
					{
						add(new ObjectFile(
								"forests/heathland.for"));
						add(new ObjectFile(
								"forests/sclerophyllous.for"));
						add(new ObjectFile(
								"forests/conifer.for"));
					}
				}, 255));
		ForestsRulesList result = new ForestsRulesList(forestsRules);
		return result;

	}

	/**
	 * @return
	 */
	private static LightsRulesList createNewLightsRules() {
		List<XplaneLightTagRule> lightsRules = new ArrayList<XplaneLightTagRule>();
		LightsRulesList result = new LightsRulesList(lightsRules);
		return result;

	}
	
}
