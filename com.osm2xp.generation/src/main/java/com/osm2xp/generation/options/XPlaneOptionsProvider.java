package com.osm2xp.generation.options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.rules.FacadesRulesList;
import com.osm2xp.generation.options.rules.ForestTagRule;
import com.osm2xp.generation.options.rules.ForestsRulesList;
import com.osm2xp.generation.options.rules.PolygonRulesList;
import com.osm2xp.generation.options.rules.PolygonTagsRule;
import com.osm2xp.generation.options.rules.XplaneLightTagRule;
import com.osm2xp.generation.options.rules.XplaneObjectTagRule;
import com.osm2xp.generation.options.rules.XplaneObjectsRulesList;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.generation.paths.PathsService;

public class XPlaneOptionsProvider {
	
	private static XplaneOptions options;

	public static XplaneOptions getOptions() {
		if (options == null) {
			loadDefaultOptions();
		}
		if (options == null) {
			Osm2xpLogger.warning("Will use default X-Plane options.");
			options = createNewXplaneOptionsBean();
		}
		return options;
	}

	public static void setOptions(XplaneOptions options) {
		XPlaneOptionsProvider.options = options;
	}
	
	private static void loadDefaultOptions() {
		options = loadOptions(getDefaultOptionsFile());
	}
	
	private static XplaneOptions loadOptions(File optionsFile) {
		try {
			return (XplaneOptions) XmlHelper.loadFileFromXml(optionsFile, XplaneOptions.class);
		} catch (com.osm2xp.core.exceptions.Osm2xpBusinessException e) {
			Osm2xpLogger.warning("Unable to load X-Plane options file at " + optionsFile.getAbsolutePath()); 
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
		result.setGeneratePolys(true);
		result.setGenerateObj(true);
		result.setPackageFacades(true);
		result.setBuildingsExclusions(createNewXplaneExclusions());
		result.setForestsRules(createNewForestRules());
		result.setObjectsRules(createNewObjectsRules());
		result.setPolygonRules(createNewPolygonRules());
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
	 * @return Default 3D Object rules list
	 */
	private static XplaneObjectsRulesList createNewObjectsRules() {
		List<XplaneObjectTagRule> XplaneObjectTagRules = new ArrayList<XplaneObjectTagRule>();
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag("power_source", "wind"),
				Lists.newArrayList(new ObjectFile("objects/wind_turbine.obj")), 0, true, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG, "lighthouse"),
				Lists.newArrayList(new ObjectFile("objects/capemay.obj")), 0, true, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG, "water_tower"),
				Lists.newArrayList(new ObjectFile("objects/watertower-3.obj")), 0, true, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG, "tower"),
				Lists.newArrayList(new ObjectFile("objects/watertower-3.obj")), 0, true, false));
		XplaneObjectTagRules.add(new XplaneObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG, "crane"),
				Lists.newArrayList(new ObjectFile("objects/crane.obj")), 0, true, false));
		XplaneObjectsRulesList result = new XplaneObjectsRulesList(XplaneObjectTagRules);
		return result;
	}

	private static PolygonRulesList createNewPolygonRules() {
		List<PolygonTagsRule> list = new ArrayList<>();
		list.add(new PolygonTagsRule(new Tag("amenity", "parking"), Lists.newArrayList(
				new Polygon("lib/airport/pavement/asphalt_1L.pol"),
				new Polygon("lib/airport/pavement/asphalt_1D.pol"),
				new Polygon("lib/airport/pavement/asphalt_2L.pol"),
				new Polygon("lib/airport/pavement/asphalt_2D.pol"),
				new Polygon("lib/airport/pavement/asphalt_3L.pol"),
				new Polygon("lib/airport/pavement/asphalt_3D.pol"),
				new Polygon("lib/airport/pavement/concrete_3L.pol"),
				new Polygon("lib/airport/pavement/concrete_3D.pol"),
				new Polygon("lib/airport/pavement/concrete_4L.pol"),
				new Polygon("lib/airport/pavement/concrete_4D.pol")
		)));
		return new PolygonRulesList(list);
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
	private static ForestsRulesList createNewForestRules() {
		List<ForestTagRule> forestsRules = new ArrayList<ForestTagRule>();
		forestsRules.add(
				new ForestTagRule(new Tag(OsmConstants.LANDUSE_TAG, "forest"), Lists.newArrayList(new ObjectFile("forests/mixed.for"),
						new ObjectFile("forests/conifer.for"), new ObjectFile("forests/broad_leaf.for")), 255));
		forestsRules.add(new ForestTagRule(new Tag("natural", "wood"),
				Lists.newArrayList(new ObjectFile("forests/mixed.for"),
						new ObjectFile("forests/conifer.for"), new ObjectFile("forests/broad_leaf.for")), 255));
		forestsRules.add(new ForestTagRule(new Tag("leisure", "garden"),
				Lists.newArrayList(new ObjectFile("forests/heathland.for"),
						new ObjectFile("forests/sclerophyllous.for"), new ObjectFile("forests/conifer.for")), 255));
		forestsRules.add(new ForestTagRule(new Tag("leisure", "park"),
				Lists.newArrayList(new ObjectFile("forests/heathland.for"),
						new ObjectFile("forests/sclerophyllous.for"), new ObjectFile("forests/conifer.for")), 255));
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
