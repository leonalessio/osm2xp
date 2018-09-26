package com.osm2xp.translators.airfield;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * Airport data writer for X-Plane
 * Based on apt.dat 10.50 spec - <a>http://developer.x-plane.com/wp-content/uploads/2017/02/XP-APT1050-Spec.pdf</a>
 * @author Dmitry Karpenko
 *
 */
public class XPAirfieldOutput {
	
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
		File folder = new File(baseFolder, id);
		for (int i = 0; folder.exists() && i < Integer.MAX_VALUE; i++) {
			folder = new File(baseFolder, id + i);
		}
		folder.mkdirs();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(folder, "apt.dat"))))) {
			writer.println(String.format("1 %d 0 0 %s %s",  (int) Math.round(airfieldData.getElevation() * METER_TO_FEET_COEF), airfieldData.getICAO(), airfieldData.getName()));
			for (RunwayData runway : runways) {
				writer.println(getRunwayStr(runway));
			}
		} catch (IOException e) {
			Osm2xpLogger.error("Error saving apt.dat for airfield " + id);
		}
	}

	private String getRunwayStr(RunwayData runway) {
		// TODO Auto-generated method stub
		return null;
	}
	
	int getSurfaceCode(String osmSurfaceType) {
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
