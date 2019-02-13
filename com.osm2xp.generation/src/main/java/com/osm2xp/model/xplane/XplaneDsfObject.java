package com.osm2xp.model.xplane;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.polygon.OsmPolygon;

/**
 * XplaneDsfObject.
 * 
 * @author Benjamin Blanchet
 * 
 */
public abstract class XplaneDsfObject {

	protected Integer dsfIndex;
	protected OsmPolygon osmPolygon;
	private Integer angle;

	public Integer getAngle() {
		return angle;
	}

	public void setAngle(Integer angle) {
		this.angle = angle;
	}

	public XplaneDsfObject() {

	}

	public XplaneDsfObject(OsmPolygon poly) {
		this.osmPolygon = poly;
	}

	public Integer getDsfIndex() {
		return dsfIndex;
	}

	public void setDsfIndex(Integer dsfIndex) {
		this.dsfIndex = dsfIndex;
	}

	public OsmPolygon getOsmPolygon() {
		return osmPolygon;
	}

	public void setPolygon(OsmPolygon polygon) {
		this.osmPolygon = polygon;
	}

	public abstract String asObjDsfText() throws Osm2xpBusinessException;

}
