package com.osm2xp.translators.airfield;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.geometry.Geomagnetism;

import math.geom2d.line.Line2D;

public class RunwayData {
	
	private Line2D runwayLine;
	private double width = 60;
	private String surface;
	private boolean hard = false;
	private double course1, course2;
	private String marking1, marking2;

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
		hard = ("asphalt".equals(surface) || "concrete".equals(surface) || "paved".equals(surface));
		String ref = polyline.getTagValue("ref");
		if (!StringUtils.isEmpty(ref) && ref.indexOf('/') > 0) {
			int idx = ref.indexOf('/');
			String startMarking = ref.substring(0, idx);
			int markingHeading = getMarkingHeading(startMarking);
			if (markingHeading >= 0) {
				double dist1 = Math.abs(course1 / 10 - markingHeading);
				double dist2 = Math.abs(course2 / 10- markingHeading);
				if (dist1 <= dist2) {
					marking1 = startMarking;
					marking2 = StringUtils.defaultIfBlank(ref.substring(idx+1), null);
				} else {
					marking1 = StringUtils.defaultIfBlank(ref.substring(idx+1), null);
					marking2 = startMarking;
				}
			}
		}
	}
	
	public int getMarkingHeading(String ref) {
		int idx = 0;
		while(idx < ref.length() && Character.isDigit(ref.charAt(idx))) {
			idx++;
		}
		if (idx > 0) {
			return Integer.parseInt(ref.substring(0, idx));
		}
		return -1;
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

	public boolean isHard() {
		return hard;
	}

	public String getMarking1() {
		if (marking1 == null) {
			marking1 = StringUtils.leftPad("" + (int)Math.round(course1 / 10), 2, '0');
		}
		return marking1;
	}

	public String getMarking2() {
		if (marking2 == null) {
			marking2 = StringUtils.leftPad("" + (int)Math.round(course2 / 10), 2, '0');
		}
		return marking2;
	}
	

}
