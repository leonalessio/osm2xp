package com.osm2xp.translators.airfield;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.osm2xp.utils.logging.Osm2xpLogger;

import math.geom2d.Point2D;

/**
 * Airport data writer for X-Plane
 * Based on apt.dat 10.50 spec - <a>http://developer.x-plane.com/wp-content/uploads/2017/02/XP-APT1050-Spec.pdf</a>
 * @author Dmitry Karpenko
 *
 */
public class XPAirfieldOutput {
	
	private static final String OSM2XP_AIRFIELD_PREFFIX = "osm2xp_";
	private static final double METER_TO_FEET_COEF = 3.28084;
	private File baseFolder;

	public XPAirfieldOutput(File baseFolder) {
		this.baseFolder = baseFolder;
		baseFolder.mkdirs();
	}
	
	public void writeAirfield(AirfieldData airfieldData) {
		List<RunwayData> runways = airfieldData.getRunways();
		if (runways.isEmpty()) {
			return; //Don't generate anyhing for airport without runways assigned
		}
		String id = airfieldData.getId();
		File folder = new File(baseFolder, OSM2XP_AIRFIELD_PREFFIX + id);
		for (int i = 0; folder.exists() && i < Integer.MAX_VALUE; i++) {
			folder = new File(baseFolder, OSM2XP_AIRFIELD_PREFFIX + id + i);
		}
		folder.mkdirs();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(folder, "apt.dat"))))) {
			writer.println("I");
			writer.println("1000 Version - data cycle 2012.03, build 20121054, metadata AptXP1000.  Copyright © 2012, Robin A. Peel(robin@xsquawkbox.net)");
			writer.println(String.format("1 %d 0 0 %s %s",  (int) Math.round(airfieldData.getElevation() * METER_TO_FEET_COEF), airfieldData.getICAO(), airfieldData.getName()));
			for (RunwayData runway : runways) {
				writer.println(getRunwayStr(runway));
			}
		} catch (IOException e) {
			Osm2xpLogger.error("Error saving apt.dat for airfield " + id);
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
		builder.append(String.format("%1.8f %2.8f 0.00 0.00 ", coords.y, coords.x));
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
