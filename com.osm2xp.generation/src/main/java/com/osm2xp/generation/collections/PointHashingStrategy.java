package com.osm2xp.generation.collections;

import org.eclipse.collections.api.block.HashingStrategy;

import math.geom2d.Point2D;

public class PointHashingStrategy implements HashingStrategy<Point2D> {

	private static final long serialVersionUID = -5101301332876705433L;

	@Override
	public int computeHashCode(Point2D pt) {
	    int hash = 1;
	    hash = hash * 31 + Double.valueOf(pt.x()).hashCode();
	    hash = hash * 31 + Double.valueOf(pt.y()).hashCode();
	    return hash;
	}

	@Override
	public boolean equals(Point2D pt1, Point2D pt2) {
		if (pt1 == null) {
			return pt1 == pt2;
		}
		return pt1.equals(pt2);
	}	
}
