package com.osm2xp.core.model.geonames;

import java.io.Serializable;

public class NamedPoint implements Serializable {
	
	private static final long serialVersionUID = -7089068690632115718L;
	
	private double x;
	private double y;
	private String name;
	
	public NamedPoint(double x, double y, String name) {
		super();
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public NamedPoint() {
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
