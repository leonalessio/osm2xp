package com.osm2xp.translators.airfield;

import math.geom2d.polygon.LinearCurve2D;
import math.geom2d.polygon.Polyline2D;

public class TaxiLane {

	private final LinearCurve2D line;
	private double width = -1;
	
	public TaxiLane(LinearCurve2D line) {
		super();
		this.line = line;
	}

	public final double getWidth() {
		return width;
	}

	public final void setWidth(double width) {
		this.width = width;
	}

	public final LinearCurve2D getLine() {
		return line;
	}
	
}
