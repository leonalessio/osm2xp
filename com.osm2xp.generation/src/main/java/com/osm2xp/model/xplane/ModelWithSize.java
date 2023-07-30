package com.osm2xp.model.xplane;

public class ModelWithSize {
	
	private String path;
	private double xSize;
	private double ySize;
	private double height = 0;
	
	public ModelWithSize(String path, double xSize, double ySize) {
		super();
		this.path = path;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public double geXSize() {
		return xSize;
	}

	public void setXSize(double xSize) {
		this.xSize = xSize;
	}

	public double getYSize() {
		return ySize;
	}

	public void setYSize(double ySize) {
		this.ySize = ySize;
	}

	@Override
	public String toString() {
		return path + ": " + xSize + "x" + ySize;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
}
