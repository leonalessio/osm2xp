package com.osm2xp.translators.airfield;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

public class PolyAirfieldData extends AirfieldData {
	
	private Box2D boundingBox;
	private LinearRing2D polygon;

	public PolyAirfieldData(OsmPolyline osmPolyline) {
		super(osmPolyline);
		if (osmPolyline.getPolyline().isClosed() && osmPolyline.getPolyline() instanceof LinearRing2D) {
			polygon = GeomUtils.forceCCW((LinearRing2D) osmPolyline.getPolyline());
		}
		boundingBox =  osmPolyline.getPolyline().boundingBox();
		if (StringUtils.isEmpty(id)) {
			Point2D center = Point2D.centroid(osmPolyline.getPolyline().vertexArray());
			id = String.format(Locale.ROOT, "%1.9f_%2.9f", center.y(), center.x());
		}
	}

	public boolean containsPolyline(OsmPolyline polyline) { //Simplified check for now
		Point2D center = Point2D.centroid(polyline.getPolyline().vertexArray());
		return boundingBox.contains(center);
	}

	public LinearRing2D getPolygon() {
		return polygon;
	}
	
	public Point2D getAreaCenter() {
		return Point2D.centroid(polygon.vertexArray());
	}

	public boolean contains(double lon, double lat) {
		return polygon.isInside(new Point2D(lon, lat));
	}

}
