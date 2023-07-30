package com.osm2xp.model.geom;

/**
 * Lod13Location.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Lod13Location implements Cloneable {

	private String lod13String;

	private double xOffset;

	private double yOffset;

	public Lod13Location(double xOffset, double yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public Lod13Location() {
	}

	public String getLod13String() {
		return lod13String;
	}

	public void setLod13String(String lod13String) {
		this.lod13String = lod13String;
	}

	public double getxOffset() {
		return xOffset;
	}

	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}

	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	@Override
	public boolean equals(Object obj) {
		Lod13Location location = (Lod13Location) obj;
		return location.getxOffset() == this.xOffset
				&& location.getyOffset() == this.yOffset;
	}

	@Override
	public Lod13Location clone() throws CloneNotSupportedException {
		Lod13Location location = new Lod13Location(this.xOffset, this.yOffset);
		return location;
	}

}
