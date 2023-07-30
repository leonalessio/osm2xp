package com.osm2xp.generation.options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.rules.ObjectTagRule;
import com.osm2xp.generation.options.rules.ObjectsRulesList;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.generation.paths.PathsService;

public class FsxOptionsProvider {
	private static FsxOptions options;
	
	private static final File FSX_OPTIONS_FILE = new File(PathsService.getPathsProvider().getBasicFolder(), "fsx/fsxOptions.xml");

	static {
		if (FSX_OPTIONS_FILE.isFile()) {
			try {
				FsxOptionsProvider.setOptions((FsxOptions) XmlHelper.loadFileFromXml(FSX_OPTIONS_FILE, FsxOptions.class));
			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error("Error initializing FSX options helper", e);
			}
		} 
		if (options == null) {
			options = createNewFsxOptionsBean();
		}
	}
	
	/**
	 * @return
	 */
	private static FsxOptions createNewFsxOptionsBean() {
		FsxOptions result = new FsxOptions(createNewObjectsRules(), null);
		return result;
	}

	/**
	 * @return
	 */
	private static ObjectsRulesList createNewObjectsRules() {
		List<ObjectTagRule> objectsList = new ArrayList<ObjectTagRule>();

		// lighthouses
		ObjectTagRule objectLighthouse = new ObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"lighthouse"), Lists.newArrayList(
						new ObjectFile("{BE7FA036-6133-48CD-B3A3-BDC339E6AB35}"),
						new ObjectFile("{8B63A054-B32E-415D-9884-258008444268}"),
						new ObjectFile("{DE00535C-5E8C-4CC0-932F-E82355A37412}")
						), 0, true);
		objectsList.add(objectLighthouse);

		// silos
		ObjectTagRule objectSilo = new ObjectTagRule(
				new Tag(OsmConstants.MAN_MADE_TAG, "silo"), Lists.newArrayList(
						new ObjectFile("{EF0366E8-AAFE-4DE8-A458-A61A5D7C84BB}"),
						new ObjectFile("{A5B68A08-6F53-43F2-B0DD-C38B9EF79421}"),
						new ObjectFile("{8D99C008-2B93-4A2B-A2D4-C2A75A1B2E41}")
						), 0, true);
		objectsList.add(objectSilo);
		// wind turbines
		ObjectTagRule objectWindTurbine = new ObjectTagRule(new Tag(
				"power_source", "wind"), Lists.newArrayList(
				new ObjectFile("{F21DB452-6D9D-424D-B608-A9C34C194CC1}")), 0, true);
		// water towers
		objectsList.add(objectWindTurbine);
		ObjectTagRule objectWaterTower = new ObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"water_tower"), Lists.newArrayList(
				new ObjectFile("{F2EF2CD2-539C-49C3-812D-128167D6EBB0}"),
				new ObjectFile("{567C15BF-E002-4DE9-A38C-B68C55135A8A}")
		), 0, true);
		// cranes
		objectsList.add(objectWaterTower);
		ObjectTagRule objectCrane = new ObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"crane"), Lists.newArrayList(
				new ObjectFile("{C545A28E-E2EC-11D2-9C84-00105A0CE62A}"),
				new ObjectFile("{B2DFD078-603C-48B4-95FA-45EE25582157}")
		), 0, true);
		objectsList.add(objectCrane);
		// radio towers
		ObjectTagRule objectRadioTower = new ObjectTagRule(new Tag(
				"tower:type", "communication"), Lists.newArrayList(
				new ObjectFile("{79D0AD2B-C7CF-4433-9C7C-25BA72B3327F}"),
				new ObjectFile("{F4CCAF40-00A9-45B9-9412-E4873DE9CA97}"),
				new ObjectFile("{C545A290-E2EC-11D2-9C84-00105A0CE62A}")
		), 0, true);
		objectsList.add(objectRadioTower);

		// nuclear plants
		ObjectTagRule objectNuclearCentral = new ObjectTagRule(new Tag(
				"generator:source", "nuclear"), Lists.newArrayList(
				new ObjectFile("{DDA775E2-5E3E-435E-9A11-F5A8ECF75D37}")
		), 0, true);
		objectsList.add(objectNuclearCentral);

		// power poles
		ObjectTagRule objectPowerPole = new ObjectTagRule(new Tag("power",
				"pole"), Lists.newArrayList(
				new ObjectFile("{54382EC5-B1BD-4A43-AA1A-4F5F529356C3}")
		), 0, true);
		objectsList.add(objectPowerPole);
		// storage tanks
		ObjectTagRule objectStorageTank = new ObjectTagRule(new Tag(OsmConstants.MAN_MADE_TAG,
				"storage_tank"), Lists.newArrayList(
				new ObjectFile("{7549DBA5-8710-4C0F-BF62-324244D86C31}"),
				new ObjectFile("{DA302E05-6454-4F7A-8C6F-C5FC32A77174}")
		), 0, true);
		objectsList.add(objectStorageTank);
		// power stations
		ObjectTagRule objectPowerStations = new ObjectTagRule(new Tag("power",
				"station"), Lists.newArrayList(
				new ObjectFile("{0C0C2054-0ABB-4E86-91A1-B146C3350558}"),
				new ObjectFile("{38C55F68-D0EA-418D-8C3D-CC797B80D410}")
		), 0, true);
		objectsList.add(objectPowerStations);

		// baseball stadium
		ObjectTagRule objectBaseballStadium = new ObjectTagRule(new Tag(
				"sport", "baseball"), Lists.newArrayList(
				new ObjectFile("{0BB07FF0-7FB0-4C88-ACDF-106339FF59D3}")
		), 0, true);
		objectsList.add(objectBaseballStadium);
		// churches
		ObjectTagRule objectChurches = new ObjectTagRule(new Tag(
				"place_of_worship:type", "church"),
				Lists.newArrayList(
						new ObjectFile(
								"{E75256EE-38FC-49A9-9689-A5F81BC1D2C0}")
				), 0, true);
		objectsList.add(objectChurches);

		// churches
		ObjectTagRule objectCathedrals = new ObjectTagRule(new Tag(
				"place_of_worship:type", "cathedral"),
				Lists.newArrayList(
						new ObjectFile(
								"{999B34C1-09CC-4172-A314-4A9371A8A8FF}")
				), 0, true);
		objectsList.add(objectCathedrals);

		// mosque
		ObjectTagRule objectMosques = new ObjectTagRule(new Tag(
				"place_of_worship:type", "mosque"),
				Lists.newArrayList(
						new ObjectFile(
								"{034F8334-FEDE-427E-A025-59B140FADA56}")
				), 0, true);
		objectsList.add(objectMosques);
		ObjectsRulesList result = new ObjectsRulesList(objectsList);
		return result;
	}

	public static void saveOptions() throws Osm2xpBusinessException {
		XmlHelper.saveToXml(getOptions(), FSX_OPTIONS_FILE);
	}

	public static FsxOptions getOptions() {
		return options;
	}

	public static void setOptions(FsxOptions options) {
		FsxOptionsProvider.options = options;
	}
}
