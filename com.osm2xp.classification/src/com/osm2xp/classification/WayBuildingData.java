package com.osm2xp.classification;

import java.util.List;

import com.osm2xp.classification.annotations.Ignore;

import math.geom2d.Box2D;
import math.geom2d.Point2D;

public class WayBuildingData extends SimpleBuildingData {

	@Ignore
	private List<Long> nodes;
	
	@Ignore
	private boolean hasHoles;

	@Ignore
	private Box2D boundingBox;

	public WayBuildingData(List<Long> nodes) {
		this.nodes = nodes;
	}

	public List<Long> getNodes() {
		return nodes;
	}
	
	@Override
	public Point2D getCenter() {
		Point2D center = super.getCenter();
		if (center == null && boundingBox != null) {
			center = new Point2D(boundingBox.getMaxX() * 0.5 + boundingBox.getMinX() * 0.5, boundingBox.getMaxY() * 0.5 + boundingBox.getMinY() * 0.5);
			setCenter(center);
		}
		return center;
	}

	public boolean isHasHoles() {
		return hasHoles;
	}

	public void setHasHoles(boolean hasHoles) {
		this.hasHoles = hasHoles;
	}

	public void setBoundingBox(Box2D boundingBox) {
		this.boundingBox = boundingBox;
		
	}

	public Box2D getBoundingBox() {
		return boundingBox;
	}

}
