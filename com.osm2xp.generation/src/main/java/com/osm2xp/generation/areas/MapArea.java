package com.osm2xp.generation.areas;

import org.locationtech.jts.geom.Polygon;

public class MapArea {
	
	public final String tag;
	public final String value;
	public final Polygon polygon;
	
	public MapArea(String tag, String value, Polygon polygon) {
		super();
		this.tag = tag;
		this.value = value;
		this.polygon = polygon;
	}

	@Override
	public String toString() {
		return "Area:" + tag + " = " + value + ", bounds " + polygon.getEnvelopeInternal();
	}
	
}
