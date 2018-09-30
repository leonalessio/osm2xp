package com.osm2xp.translators.airfield;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.geometry.Geomagnetism;

import math.geom2d.line.Line2D;

public class RunwayData {
	
	private Line2D runwayLine;
	private double width = 60;
	private String surface;
	private double course1, course2;

	public double getCourse1() {
		return course1;
	}

	public double getCourse2() {
		return course2;
	}

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
		double trueCourse = GeomUtils.calcHeadingAngleInDegrees(runwayLine.p1, runwayLine.p2);
		Geomagnetism geomagnetism = new Geomagnetism(runwayLine.p1.x, runwayLine.p1.y);
		double magneticCourse = (trueCourse + geomagnetism.getDeclination()) % 360;
		course1 = magneticCourse;
		course2 = (magneticCourse + 180) % 360;
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
