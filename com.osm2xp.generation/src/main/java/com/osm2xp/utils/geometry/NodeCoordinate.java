package com.osm2xp.utils.geometry;

import org.locationtech.jts.geom.Coordinate;

public class NodeCoordinate extends Coordinate {

	private static final long serialVersionUID = -639250971228091932L;
	private long nodeId;

	public NodeCoordinate(double x, double y, long nodeId) {
		super(x, y);
		this.nodeId = nodeId;
	}

	public NodeCoordinate() {
		// Default constructor
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
	
	@Override
	public void setCoordinate(Coordinate other) {
		if (other instanceof NodeCoordinate) {
			nodeId = ((NodeCoordinate) other).getNodeId();
		}
		super.setCoordinate(other);
	}

}
