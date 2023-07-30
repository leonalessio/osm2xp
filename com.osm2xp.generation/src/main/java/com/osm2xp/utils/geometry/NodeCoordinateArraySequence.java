/**
 * 
 */
package com.osm2xp.utils.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * @author 32kda
 *
 */
public class NodeCoordinateArraySequence extends CoordinateArraySequence {

	public NodeCoordinateArraySequence(Coordinate[] coordinates, int dimension, int measures) {
		super(coordinates, dimension, measures);
	}

	public NodeCoordinateArraySequence(Coordinate[] coordinates, int dimension) {
		super(coordinates, dimension);
	}

	public NodeCoordinateArraySequence(Coordinate[] coordinates) {
		super(coordinates);
	}

	public NodeCoordinateArraySequence(CoordinateSequence coordSeq) {
		super(coordSeq);
	}

	public NodeCoordinateArraySequence(int size, int dimension, int measures) {
		super(size, dimension, measures);
	}

	public NodeCoordinateArraySequence(int size, int dimension) {
		super(size, dimension);
	}

	public NodeCoordinateArraySequence(int size) {
		super(size);
	}
	
	@Override
	public Coordinate createCoordinate() {
		return new NodeCoordinate();
	}

}
