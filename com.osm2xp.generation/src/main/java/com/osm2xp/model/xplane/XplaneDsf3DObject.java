package com.osm2xp.model.xplane;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.generation.options.rules.XplaneObjectTagRule;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.utils.MiscUtils;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * XplaneDsfObject.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneDsf3DObject extends XplaneDsfObject {

	private int objectIdx;
	private Point2D origin;

	public XplaneDsf3DObject() {
	}

	public XplaneDsf3DObject(OsmPolygon osmPolygon, int objectIdx, double angle, Point2D origin) {
		this.osmPolygon = osmPolygon;
		this.objectIdx = objectIdx;
		this.angle = angle;
		this.origin = origin;
	}

	@Override
	public String asObjDsfText() throws Osm2xpBusinessException {
		throw new UnsupportedOperationException();
	}

	public int getObjectIdx() {
		return objectIdx;
	}

	public Point2D getOrigin() {
		return origin;
	}

	

}
