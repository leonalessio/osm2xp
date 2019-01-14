package com.osm2xp.translators.airfield;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

import math.geom2d.line.Line2D;

public class RunwayData extends AerowayData {
	
	private Line2D runwayLine;
	private double width = XplaneOptionsHelper.getOptions().getAirfieldOptions().getDefaultGrassRunwayWidth();
	private String surface;
	private String ref;
	private boolean hard = false;
	private double course1, course2;
	private String marking1, marking2;

	public RunwayData(OsmPolyline polyline) {
		super(polyline);
		runwayLine = GeomUtils.getCenterline(polyline.getPolyline());
		surface = polyline.getTagValue("surface");
		double magneticCourse = GeomUtils.getMagneticBearing(runwayLine.p1, runwayLine.p2);
		course1 = magneticCourse;
		course2 = (magneticCourse + 180) % 360;
		if (course2 < course1) { //course1 should be always smaller, than course2
			double tmp = course2;
			course2 = course1;
			course1 = tmp;
			runwayLine = runwayLine.getReverseCurve();
		}
		hard = ("asphalt".equals(surface) || "concrete".equals(surface) || "paved".equals(surface));
		if (hard) {
			width = XplaneOptionsHelper.getOptions().getAirfieldOptions().getDefaultHardRunwayWidth();
		}
		String widthStr = polyline.getTagValue("width");
		if (widthStr != null) {
			try {
				width = Double.parseDouble(widthStr);
			} catch (Exception e) {
				// Ignore
			}
		}
		String ref = polyline.getTagValue("ref");
		this.ref = ref;
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
		if (!StringUtils.isEmpty(name)) {
			id = toId(name);
		}
		if (!StringUtils.isEmpty(ref)) {
			id = toId(ref);
		}
		if (id == null) {
			id = getMarking1() + "/" + getMarking2();
		}
	}
	
	public double getCourse1() {
		return course1;
	}

	public double getCourse2() {
		return course2;
	}
	
	public double getTrueCourse() {
		return GeomUtils.getTrueBearing(runwayLine.p1, runwayLine.p2);
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
	
	@Override
	public String toString() {
		if (name != null) {
			return name;
		}
		if (id != null) {
			return id;
		}
		return super.toString();
	}

	public final String getRef() {
		return ref;
	}
}
