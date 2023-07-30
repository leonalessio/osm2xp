package com.osm2xp.generation.collections;

import gnu.trove.map.hash.TCustomHashMap;
import math.geom2d.Point2D;

/**
 * Map with {@link Point2D} as keys
 * Needed because Point2D has no adequate hashCode() in new impl
 * @author Dmitry Karpenko
 *
 * @param <T> value type
 */
public class PointMap<T> extends TCustomHashMap<Point2D, T> {
	
	public PointMap() {
		super(new PointHashingStrategy());
	}

}
