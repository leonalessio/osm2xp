package com.osm2xp.core.model.geonames;

import java.io.Serializable;

public class PointList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7275154970349436101L;
	
	private NamedPoint[] points;
	
	public PointList() {
		// TODO Auto-generated constructor stub
	}
	
	public PointList(NamedPoint[] points) {
		this.points = points;
	}

	public NamedPoint[] getPoints() {
		return points;
	}

}
