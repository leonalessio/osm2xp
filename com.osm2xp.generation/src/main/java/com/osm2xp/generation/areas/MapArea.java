package com.osm2xp.generation.areas;

import org.locationtech.jts.geom.Polygon;

public class MapArea {
	
	public final String type;
	public final Polygon polygon;
	
	public MapArea(String type, Polygon polygon) {
		super();
		this.type = type;
		this.polygon = polygon;
	}

	@Override
	public String toString() {
		return "Area Type:" + type + ", bounds " + polygon.getEnvelopeInternal();
	}
	
}
