package com.osm2xp.classification;

import com.onpositive.classification.core.buildings.OSMBuildingType;
import com.osm2xp.classification.annotations.Ignore;
import com.osm2xp.classification.annotations.Positive;
import com.osm2xp.classification.annotations.Result;

public class BuildingData {
	
	@Result
	private OSMBuildingType type;
	@Positive
	private double perimeter;
	@Positive
	private double area;
	@Positive
	private double maxSide;
//	@Present
	@Positive
//	@Ignore
	private double height;
//	@Present
	@Positive
	private int levels;
	@Positive
	private int sidesCount;
	@Ignore
	private long id;	
	@Ignore
	private boolean hasHoles;
	
	public BuildingData() {
		super();
	}
	
	public void copyProps(BuildingData data) {
		this.type = data.type;
		this.perimeter = data.perimeter;
		this.area = data.area;
		this.maxSide = data.maxSide;
		this.height = data.height;
		this.levels = data.levels;
		this.sidesCount = data.sidesCount;
		this.id = data.id;
	}

	public OSMBuildingType getType() {
		return type;
	}

	public void setType(OSMBuildingType type) {
		this.type = type;
	}

	public double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isHasHoles() {
		return hasHoles;
	}

	public void setHasHoles(boolean hasHoles) {
		this.hasHoles = hasHoles;
	}

}
