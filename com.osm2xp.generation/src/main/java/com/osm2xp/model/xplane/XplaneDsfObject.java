package com.osm2xp.model.xplane;

import com.osm2xp.model.osm.polygon.OsmPolygon;

/**
 * XplaneDsfObject.
 * 
 * @author Benjamin Blanchet
 * 
 */
public abstract class XplaneDsfObject {

	protected int dsfIndex;
	protected OsmPolygon osmPolygon;
	protected double angle;

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public XplaneDsfObject() {

	}

	public XplaneDsfObject(OsmPolygon poly) {
		this.osmPolygon = poly;
	}

	public int getDsfIndex() {
		return dsfIndex;
	}

	public void setDsfIndex(int dsfIndex) {
		this.dsfIndex = dsfIndex;
	}

	public OsmPolygon getOsmPolygon() {
		return osmPolygon;
	}

	public void setPolygon(OsmPolygon polygon) {
		this.osmPolygon = polygon;
	}

}
