package com.osm2xp.translators.airfield;

import math.geom2d.polygon.Polyline2D;

public class TaxiLane {

	private final Polyline2D line;
	private double width = -1;
	
	public TaxiLane(Polyline2D line) {
		super();
		this.line = line;
	}

	public final double getWidth() {
		return width;
	}

	public final void setWidth(double width) {
		this.width = width;
	}

	public final Polyline2D getLine() {
		return line;
	}
	
}
