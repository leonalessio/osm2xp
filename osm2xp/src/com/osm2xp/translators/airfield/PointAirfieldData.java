package com.osm2xp.translators.airfield;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Point2D;

public class PointAirfieldData extends AirfieldData {

	protected Point2D center;
	protected int maxRadius = 2000; //TODO make this settable?
	
	public PointAirfieldData(Node node) {
		super(node);
		center = new Point2D(node.getLon(), node.getLat());
		if (StringUtils.isEmpty(id)) {
			id = String.format(Locale.ROOT, "%1.9f_%2.9f", center.y, center.x);
		}
	}

	@Override
	public boolean containsPolyline(OsmPolyline polyline) {
		Point2D center2 = polyline.getCenter();
		return GeomUtils.latLonDistance(center.y, center.x, center2.y, center2.x) <= maxRadius;
	}

	@Override
	public Point2D getAreaCenter() {
		return center;
	}

	@Override
	public boolean contains(double lon, double lat) {
		return GeomUtils.latLonDistance(center.y, center.x, lat, lon) <= maxRadius;
	}

	public int getMaxRadius() {
		return maxRadius;
	}

	public void setMaxRadius(int maxRadius) {
		this.maxRadius = maxRadius;
	}

}
