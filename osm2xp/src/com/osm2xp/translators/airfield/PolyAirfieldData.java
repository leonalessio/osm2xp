package com.osm2xp.translators.airfield;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;

public class PolyAirfieldData extends AirfieldData {
	
	private Box2D boundingBox;
	private Polyline2D polygon;

	public PolyAirfieldData(OsmPolyline osmPolyline) {
		super(osmPolyline);
		if (osmPolyline.getPolyline().isClosed() && osmPolyline.getPolyline() instanceof LinearRing2D) {
			polygon = GeomUtils.forceCCW((LinearRing2D) osmPolyline.getPolyline());
		}
		boundingBox = osmPolyline.getPolyline().getBoundingBox();
		if (StringUtils.isEmpty(id)) {
			double lat = (boundingBox.getMaxY() + boundingBox.getMinY()) / 2;
			double lon = (boundingBox.getMaxX() + boundingBox.getMinX()) / 2;
			id = String.format("%1.9f_%2.9f", lat, lon);
		}
	}

	public boolean containsPolyline(OsmPolyline polyline) { //Simplified check for now
		Box2D bBox = polyline.getPolyline().getBoundingBox();
		return boundingBox.contains(bBox.getMinX() / 2 + bBox.getMaxX() / 2, bBox.getMinY() / 2 + bBox.getMaxY() / 2);
	}

	public Polyline2D getPolygon() {
		return polygon;
	}
	
	public Point2D getAreaCenter() {
		return Point2D.centroid(polygon.getPointArray());
	}

	public boolean contains(double lon, double lat) {
		return polygon.contains(lon, lat);
	}

}
