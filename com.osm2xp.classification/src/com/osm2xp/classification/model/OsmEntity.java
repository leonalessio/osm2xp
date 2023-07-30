package com.osm2xp.classification.model;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;

import math.geom2d.Box2D;
import math.geom2d.Point2D;

public class OsmEntity {
	
	protected final List<Tag> tags;
	
	protected final long id;
	
	protected Point2D center;
	
	protected Box2D boundingBox;

	public OsmEntity(long id, List<Tag> tags) {
		super();
		this.id = id;
		this.tags = tags;
	}

	public List<Tag> getTags() {
		return tags;
	}
	
	public Point2D getCenter() {
		if (center == null && boundingBox != null) {
			center = new Point2D(boundingBox.getMaxX() * 0.5 + boundingBox.getMinX() * 0.5, boundingBox.getMaxY() * 0.5 + boundingBox.getMinY() * 0.5);
		}
		return center;
	}
	
	public void setCenter(Point2D center) {
		this.center = center;
	}
	
	public void setCenter(double lat, double lon) {
		this.center = new Point2D(lon, lat);
	}

	public Box2D getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Box2D boundingBox) {
		this.boundingBox = boundingBox;
	}

	public long getId() {
		return id;
	}
}
