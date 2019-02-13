package com.osm2xp.translators.airfield;

import com.osm2xp.generation.options.XPlaneOptionsProvider;

public class HelipadData {
	
	private final double lon, lat;
	
	private double heading = -1;

	private double length = XPlaneOptionsProvider.getOptions().getAirfieldOptions().getDefaultHelipadSize();
	
	private double width = XPlaneOptionsProvider.getOptions().getAirfieldOptions().getDefaultHelipadSize();
	
	public HelipadData(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}
	
}
