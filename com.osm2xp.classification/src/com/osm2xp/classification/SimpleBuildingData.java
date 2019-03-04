package com.osm2xp.classification;

import com.osm2xp.classification.annotations.Ignore;

import math.geom2d.Point2D;

public class SimpleBuildingData extends BuildingData {

	@Ignore
	private Point2D center;
	
	public Point2D getCenter() {
		return center;
	}

	public void setCenter(Point2D center) {
		this.center = center;
	}
	
	public void setCenter(double lat, double lon) {
		this.center = new Point2D(lon, lat);
	}
	
}
