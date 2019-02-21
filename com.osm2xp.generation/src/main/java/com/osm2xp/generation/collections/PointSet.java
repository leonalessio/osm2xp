package com.osm2xp.generation.collections;

import org.eclipse.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

import math.geom2d.Point2D;

/**
* Set containing {@link Point2D} 
* Needed because Point2D has no adequate hashCode() in new impl
* @author Dmitry Karpenko
*/
public class PointSet extends UnifiedSetWithHashingStrategy<Point2D> {

	public PointSet() {
		super(new PointHashingStrategy());
	}
	
}
