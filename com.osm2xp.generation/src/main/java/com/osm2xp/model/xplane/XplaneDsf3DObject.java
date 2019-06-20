package com.osm2xp.model.xplane;

import com.osm2xp.model.osm.polygon.OsmPolygon;

import math.geom2d.Point2D;

/**
 * XplaneDsfObject.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneDsf3DObject extends XplaneDsfObject {

	private Point2D origin;

	public XplaneDsf3DObject() {
	}

	public XplaneDsf3DObject(OsmPolygon osmPolygon, int objectIdx, double angle, Point2D origin) {
		this.osmPolygon = osmPolygon;
		this.dsfIndex = objectIdx;
		this.angle = angle;
		this.origin = origin;
	}

	public Point2D getOrigin() {
		return origin;
	}

	public void setOrigin(Point2D origin) {
		this.origin = origin;
	}

	

}
