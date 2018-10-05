package com.osm2xp.translators.airfield;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.utils.logging.Osm2xpLogger;

import math.geom2d.Point2D;

/**
 * Airport data writer for X-Plane
 * Based on apt.dat 10.50 spec - <a>http://developer.x-plane.com/wp-content/uploads/2017/02/XP-APT1050-Spec.pdf</a>
 * @author Dmitry Karpenko
 *
 */
public class XPAirfieldOutput {
	
	private static final String NAV_DATA_FOLDER_NAME = "Earth nav data";
	private static final String OSM2XP_AIRFIELD_PREFFIX = "osm2xp_";
	private static final double METER_TO_FEET_COEF = 3.28084;
	private File baseFolder;
	private boolean writeMainAirfield;

	public XPAirfieldOutput(File baseFolder, boolean writeMainAirfield) {
		this.baseFolder = baseFolder;
		this.writeMainAirfield = writeMainAirfield;
		baseFolder.mkdirs();
	}
	
	public List<String> getAptHeaderString() {
		List<String> result = new ArrayList<String>();
		result.add("I");
		result.add("1000 Version - generated by OSM2XP " + Osm2xpConstants.OSM2XP_VERSION);
		result.add("");
		return result;
	}
	
	public void writeAirfield(AirfieldData airfieldData) {
		List<RunwayData> runways = airfieldData.getRunways();
		if (runways.isEmpty()) {
			return; //Do nothing, if no runways assigned for airport
		}
		List<String> defsList = new ArrayList<String>();
		defsList.addAll(getAptHeaderString());
		defsList.add(String.format("1 %d 0 0 %s %s",  (int) Math.round(airfieldData.getElevation() * METER_TO_FEET_COEF), airfieldData.getICAO(), airfieldData.getName()));
		for (RunwayData runway : runways) {
			defsList.add(getRunwayStr(runway));
		}
		defsList.add("99");
		writeAptData(airfieldData.getId(), defsList.toArray(new String[0]));
	}

	public void writeSingleRunway(RunwayData runwayData) {
		List<String> defsList = new ArrayList<String>();
		defsList.addAll(getAptHeaderString());
		defsList.add(String.format("1 %d 0 0 %s %s",  (int) Math.round(runwayData.getElevation() * METER_TO_FEET_COEF), "xxxx", runwayData.getName()));
		defsList.add(getRunwayStr(runwayData));
		defsList.add("99");
		writeAptData(runwayData.getId(), defsList.toArray(new String[0]));
	}
	
	protected void writeAptData(String aptId, String[] aptDefinition) {
		if (aptDefinition.length == 0) {
			return;
		}
		File dataFolder;
		if (writeMainAirfield) {
			File airfieldFolder = new File(baseFolder, OSM2XP_AIRFIELD_PREFFIX + aptId);
			for (int i = 0; airfieldFolder.exists() && i < Integer.MAX_VALUE; i++) {
				airfieldFolder = new File(baseFolder, OSM2XP_AIRFIELD_PREFFIX + aptId + i);
			}
			dataFolder = new File(airfieldFolder, NAV_DATA_FOLDER_NAME);
		} else {
			dataFolder = new File(baseFolder, NAV_DATA_FOLDER_NAME);
		}
		dataFolder.mkdirs();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(dataFolder, "apt.dat"))))) {
			for (String string : aptDefinition) {
				writer.println(string);
			}
			
		} catch (IOException e) {
			Osm2xpLogger.error("Error saving apt.dat for airfield " + aptId);
		}
	}

	private String getRunwayStr(RunwayData runway) {
		StringBuilder builder = new StringBuilder("100 ");
		builder.append(String.format("%1.2f", runway.getWidth()));
		builder.append(' ');
		builder.append(getSurfaceCode(runway));
		builder.append(' ');
		builder.append(getSurfaceShoulderCode(runway));
		builder.append(' ');
		builder.append(getRoughness(runway));
		builder.append(' ');
		builder.append(getCenterLights(runway));
		builder.append(' ');
		builder.append(getEdgeLights(runway));
		builder.append(' ');
		builder.append(getDistSigns(runway));
		builder.append(' ');
		builder.append(getEndStr(runway.getMarking1(), runway.getRunwayLine().p1, runway.isHard()));
		builder.append(' ');
		builder.append(getEndStr(runway.getMarking2(), runway.getRunwayLine().p2, runway.isHard()));
		return builder.toString();
	}
	
	private String getEndStr(String marking, Point2D coords, boolean isHard) {
		StringBuilder builder = new StringBuilder(marking);
		builder.append(' ');
		builder.append(String.format("%1.8f %2.8f 0 0 ", coords.y, coords.x));
		builder.append(' ');
		builder.append(getRunwayMarkingCode(isHard));
		builder.append(' ');
		builder.append(getApproachLightingCode(isHard));
		builder.append(' ');
		builder.append(getTGZLightingCode(isHard));
		builder.append(' ');
		builder.append(getREILCode(isHard));
		return builder.toString();
	}
	
	private int getRunwayMarkingCode(boolean isHard) {
		return isHard ? 1 : 0;
	}
	
	private int getTGZLightingCode(boolean isHard) {
		return isHard ? 1 : 0;
	}
	
	private int getREILCode(boolean isHard) {
		return isHard ? 1 : 0;
	}	
	
	private int getApproachLightingCode(boolean isHard) {
		return isHard ? 1 : 0;
	}

	private int getDistSigns(RunwayData runway) {
		return runway.isHard() ? 1 : 0;
	}

	private int getCenterLights(RunwayData runway) {
		return runway.isHard() ? 1 : 0;
	}
	
	private int getEdgeLights(RunwayData runway) {
		return runway.isHard() ? 2 : 0;
	}

	private String getRoughness(RunwayData runway) {
		if (runway.isHard()) {
			return "0.15";
		}
		return "0.25";
	}

	private int getSurfaceShoulderCode(RunwayData runwayData) {
		if (!runwayData.isHard())
			return 0;
		String osmSurfaceType = runwayData.getSurface();
		if ("asphalt".equals(osmSurfaceType))
			return 1;
		if ("concrete".equals(osmSurfaceType) || "paved".equals(osmSurfaceType))
			return 2;
		return 0;
	}

	private int getSurfaceCode(RunwayData runwayData) {
		String osmSurfaceType = runwayData.getSurface();
		if ("asphalt".equals(osmSurfaceType))
			return 1;
		if ("concrete".equals(osmSurfaceType) || "paved".equals(osmSurfaceType))
			return 2;
		if ("earth".equals(osmSurfaceType) || "dirt".equals(osmSurfaceType) || "mud".equals(osmSurfaceType) || "sand".equals(osmSurfaceType)) {
			return 4;
		}
		if ("gravel".equals(osmSurfaceType) || "fine_gravel".equals(osmSurfaceType)) {
			return 5;
		}
		return 3; //grass by default
	}

}
