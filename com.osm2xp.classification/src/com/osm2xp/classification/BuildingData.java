package com.osm2xp.classification;

import com.osm2xp.core.model.osm.BuildingType;

public class BuildingData {
	
	private BuildingType type;
	private double perimeter;
	private double square;
	private double maxSide;
	private double height;
	private int levels;
	private int sidesCount;
	
	public BuildingData() {
		super();
	}
	
	public void copyProps(BuildingData data) {
		this.type = data.type;
		this.perimeter = data.perimeter;
		this.square = data.square;
		this.maxSide = data.maxSide;
		this.height = data.height;
		this.levels = data.levels;
		this.sidesCount = data.sidesCount;
	}

	public BuildingType getType() {
		return type;
	}

	public void setType(BuildingType type) {
		this.type = type;
	}

	public double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}

	public double getSquare() {
		return square;
	}

	public void setSquare(double square) {
		this.square = square;
	}

	public double getMaxSide() {
		return maxSide;
	}

	public void setMaxSide(double maxSide) {
		this.maxSide = maxSide;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public int getSidesCount() {
		return sidesCount;
	}

	public void setSidesCount(int sidesCount) {
		this.sidesCount = sidesCount;
	}
	
}
