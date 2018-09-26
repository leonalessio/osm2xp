package com.osm2xp.translators.airfield;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.line.Line2D;

public class RunwayData {
	
	private Line2D runwayLine;
	private double width = 60;
	private String surface;

	public RunwayData(OsmPolyline polyline) {
		runwayLine = GeomUtils.getCenterline(polyline.getPolyline());
		String widthStr = polyline.getTagValue("width");
		if (widthStr != null) {
			try {
				width = Double.parseDouble(widthStr);
			} catch (Exception e) {
				// Ignore
			}
		}
		surface = polyline.getTagValue("surface");
	}

	public Line2D getRunwayLine() {
		return runwayLine;
	}

	public double getWidth() {
		return width;
	}

	public String getSurface() {
		if (surface == null) {
			return "grass";
		}
		return surface;
	}
	

}
